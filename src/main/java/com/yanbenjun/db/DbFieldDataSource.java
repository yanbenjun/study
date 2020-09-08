package com.yanbenjun.db;

import java.util.List;

/**
 * 通过外键字段从其关联的表获取关联表的具体数据
 * 多对一关系
 * 
 * 使用场景：当从一个原始表复制数据到另一个表，另一个表需要额外的字段来自于原始表关联的另一个表里
 * @author Administrator
 *
 */
public class DbFieldDataSource
{
    private long id;
    /**
     * 数据来源字段
     */
    private DbField sourceField;
    
    /**
     * 关联主表
     */
    private DbTable primaryTable;

    /**
     * 关联list
     */
    private List<CorrelationPair> joinList;

    public long getId()
    {
        return id;
    }

    public void setId(long id)
    {
        this.id = id;
    }

    public DbField getSourceField()
    {
        return sourceField;
    }

    public void setSourceField(DbField sourceField)
    {
        this.sourceField = sourceField;
    }

    public DbTable getPrimaryTable()
    {
        return primaryTable;
    }

    public void setPrimaryTable(DbTable primaryTable)
    {
        this.primaryTable = primaryTable;
    }

    public List<CorrelationPair> getJoinList()
    {
        return joinList;
    }

    public void setJoinList(List<CorrelationPair> joinList)
    {
        this.joinList = joinList;
    }
}
