package com.yanbenjun.task;

import java.util.concurrent.TimeUnit;

/**
 * int corePoolSize = 5;
        int maximumPoolSize = 5;
        long keepAliveTime = 60;
        TimeUnit unit = TimeUnit.SECONDS;
        BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<>(1);
        ThreadFactory threadFactory = new ThreadFactory()
        RejectedExecutionHandler handler = new RejectedExecutionHandler()
 * @author Administrator
 *
 */
public class TaskPoolConfig
{
    private static TaskPoolConfig instance = null;
    
    private int corePoolSize = 5;
    private int maximumPoolSize = 5;
    private long keepAliveTime = 60000;
    private TimeUnit unit = TimeUnit.SECONDS;
    
    private int queueSize = 5;
    private int queueType;
    
    private String threadFactoryName = "yanbenjun_test";
    private String handler = "default";
    
    public static TaskPoolConfig getDefaultConfig()
    {
        if(instance == null)
        {
            instance = new TaskPoolConfig();
        }
        return instance;
    }
    
    public static TaskPoolConfig getBranchThreadPoolConfig()
    {
        TaskPoolConfig pool = new TaskPoolConfig();
        pool.setQueueSize(Integer.MAX_VALUE);
        pool.setThreadFactoryName("BranchThread");
        return pool;
    }
    
    public int getCorePoolSize()
    {
        return corePoolSize;
    }
    public void setCorePoolSize(int corePoolSize)
    {
        this.corePoolSize = corePoolSize;
    }
    public int getMaximumPoolSize()
    {
        return maximumPoolSize;
    }
    public void setMaximumPoolSize(int maximumPoolSize)
    {
        this.maximumPoolSize = maximumPoolSize;
    }
    public long getKeepAliveTime()
    {
        return keepAliveTime;
    }
    public void setKeepAliveTime(long keepAliveTime)
    {
        this.keepAliveTime = keepAliveTime;
    }
    public TimeUnit getUnit()
    {
        return unit;
    }
    public void setUnit(TimeUnit unit)
    {
        this.unit = unit;
    }
    public int getQueueSize()
    {
        return queueSize;
    }
    public void setQueueSize(int queueSize)
    {
        this.queueSize = queueSize;
    }
    public int getQueueType()
    {
        return queueType;
    }
    public void setQueueType(int queueType)
    {
        this.queueType = queueType;
    }
    public String getThreadFactoryName()
    {
        return threadFactoryName;
    }
    public void setThreadFactoryName(String threadFactoryName)
    {
        this.threadFactoryName = threadFactoryName;
    }
    public String getHandler()
    {
        return handler;
    }
    public void setHandler(String handler)
    {
        this.handler = handler;
    }
}
