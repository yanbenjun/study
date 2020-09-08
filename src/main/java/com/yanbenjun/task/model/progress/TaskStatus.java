package com.yanbenjun.task.model.progress;

import java.util.HashMap;
import java.util.Map;

public enum TaskStatus
{
    INIT(0),
    RUNNING(100),
    SUCCESS(200),
    FAIL(500),
    FINISH(201);
    
    private static Map<Integer, TaskStatus> cache = new HashMap<>();
    
    static
    {
        for(TaskStatus status : TaskStatus.values())
        {
            cache.put(status.getValue(), status);
        }
    }

    private int value;
    
    private TaskStatus(int value)
    {
        this.value = value;
    }
    
    public static TaskStatus enumOf(int value)
    {
        return cache.get(value);
    }

    public int getValue()
    {
        return this.value;
    }
}
