package com.yanbenjun.task.inf;

import java.util.concurrent.ThreadPoolExecutor;

import com.yanbenjun.task.TaskPoolConfig;
import com.yanbenjun.task.model.progress.TaskProgressManager;
import com.yanbenjun.task.model.request.TaskRequest;
import com.yanbenjun.task.util.ThreadPoolExecutorBuilder;

/**
 * 通过继承实现这个类，可以实现跨服务任务的执行而不用担心服务之间本身的超时链接
 * 启动执行-等待执行完毕-进行下一个任务
 * @author Administrator
 *
 */
public abstract class SimpleAsyncBranchTask extends AsyncBranchTask
{
    private static ThreadPoolExecutor taskExecutePool = ThreadPoolExecutorBuilder.newPoolWithAbortPolicy(TaskPoolConfig.getBranchThreadPoolConfig());

    protected SimpleAsyncBranchTask(TaskRequest taskRequest, TaskProgressManager taskProgressManager)
    {
        super(taskRequest, taskProgressManager, taskExecutePool);
    }
}
