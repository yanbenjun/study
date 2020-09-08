package com.yanbenjun.db;

import com.yanbenjun.db.type.SqlType;

/**
 * 数据库字段
 * 
 * @author Administrator
 *
 */
public class DbField
{
    private long id;

    private String originName;

    private String name;

    private int sqlType;

    private int length;

    private boolean notNull;

    private String defaultValue;

    private boolean primaryKey;

    private boolean key;

    private String comment;

    private int orderInTable;
    
    private DbTable dbTable;
    
    private DbFieldDataSource fieldDataSource;

    /**
     * 转化为建表语句片段
     * 
     * @return
     */
    public String toCreateSqlSegment()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("`").append(originName).append("` ");
        sb.append(SqlType.valueOfType(this.sqlType).getName());
        int len = length > 0 ? length : SqlType.valueOfType(this.sqlType).getDefaultLength();
        String typeStr = len > 0 ? "(" + len + ") " : " ";
        sb.append(typeStr);
        sb.append(notNull ? "NOT NULL " : " ");
        sb.append(this.defaultValue == null ? " " : "DEFAULT '" + this.defaultValue+ "'");
        sb.append(" COMMENT '" + this.comment+ "'");
        return sb.toString();
    }

    public long getId()
    {
        return id;
    }

    public void setId(long id)
    {
        this.id = id;
    }

    public String getOriginName()
    {
        return originName;
    }

    public void setOriginName(String originName)
    {
        this.originName = originName;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public int getSqlType()
    {
        return sqlType;
    }

    public void setSqlType(int sqlType)
    {
        this.sqlType = sqlType;
    }

    public int getLength()
    {
        return length;
    }

    public void setLength(int length)
    {
        this.length = length;
    }

    public boolean isNotNull()
    {
        return notNull;
    }

    public void setNotNull(boolean notNull)
    {
        this.notNull = notNull;
    }

    public boolean isPrimaryKey()
    {
        return primaryKey;
    }

    public void setPrimaryKey(boolean primaryKey)
    {
        this.primaryKey = primaryKey;
    }

    public boolean isKey()
    {
        return key;
    }

    public void setKey(boolean key)
    {
        this.key = key;
    }

    public String getComment()
    {
        return comment;
    }

    public void setComment(String comment)
    {
        this.comment = comment;
    }

    public DbTable getDbTable()
    {
        return dbTable;
    }

    public void setDbTable(DbTable dbTable)
    {
        this.dbTable = dbTable;
    }

    public DbFieldDataSource getFieldDataSource()
    {
        return fieldDataSource;
    }

    public void setFieldDataSource(DbFieldDataSource fieldDataSource)
    {
        this.fieldDataSource = fieldDataSource;
    }

    public int getOrderInTable()
    {
        return orderInTable;
    }

    public void setOrderInTable(int orderInTable)
    {
        this.orderInTable = orderInTable;
    }

    public String getDefaultValue()
    {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue)
    {
        this.defaultValue = defaultValue;
    }
}
