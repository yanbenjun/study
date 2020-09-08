package com.yanbenjun.request.collapse;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class CompletableFutureUseTest
{
    public static void main(String[] args)
    {
        CompletableFuture.supplyAsync(()->{
            return "yanbenjun";
        }).thenAcceptAsync(item->{
            try
            {
                Thread.sleep(2000);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            System.out.println(item);
        },(runnable)->{
            new Thread(runnable).start();
        });
        System.out.println("hello");
    }
}
