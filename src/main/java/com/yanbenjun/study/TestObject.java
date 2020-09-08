package com.yanbenjun.study;

public class TestObject
{
    private int id;
    public TestObject(int id)
    {
        this.id = id;
    }
    @Override
    public void finalize() throws Throwable
    {
        super.finalize();
        System.out.println("I am finalize."+id);
        Object obj = new Object();
    }
    
    public void print()
    {
        System.out.println("I am print." + id);
    }
}
