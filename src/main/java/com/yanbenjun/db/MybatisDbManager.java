package com.yanbenjun.db;

import java.io.IOException;
import java.io.Reader;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

public class MybatisDbManager
{
    private static SqlSessionFactory factory;

    static
    {
        String resource = "mybatis-config.xml";
        Reader reader;
        try
        {
            reader = Resources.getResourceAsReader(resource);
            factory = (new SqlSessionFactoryBuilder().build(reader));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    private static SqlSession sqlSession;

    public static SqlSession getSqlSession()
    {
        sqlSession = factory.openSession();
        return sqlSession;
    }

    public static <T> T getDao(Class<T> cls)
    {
        return getSqlSession().getMapper(cls);
    }

    public static void destroyConnection()
    {
        sqlSession.close();
    }

    public static SqlSessionFactory getFactory()
    {
        return factory;
    }

}
