package com.study.lock;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.LockSupport;

/**
 * 可重入读写锁实现，读锁和写锁的状态改变不是原子的版本
 * @author Administrator
 *
 */
public class YbjReentrantReadWriteLock
{
    private AtomicInteger readCount = new AtomicInteger(0);
    private AtomicInteger writeCount = new AtomicInteger(0);
    //只有写线程能成为owner
    private AtomicReference<Thread> owner = new AtomicReference<>();
    private volatile LinkedBlockingQueue<WaitNode> lockWaitors = new LinkedBlockingQueue<>();
    
    public static class WaitNode {
        Thread thread;
        boolean write;
        int arg;
        public WaitNode(Thread thread, boolean write, int arg) {
            this.thread = thread;
            this.write = write;
            this.arg = arg;
        }
    }

    public void lock()
    {
        int acqurie = 1;
        if(!tryLock(acqurie)) {
            //放入队列，用什么方法？
            WaitNode node = new WaitNode(Thread.currentThread(), true, acqurie);
            lockWaitors.offer(node);
            while(true) {
                node = lockWaitors.peek();
                if (node != null && node.thread == Thread.currentThread()) {//为什么必须判断头部是当前线程本身？
                    //因为，程序代码这里当前是在为执行到这里的线程本身抢锁，抢到锁之后，应该移除队列的也必须是当前线程，否则不是本身的话
                    //就相当于我线程抢到了锁，但是我把你从队列里移除了
                    if(tryLock(acqurie)) {
                        lockWaitors.poll();
                        return;
                    } else {
                        LockSupport.park();
                    }
                } else {
                    LockSupport.park();
                }
            }
        }
        
    }

    public boolean tryLock(int acqurie)
    {
        int rc = readCount.get();
        if (rc != 0) {
            return false;//为什么直接只判断写锁不为0就返回，这和jdk的读写锁实现是一致的，不允许同一个线程读锁，升级写锁//如果rc==1，是否能判断这个获取了唯一读锁的线程是否是来抢锁的线程，貌似判断不了
        }
        int count = writeCount.get();
        if (count == 0) {
            //利用原子操作，去抢写锁（设置writeCount=1）但是这里与上面readCount的判断会有原子性问题，可能此时readCount被别的线程修改了
            //所以需要一个判断read，write，和设置write的原子操作，JDK是将readCount和WriteCount用一个整形的高半位和低半位分别来表示实现的。
            //这里为了简单，先不管
            //抢锁
            //bug1，不要把参数传反了，否则不会成功,bug2,应该设置为获取的count + acqurie
             //bug2 boolean success = writeCount.compareAndSet(0, acqurie);
             boolean success = writeCount.compareAndSet(count, count + acqurie);
             //成功则设置当前线程为owner
             if (success) {
                 owner.set(Thread.currentThread());//bug，这里抢成功了没有返回true，那么会一直抢不成功
                 return true;
             }
        } else {
            //能直接返回吗，不能
            if(owner.get() == Thread.currentThread()) {//写锁重入
                writeCount.set(count + acqurie);//这里可以直接修改值
            }
            return false;
        }
        
        return false;
    }

    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException
    {
        throw new UnsupportedOperationException();
    }

    public void unlock()
    {
        int acquire = 1;
        if (tryUnlock(acquire)) {
            WaitNode next = lockWaitors.peek();
            if (next != null) {
                Thread t = next.thread;
                LockSupport.unpark(t);
            }
        }
        System.out.println("writeCount"+writeCount);
        System.out.println("readCount"+readCount);
    }
    
    public boolean tryUnlock(int acquire) {
        if (Thread.currentThread() != owner.get()) {
            throw new IllegalMonitorStateException();
        } else {
            int count = writeCount.get();
            writeCount.set(count - acquire);
            if (writeCount.get() == 0) {
                //为什么要用原子操作
                //按理说只有获得到锁的线程才能走到这里，owner也不会被获取锁的地方改变
                //1。不会被释放锁的改变，2、抢锁的线程呢？其它线程此时能抢锁吗，能，因为writeCount==0
                //因为writeCount先被修改为0，此时其它线程可以去抢写锁，抢到后owner被修改为其它线程，若不采用CAS操作，可能会覆盖成功抢锁的owner为空,但是此时锁确实另外一个线程的
                //所以要用原子操作，防止覆盖
                owner.compareAndSet(Thread.currentThread(), null);
                return true;
            }
            return false;
        }
    }
    

