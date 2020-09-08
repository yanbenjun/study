package com.yanbenjun.test;

public class Test
{
    private static volatile boolean flag = true;
    public static void main(String[] args) throws InterruptedException
    {
        new Thread() {
            public void run() {
                
                int i=0;
                while(flag) {
                        i++;
                }
                System.out.println("flag 被设置为true了" + i);
            }
        }.start();
        
        Thread.sleep(2000);
        flag = false;
        System.out.println("over");
    }
}
