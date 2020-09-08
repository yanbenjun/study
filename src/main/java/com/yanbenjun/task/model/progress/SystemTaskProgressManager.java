package com.yanbenjun.task.model.progress;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SystemTaskProgressManager implements TaskProgressManager
{
    private static Map<String, TaskProgress> CACHE = new ConcurrentHashMap<>();
    @Override
    public TaskProgress init(String taskId, TaskProgress taskProgress) throws TaskProgressOperationException
    {
        CACHE.put(taskId, taskProgress);
        return taskProgress.clone();
    }

    @Override
    public TaskProgress query(String taskId) throws TaskProgressOperationException
    {
        TaskProgress taskProgress = CACHE.get(taskId);
        if(taskProgress != null)
        {
            return taskProgress.clone();
        }
        return null;
    }

    @Override
    public TaskProgress refresh(String taskId, int percent) throws TaskProgressOperationException
    {
        TaskProgress taskProgress = CACHE.get(taskId);
        if(taskProgress != null)
        {
            synchronized (taskProgress)
            {
                percent = percent > TaskProgress.FINISH ? TaskProgress.FINISH : percent;
                percent = percent < taskProgress.getPercent() ? taskProgress.getPercent() : percent;
                taskProgress.setPercent(percent);
                if(percent >= TaskProgress.FINISH)
                {
                    taskProgress.setStatus(TaskStatus.FINISH.getValue());
                }
                return taskProgress.clone();
            }
        }
        return null;
    }
    
    public static SystemTaskProgressManager instance()
    {
        return SystemTaskProgressManagerBuilder.INSTANCE;
    }
    
    private static class SystemTaskProgressManagerBuilder
    {
        private static final SystemTaskProgressManager INSTANCE = new SystemTaskProgressManager();
    }

    @Override
    public TaskProgress remove(String taskId) throws TaskProgressOperationException
    {
        return CACHE.remove(taskId);
    }
}
