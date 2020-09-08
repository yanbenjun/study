package com.yanbenjun.task;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

public class DefaultRejectedHandler implements RejectedExecutionHandler
{

    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor)
    {
        System.out.println("任务被拒绝执行了，该任务会在后面再次通过读取数据库信息中的任务信息尝试加入到任务执行中。");
    }

}
