package com.yanbenjun.study;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class DynamicProxyTest
{
    
    public static void main(String[] args)
    {
        MyInvocationHandler mih = new MyInvocationHandler(new TargetObject());
        IPrint proxy = (IPrint) Proxy.newProxyInstance(TestObject.class.getClassLoader(), new Class[]{IPrint.class}, mih);
        proxy.print();
    }
    
    private static class MyInvocationHandler implements InvocationHandler
    {
        private Object obj;
        public MyInvocationHandler(Object obj)
        {
            this.obj = obj;
        }
        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable
        {
            System.out.println("proxy start");
            method.invoke(obj, args);
            return null;
        }
        
    }
}
