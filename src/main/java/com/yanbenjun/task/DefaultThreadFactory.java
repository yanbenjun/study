package com.yanbenjun.task;

import java.util.concurrent.ThreadFactory;

/**
 * 没考虑线程安全性
 * i
 * @author Administrator
 *
 */
public class DefaultThreadFactory implements ThreadFactory
{
    private String name;
    private int i;
    
    public DefaultThreadFactory(String name)
    {
        this.name = name;
    }

    @Override
    public Thread newThread(Runnable r)
    {
        Thread t = new Thread(r);
        t.setName(this.name + "_thread_" + (++i));
        return t;
    }

}
