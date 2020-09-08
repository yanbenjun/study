package com.yanbenjun.task.model.progress;

/**
 * 任务进度
 * @author Administrator
 *
 */
public class TaskExecuteProgress
{
    public static final int VALID_MIN_VALUE = 0;
    public static final int START_VALUE = 1;
    public static final int FINISH_VALUE = 100;
    
    private int value;

    public int getValue()
    {
        return value;
    }

    public void setValue(int value)
    {
        this.value = value;
    }
    
}
