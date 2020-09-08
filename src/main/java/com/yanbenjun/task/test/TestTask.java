package com.yanbenjun.task.test;

import com.yanbenjun.task.TaskService;
import com.yanbenjun.task.inf.SimpleAsyncLeafTask;
import com.yanbenjun.task.model.progress.TaskProgress;
import com.yanbenjun.task.model.progress.TaskProgressManager;
import com.yanbenjun.task.model.request.TaskRequest;

/**
 * 
 * @author Administrator
 *
 */
public class TestTask extends SimpleAsyncLeafTask
{
    private TaskService taskService;
    protected TestTask(TaskRequest taskRequest, TaskProgressManager taskProgressManager)
    {
        super(taskRequest, taskProgressManager);
    }

    @Override
    protected void handle(TaskRequest tskReq)
    {
        taskService.start(tskReq);
        
    }

    @Override
    public TaskProgress progress()
    {
        // TODO Auto-generated method stub
        return null;
    }
}
