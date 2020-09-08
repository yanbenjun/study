package com.yanbenjun.task.error;

public class TaskExecutionException extends RuntimeException
{

    /**
     * 
     */
    private static final long serialVersionUID = -360478855127957106L;
    
    private TaskExecutionExceptionModel exceptionModel;
    
    public TaskExecutionException(String taskId, String errorInfo)
    {
        super(errorInfo);
        this.setExceptionModel(new TaskExecutionExceptionModel(taskId,errorInfo));
    }

    public TaskExecutionExceptionModel getExceptionModel()
    {
        return exceptionModel;
    }

    public void setExceptionModel(TaskExecutionExceptionModel exceptionModel)
    {
        this.exceptionModel = exceptionModel;
    }

}
