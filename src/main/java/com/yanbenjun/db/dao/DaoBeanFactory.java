package com.yanbenjun.db.dao;

import java.lang.reflect.Proxy;

public class DaoBeanFactory
{
    public static <T> T getBean(Class<T> cls)
    {
        DaoBeanAware<T> dba = new DaoBeanAware<T>(cls);
        Class<?>[] ss = new Class<?>[1];
        ss[0] = cls;
        @SuppressWarnings("unchecked")
        T result = (T) Proxy.newProxyInstance(cls.getClassLoader(), ss, dba);
        return result;
    }
    
}
