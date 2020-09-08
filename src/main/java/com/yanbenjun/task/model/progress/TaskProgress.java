package com.yanbenjun.task.model.progress;

/**
 * 进度MBean
 * @author Administrator
 *
 */
public class TaskProgress
{
    public static final int FINISH = 100;
    
    public static final int INIT = 0;
    
    public static final int START = 1;
    
    private int percent;
    
    private int status;
    
    public TaskProgress()
    {
        
    }
    
    public TaskProgress(int percent, int status)
    {
        this.percent = percent;
        this.status = status;
    }
    
    public TaskProgress clone()
    {
        return new TaskProgress(this.percent, this.status);
    }

    public int getPercent()
    {
        return percent;
    }

    public void setPercent(int percent)
    {
        this.percent = percent;
    }

    public int getStatus()
    {
        return status;
    }

    public void setStatus(int status)
    {
        this.status = status;
    }
}
