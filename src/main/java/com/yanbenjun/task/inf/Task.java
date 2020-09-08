package com.yanbenjun.task.inf;

import com.yanbenjun.task.model.progress.TaskProgress;
import com.yanbenjun.task.model.response.TaskResponse;

public interface Task
{
    public TaskResponse execute() throws Throwable;
    
    public TaskProgress progress();
}
