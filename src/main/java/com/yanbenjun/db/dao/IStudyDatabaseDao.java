package com.yanbenjun.db.dao;

import org.apache.ibatis.annotations.Param;

public interface IStudyDatabaseDao
{
    public void executeSql(@Param("sql") String sql);
}
