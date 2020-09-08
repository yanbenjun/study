package com.yanbenjun.task.test;

public class Test2
{
    public static void main(String[] args)
    {
        int a = Integer.MAX_VALUE;
        int b = Integer.MIN_VALUE;
        System.out.println(a-b);
        System.out.println(1234<(a-b));
        try
        {
            System.out.println(0);
            throw new RuntimeException("error");
        }
        catch (Exception e)
        {
            System.out.println(2);
        }
        finally
        {
            System.out.println(1);
        }
        System.out.println(3);
    }
}   
