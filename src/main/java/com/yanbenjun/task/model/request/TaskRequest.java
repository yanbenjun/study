package com.yanbenjun.task.model.request;

import com.yanbenjun.task.model.TaskStartServer;

public class TaskRequest
{
    private String taskId;
    
    private String taskName;
    
    private TaskStartServer taskStartServer;
    
    private String path;
    
    private TaskParam taskParam;
    
    /**
     * 是否垮微服务调用
     */
    private boolean local;
    
    public boolean local()
    {
        return this.local;
    }

    public String getTaskId()
    {
        return taskId;
    }

    public void setTaskId(String taskId)
    {
        this.taskId = taskId;
    }

    public String getTaskName()
    {
        return taskName;
    }

    public void setTaskName(String taskName)
    {
        this.taskName = taskName;
    }

    public String getPath()
    {
        return path;
    }

    public void setPath(String path)
    {
        this.path = path;
    }

    public TaskParam getTaskParam()
    {
        return taskParam;
    }

    public void setTaskParam(TaskParam taskParam)
    {
        this.taskParam = taskParam;
    }

    public boolean isLocal()
    {
        return local;
    }

    public void setLocal(boolean local)
    {
        this.local = local;
    }

    public TaskStartServer getTaskStartServer()
    {
        return taskStartServer;
    }

    public void setTaskStartServer(TaskStartServer taskStartServer)
    {
        this.taskStartServer = taskStartServer;
    }
}
