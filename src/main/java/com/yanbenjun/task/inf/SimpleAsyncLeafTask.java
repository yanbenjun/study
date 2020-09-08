package com.yanbenjun.task.inf;

import java.util.concurrent.ThreadPoolExecutor;

import com.yanbenjun.task.TaskPoolConfig;
import com.yanbenjun.task.model.progress.TaskProgressManager;
import com.yanbenjun.task.model.request.TaskRequest;
import com.yanbenjun.task.util.ThreadPoolExecutorBuilder;

public abstract class SimpleAsyncLeafTask extends AsyncLeafTask
{
    private static ThreadPoolExecutor taskExecutePool = ThreadPoolExecutorBuilder.newPoolWithAbortPolicy(TaskPoolConfig.getDefaultConfig());

    protected SimpleAsyncLeafTask(TaskRequest taskRequest, TaskProgressManager taskProgressManager)
    {
        super(taskRequest, taskProgressManager, taskExecutePool);
    }
}
