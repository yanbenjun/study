package com.yanbenjun.task.model.waitor;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.yanbenjun.task.model.TaskStartServer;
import com.yanbenjun.task.model.progress.TaskProgress;
import com.yanbenjun.task.model.progress.TaskProgressManager;
import com.yanbenjun.task.model.progress.TaskStatus;
import com.yanbenjun.task.model.request.TaskRequest;

public class TaskWaitorManager
{
    private static Map<String, TaskWaitor> CACHE = new ConcurrentHashMap<>();

    public TaskWaitor init(String taskId, TaskProgressManager manager)
    {
        return CACHE.putIfAbsent(taskId, new TaskWaitor(manager));
    }

    public TaskWaitor get(String taskId)
    {
        return CACHE.get(taskId);
    }

    /**
     * 任务开始后，等待任务结束（通过让任务阻塞在taskWaitor处，不管其他任务在哪执行，都会在任务结束后通知taskId对应的taskWaitor任务结束，阻塞完毕。
     * @param unit 等待时间的单位
     * @param period 等待时长（单位unit）
     * @param taskId 等待的任务ID
     * @throws TimeoutException 超时跳出阻塞
     */
    public void waitFinish(TimeUnit unit, long period, final String taskId) throws TimeoutException
    {
        long startTime = System.nanoTime();
        TaskWaitor taskWaitor = TaskWaitorManager.instance().get(taskId);
        if (taskWaitor != null)
        {
            synchronized (taskWaitor)
            {
                TaskProgressManager taskProgressManager = taskWaitor.getTaskProgressManager();
                TaskProgress taskProgress = taskProgressManager.query(taskId);
                while (taskProgress != null && (taskProgress.getStatus() == TaskStatus.INIT.getValue()
                        || taskProgress.getStatus() == TaskStatus.RUNNING.getValue()))
                {
                    try
                    {
                        unit.timedWait(taskWaitor, period);
                    }
                    catch (InterruptedException e)
                    {
                    }

                    long curTime = System.nanoTime();
                    if (curTime - startTime > unit.toNanos(period))
                    {
                        throw new TimeoutException("等待任务执行超时，任务ID: " + taskId);
                    }
                    taskProgress = taskProgressManager.query(taskId);
                }
            }
        }

    }

    /**
     * 任务结束，根据请求 发送结束通知
     * @param taskRequest 请求
     */
    public void finishNotify(TaskRequest taskRequest)
    {
        final String taskId = taskRequest.getTaskId();
        TaskWaitor taskWaitor = CACHE.remove(taskId);
        if (taskWaitor != null)
        {
            // 通知本JVM进程该任务结束
            synchronized (taskWaitor)
            {
                taskWaitor.notifyAll();
            }
        }
        
        //根据taskRequest中的TaskStartServer，通知启动该任务的服务JVM该任务结束
        TaskStartServer tsServer = taskRequest.getTaskStartServer();
        tsServer.notifyFinish(taskId);
    }

    public static TaskWaitorManager instance()
    {
        return TaskWaitorManagerBuilder.INSTANCE;
    }

    private static class TaskWaitorManagerBuilder
    {
        private static final TaskWaitorManager INSTANCE = new TaskWaitorManager();
    }
}
