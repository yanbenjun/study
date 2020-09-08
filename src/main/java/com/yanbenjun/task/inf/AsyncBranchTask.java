package com.yanbenjun.task.inf;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.yanbenjun.task.model.progress.TaskProgress;
import com.yanbenjun.task.model.progress.TaskProgressManager;
import com.yanbenjun.task.model.request.TaskRequest;
import com.yanbenjun.task.model.response.TaskResponse;

/**
 * 跨服务启动的任务继承此类
 * 
 * @author Administrator
 *
 */
public abstract class AsyncBranchTask extends AsyncTask implements Branch
{
    private List<AsyncTask> children = new ArrayList<>();
    protected AsyncBranchTask(TaskRequest taskRequest, TaskProgressManager taskProgressManager,
            ThreadPoolExecutor threadPool)
    {
        super(taskRequest, taskProgressManager, threadPool);
        children.addAll(newAllChildrenTask());
    }
    
    public abstract List<AsyncTask> newAllChildrenTask();

    @Override
    public final void run()
    {
        for (AsyncTask asyncTask : children)
        {
            try
            {
                asyncTask.execute();
            }
            catch (Throwable e)
            {
                e.printStackTrace();
            }
        }
    }

    /**
     * 异步任务执行入口
     */
    @Override
    public final TaskResponse execute()
    {
        try
        {
            TaskResponse response = init();
            threadPool.execute(this);
            return response;
        }
        catch (Throwable e)
        {
            // TODO
            e.printStackTrace();
            return null;
        }
        finally
        {
            // TODO
        }
    }

    @Override
    public final void waitFinish(long time, TimeUnit unit) throws TimeoutException
    {
        long leftTime = time;
        for (AsyncTask asyncTask : children)
        {
            long start = System.nanoTime();
            asyncTask.waitFinish(leftTime, unit);
            long end = System.nanoTime();
            leftTime = leftTime - (end - start);
            if(leftTime <= 0)
            {
                throw new TimeoutException("wait timeout in task: " + this.taskRequest.getTaskId());
            }
        }
    }
    

    @Override
    public TaskProgress progress()
    {
        return taskProgressManager.query(this.taskRequest.getTaskId());
    }
}