    public void lockShared()
    {
        int acqurie = 1;
        if (!tryLockShared(acqurie)) {//没有获取成功，说明有线程获取到了写锁
            //需要进入等待队列
            WaitNode node = new WaitNode(Thread.currentThread(), false, acqurie);
            lockWaitors.offer(node);
            while(true) {//自旋为队列中的头元素获取共享锁
                WaitNode next = lockWaitors.peek();
                //if (node != null && next.thread [bug3写成了不等于，正好造成了移除别的线程的问题] != Thread.currentThread()) {//为什么必须判断头部是当前线程本身？
                if (node != null && next.thread == Thread.currentThread()) {//为什么必须判断头部是当前线程本身？
                    //因为，程序代码这里当前是在为执行到这里的线程本身抢锁，抢到锁之后，应该移除队列的也必须是当前线程，否则不是本身的话
                    //就相当于我线程抢到了锁，但是我把你从队列里移除了
                    //尝试
                    if(!tryLockShared(acqurie)) {//尝试失败，挂起
                        LockSupport.park();
                    } else {//尝试成功，返回；不是的，要删除头元素，又返回，不是的，还要继续返回下一个元素，如果是读锁，继续尝试获取共享锁
                        //删除当前成功抢锁的节点
                        lockWaitors.poll();
                        //但是这里不用去拿下一个节点尝试只需要唤醒即可
                        next = lockWaitors.peek();
                        if (next != null && !next.write) {
                            LockSupport.unpark(next.thread);//被唤醒的线程会继续通知下一个unpark
                        }
                        return;
                    }
                } else {//如果为空，或者是获取写锁的，则挂起，写锁的自旋会自己处理
                    LockSupport.park();
                }
            }
        }
        
    }

    public boolean tryLockShared(int acqurie)
    {
        while(true){//为什么要用自旋操作，因为，可能两个线程同时获取读锁，有一个失败，则需要让它继续尝试，直到获取成功或者有其它线程获取到写锁，否则只尝试一次，可能失败，外层还需要这个for逻辑
            if (writeCount.get() == 0 || owner.get() == Thread.currentThread()) {
                int rc = readCount.get();
                if(readCount.compareAndSet(rc, rc + acqurie)) {//这里也存在获取写锁和读锁的原子问题
                    return true;
                }
            } else {
                return false;
            }
        }
    }

    public boolean tryLockShared(long time, TimeUnit unit) throws InterruptedException
    {
        throw new UnsupportedOperationException();
    }

    public void unlockSharedBadPratice()
    {
        int rc = readCount.get();
        int update = rc - 1;
        //不能这么做readCount.set(rc - 1);//xxxxx，这里可能多个线程同时修改，引起线程安全问题
        if (readCount.compareAndSet(rc, rc - 1)) {
            if (update == 0) {
                WaitNode next = lockWaitors.peek();
                if (next != null) {
                    LockSupport.unpark(next.thread);
                }
            }
        } else {
            //失败继续获取最新值，自旋设置readCount，所以这里应该用循环
        }
        
    }
    
    public void unlockShared()
    {
        int acquire = 1;
        if(tryUnlockShared(acquire)) {
            WaitNode next = lockWaitors.peek();
            if (next != null) {
                LockSupport.unpark(next.thread);
            }
        }
    }
    
    public boolean tryUnlockShared(int acquire) {
        while(true) {//为什么循环，原因同尝试获取共享锁
            int rc = readCount.get();
            int update = rc - acquire;
            if (readCount.compareAndSet(rc, update)) {
                return update == 0;
            }
        }
    }

}
