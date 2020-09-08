package com.yanbenjun.db.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.yanbenjun.db.DbTable;

public interface IDbTableDao
{
	public void executeSql(@Param("sql") String sql);
	
    public void addAll(List<DbTable> dbTables);
    
    public DbTable queryTableInfoById(long tableId);
}
