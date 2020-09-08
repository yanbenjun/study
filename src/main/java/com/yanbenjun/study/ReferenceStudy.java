package com.yanbenjun.study;

import java.lang.ref.PhantomReference;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;

import com.yanbenjun.util.TimeUtil;

public class ReferenceStudy
{
    public static void main(String[] args) throws InterruptedException
    {
        ReferenceQueue<TestObject> q = new ReferenceQueue<>();
        TestObject o1 = new TestObject(1);
        o1 = null;
        SoftReference<TestObject> sr = new SoftReference<TestObject>(new TestObject(2),q);
        //WeakReference<TestObject> sr2 = new WeakReference<TestObject>(new TestObject(3),q);
        PhantomReference<TestObject> sr3 = new PhantomReference<TestObject>(new TestObject(4),q);
        System.gc();
        Reference<? extends TestObject> o3 = q.remove();
        o3.get().print();
        TimeUtil.sleep(1000);
        ThreadLocal<Object> t = null;
    }
}
