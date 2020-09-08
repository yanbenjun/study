package com.yanbenjun.task.model.response;

public class TaskResponseHeader
{
    private String taskId;
    
    private String taskName;

    private String runServer;

    private String runServerIp;
    
    private ContentType contentType;

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

    public String getRunServer()
    {
        return runServer;
    }

    public void setRunServer(String runServer)
    {
        this.runServer = runServer;
    }

    public String getRunServerIp()
    {
        return runServerIp;
    }

    public void setRunServerIp(String runServerIp)
    {
        this.runServerIp = runServerIp;
    }

    public ContentType getContentType()
    {
        return contentType;
    }

    public void setContentType(ContentType contentType)
    {
        this.contentType = contentType;
    }

    
}
