package com.yanbenjun.task.inf;

import java.util.Random;

import com.yanbenjun.task.model.progress.TaskProgress;
import com.yanbenjun.task.model.progress.TaskProgressManager;
import com.yanbenjun.task.model.progress.TaskStatus;
import com.yanbenjun.task.model.request.TaskRequest;
import com.yanbenjun.task.model.response.ContentType;
import com.yanbenjun.task.model.response.TaskResponse;
import com.yanbenjun.task.model.response.TaskResponseHeader;

public abstract class AbstractTask implements Task
{
    protected TaskRequest taskRequest;

    protected TaskProgressManager taskProgressManager;

    protected AbstractTask(TaskRequest taskRequest, TaskProgressManager taskProgressManager)
    {
        this.taskRequest = taskRequest;
        this.taskProgressManager = taskProgressManager;
    }

    protected TaskResponse init()
    {
        TaskResponse response = new TaskResponse();
        response.setStatusCode(200);
        TaskResponseHeader header = new TaskResponseHeader();
        header.setContentType(ContentType.JSON);
        header.setTaskId(this.taskRequest.getTaskId());
        header.setTaskName(this.taskRequest.getTaskName());
        response.setHeader(header);
        return response;
    }

    protected abstract TaskProgress refreshProgress(int percent);

    protected abstract void finishNotify(TaskStatus taskStatus);

    /**
     * Test 用
     * 
     * @param time
     */
    protected void sleep(long time)
  {
        Random rand = new Random();
        long period = rand.nextInt(3000) + time;
        try
        {
            Thread.sleep(period);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }

    public TaskRequest getTaskRequest()
    {
        return taskRequest;
    }

    public void setTaskRequest(TaskRequest taskRequest)
    {
        this.taskRequest = taskRequest;
    }
}
