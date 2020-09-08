package com.yanbenjun.task.inf;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.yanbenjun.task.model.progress.TaskProgressManager;
import com.yanbenjun.task.model.progress.TaskStatus;
import com.yanbenjun.task.model.request.TaskRequest;
import com.yanbenjun.task.model.response.ContentType;
import com.yanbenjun.task.model.response.TaskResponse;

public abstract class AsyncLeafTask extends AsyncTask implements Leaf
{
    private Future<?> futureResult;
    
    protected AsyncLeafTask(TaskRequest taskRequest, TaskProgressManager taskProgressManager,
            ThreadPoolExecutor threadPool)
    {
        super(taskRequest, taskProgressManager, threadPool);
    }
    protected abstract void handle(TaskRequest tskReq);
    
    @Override
    public void run()
    {
        try
        {
            handle(this.taskRequest);
        }
        catch (Throwable e)
        {
            finishNotify(TaskStatus.FAIL);
            e.printStackTrace();
            throw e;//这个地方抛出异常没用，控制台不会打印任何消息，会被Future的ExecutionException捕获
        }
        finishNotify(TaskStatus.SUCCESS);
    }

    /**
     * 异步任务执行入口
     */
    @Override
    public TaskResponse execute()
    {
        try
        {
            TaskResponse response = init();
            response.getHeader().setContentType(ContentType.FUTURE);
            this.futureResult = threadPool.submit(this);
            response.setFuture(futureResult);
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
    public void waitFinish(long time, TimeUnit unit) throws TimeoutException
    {
        if(this.futureResult == null)
        {
            throw new RuntimeException("非法的操作，任务还没有开始执行。");
        }
        long startTime = System.nanoTime();
        while(true)
        {
            try
            {
                long leftTime = unit.toNanos(time) - (System.nanoTime() - startTime);
                if(leftTime <= 0)
                {
                    throw new TimeoutException();
                }
                futureResult.get(leftTime, TimeUnit.NANOSECONDS);
                return;
            }
            catch (InterruptedException e)
            {
                // TODO 打印被打断日志记录
            }
            catch (ExecutionException e)
            {
                // TODO 打印任务执行错误日志
                e.printStackTrace();
            }
        }
    }

    public Future<?> getFutureResult()
    {
        return futureResult;
    }
}
