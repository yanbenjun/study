package com.yanbenjun.task.inf;

import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.yanbenjun.task.model.TaskStartServer;
import com.yanbenjun.task.model.progress.TaskProgress;
import com.yanbenjun.task.model.progress.TaskProgressManager;
import com.yanbenjun.task.model.progress.TaskStatus;
import com.yanbenjun.task.model.request.TaskRequest;
import com.yanbenjun.task.model.response.TaskResponse;
import com.yanbenjun.task.model.waitor.TaskWaitorManager;

public abstract class AsyncTask extends AbstractTask implements Runnable
{
    protected ThreadPoolExecutor threadPool;

    protected AsyncTask(TaskRequest taskRequest, TaskProgressManager taskProgressManager, ThreadPoolExecutor threadPool)
    {
        super(taskRequest, taskProgressManager);
        this.threadPool = threadPool;
    }
    
    /**
     * 
     * @param time
     * @param unit
     * @throws TimeoutException
     */
    public abstract void waitFinish(long time, TimeUnit unit) throws TimeoutException;
    
    @Override
    protected TaskResponse init()
    {
        TaskResponse response = super.init();
        this.taskRequest.setTaskStartServer(TaskStartServer.local());
        TaskWaitorManager.instance().init(this.taskRequest.getTaskId(), taskProgressManager);
        this.taskProgressManager.init(this.taskRequest.getTaskId(), new TaskProgress());
        return response;
    }

    @Override
    protected TaskProgress refreshProgress(int percent)
    {
        return taskProgressManager.refresh(this.taskRequest.getTaskId(), percent);
    }

    @Override
    protected void finishNotify(TaskStatus taskStatus)
    {
        final String taskId = this.taskRequest.getTaskId();
        taskProgressManager.refresh(taskId, taskStatus.getValue());
        TaskWaitorManager.instance().finishNotify(this.taskRequest);
    }

    public ThreadPoolExecutor getThreadPool()
    {
        return threadPool;
    }

    public void setThreadPool(ThreadPoolExecutor threadPool)
    {
        this.threadPool = threadPool;
    }

}
