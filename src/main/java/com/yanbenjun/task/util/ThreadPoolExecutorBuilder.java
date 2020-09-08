package com.yanbenjun.task.util;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.yanbenjun.task.DefaultRejectedHandler;
import com.yanbenjun.task.DefaultThreadFactory;
import com.yanbenjun.task.TaskPoolConfig;

public class ThreadPoolExecutorBuilder
{

    /**
     * 考虑把所有任务信息存入数据库，服务器从数据库一旦拥有额外的资源就读取任务信息进行任务计算
     */
    public static ThreadPoolExecutor newPool(TaskPoolConfig config)
    {
        return newPool(config, new DefaultRejectedHandler());
    }

    /**
     * 考虑把所有任务信息存入数据库，服务器从数据库一旦拥有额外的资源就读取任务信息进行任务计算
     */
    public static ThreadPoolExecutor newPoolWithAbortPolicy(TaskPoolConfig config)
    {
        return newPool(config, new ThreadPoolExecutor.AbortPolicy());
    }

    /**
     * 考虑把所有任务信息存入数据库，服务器从数据库一旦拥有额外的资源就读取任务信息进行任务计算
     */
    private static ThreadPoolExecutor newPool(TaskPoolConfig config, RejectedExecutionHandler handler)
    {
        int corePoolSize = config.getCorePoolSize();
        int maximumPoolSize = config.getMaximumPoolSize();
        long keepAliveTime = config.getKeepAliveTime();
        TimeUnit unit = config.getUnit();
        BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<>(config.getQueueSize());
        ThreadFactory threadFactory = new DefaultThreadFactory(config.getThreadFactoryName());
        return new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory,
                handler);
    }

}
