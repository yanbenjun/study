package com.yanbenjun.task.model.waitor;

import com.yanbenjun.task.model.progress.TaskProgressManager;

public class TaskWaitor
{
    private TaskProgressManager taskProgressManager;
    
    public TaskWaitor(TaskProgressManager taskProgressManager)
    {
        this.taskProgressManager = taskProgressManager;
    }

    public TaskProgressManager getTaskProgressManager()
    {
        return taskProgressManager;
    }
}
