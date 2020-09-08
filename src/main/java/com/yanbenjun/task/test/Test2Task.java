package com.yanbenjun.task.test;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.yanbenjun.task.model.request.TaskRequest;

public class Test2Task implements Callable<String>
{
    private String taskId;

    public Test2Task(String taskId)
    {
        this.setTaskId(taskId);
    }

    public static void main(String[] args)
    {
        TaskRequest tr = new TaskRequest();
        tr.setTaskId("22");
        Test2Task tt = new Test2Task("22");
        FutureTask<String> ft = new FutureTask<>(tt);
        Thread th = new Thread(ft);
        th.start();

        Thread t2 = new Thread()
        {
            public void run()
            {
                int i = 0;
                long startTime = System.nanoTime();
                while (true)
                {
                    try
                    {
                        long endTime = System.nanoTime();
                        long leftTime = 2- TimeUnit.NANOSECONDS.toSeconds(endTime - startTime);
                        System.out.println("剩余等待时间：" + leftTime +"s");
                        if(leftTime <= 0)
                        {
                            //throw new TimeoutException();
                        }
                        String s = ft.get(0, TimeUnit.SECONDS);
                        System.out.println(s);
                        return;
                    }
                    catch (InterruptedException e)
                    {
                        System.out.println("我被打断了" + (i++));
                    }
                    catch (ExecutionException e)
                    {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    catch (TimeoutException e)
                    {
                        System.out.println("超时");
                        e.printStackTrace();
                        break;
                    }
                }
            }
        };
        t2.start();
        
        while(true)
        {
            t2.interrupt();
            try
            {
                TimeUnit.SECONDS.sleep(1);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
    }

    @Override
    public String call() throws Exception
    {
        System.out.println("Start task: " + this.taskId);
        System.out.println("Task: " + this.taskId + " is running...... ");
        TimeUnit.SECONDS.sleep(10);
        if (this.taskId.equals("1") || this.taskId.equals("2"))
        {
            throw new RuntimeException("这是我抛出的异常" + this.taskId);
        }
        System.out.println("End task: " + this.taskId);
        return "s";
    }

    public String getTaskId()
    {
        return taskId;
    }

    public void setTaskId(String taskId)
    {
        this.taskId = taskId;
    }

}
