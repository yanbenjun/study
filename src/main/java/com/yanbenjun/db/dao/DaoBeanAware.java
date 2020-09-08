package com.yanbenjun.db.dao;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import org.apache.ibatis.session.SqlSession;

import com.yanbenjun.db.MybatisDbManager;

public class DaoBeanAware<T> implements InvocationHandler
{
    private T daoBean;
    
    private SqlSession session;
    
    public DaoBeanAware(Class<T> cls)
    {
        this.session = MybatisDbManager.getSqlSession();
        daoBean = this.session.getMapper(cls);
    }
    
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable
    {
        Object result = method.invoke(daoBean, args);
        //session.commit();
        //System.out.println(method.getName());
        return result;
    }

    public T getDaoBean()
    {
        return daoBean;
    }

    public void setDaoBean(T daoBean)
    {
        this.daoBean = daoBean;
    }

    public SqlSession getSession()
    {
        return session;
    }

}
