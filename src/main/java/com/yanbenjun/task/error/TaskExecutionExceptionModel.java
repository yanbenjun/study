package com.yanbenjun.task.error;

/**
 * 
 * @author Administrator
 *
 */
public class TaskExecutionExceptionModel
{
    private String taskId;
    
    private String errorInfo;
    
    private Throwable exception;
    
    /**
     * 构造方法
     */
    public TaskExecutionExceptionModel(String taskId)
    {
        this.setTaskId(taskId);
    }
    
    /**
     * 构造方法
     */
    public TaskExecutionExceptionModel(String taskId, String errorInfo)
    {
        this.setTaskId(taskId);
        this.setErrorInfo(errorInfo);
    }
    
    /**
     * 构造方法
     */
    public TaskExecutionExceptionModel(String taskId, Throwable e)
    {
        this.setTaskId(taskId);
        this.setException(e);
    }
    
    /**
     * 构造方法
     */
    public TaskExecutionExceptionModel(String taskId, String errorInfo, Throwable e)
    {
        this.setTaskId(taskId);
        this.setErrorInfo(errorInfo);
        this.setException(e);
    }

    public String getTaskId()
    {
        return taskId;
    }

    public void setTaskId(String taskId)
    {
        this.taskId = taskId;
    }

    public String getErrorInfo()
    {
        return errorInfo;
    }

    public void setErrorInfo(String errorInfo)
    {
        this.errorInfo = errorInfo;
    }

    public Throwable getException()
    {
        return exception;
    }

    public void setException(Throwable exception)
    {
        this.exception = exception;
    }

}
