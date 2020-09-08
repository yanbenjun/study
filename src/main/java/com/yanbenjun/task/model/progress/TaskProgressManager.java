package com.yanbenjun.task.model.progress;

public interface TaskProgressManager
{
    public TaskProgress init(String taskId, TaskProgress taskProgress) throws TaskProgressOperationException;
    
    public TaskProgress query(String taskId) throws TaskProgressOperationException;
    
    public TaskProgress refresh(String taskId, int percent) throws TaskProgressOperationException;
    
    public TaskProgress remove(String taskId) throws TaskProgressOperationException;
}
