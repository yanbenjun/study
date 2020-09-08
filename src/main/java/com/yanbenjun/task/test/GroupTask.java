package com.yanbenjun.task.test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.yanbenjun.task.model.TaskStartServer;
import com.yanbenjun.task.model.progress.SystemTaskProgressManager;
import com.yanbenjun.task.model.request.TaskRequest;

public class GroupTask
{
    private static List<TestTask> tts = new ArrayList<>();
    
    public static void main(String[] args)
    {
        TaskRequest tr = new TaskRequest();
        tr.setTaskId("1");
        tr.setTaskName("test");
        tr.setTaskStartServer(TaskStartServer.local());
        TestTask tt1 = new TestTask(tr, SystemTaskProgressManager.instance());
        tts.add(tt1);
        for(TestTask tt : tts)
        {
            tt.execute();
            try
            {
                tt.waitFinish(3600, TimeUnit.SECONDS);
            }
            catch (TimeoutException e)
            {
                e.printStackTrace();
            }
        }
        
    }
}
