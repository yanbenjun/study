package com.yanbenjun.task.model.progress;

public class TaskProgressOperationException extends RuntimeException
{
    /**
     * 
     */
    private static final long serialVersionUID = -2544471687830642236L;
    
    public TaskProgressOperationException()
    {
        
    }
    
    public TaskProgressOperationException(String message)
    {
        super(message);
    }
    
    public TaskProgressOperationException(String message, Throwable cause) {
        super(message, cause);
    }

    public TaskProgressOperationException(Throwable cause) {
        super(cause);
    }

}
