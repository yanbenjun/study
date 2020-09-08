package com.yanbenjun.db;

public class CorrelationPair
{
    private DbField leftField;
    
    private DbField rightField;

    public DbField getLeftField()
    {
        return leftField;
    }

    public void setLeftField(DbField leftField)
    {
        this.leftField = leftField;
    }

    public DbField getRightField()
    {
        return rightField;
    }

    public void setRightField(DbField rightField)
    {
        this.rightField = rightField;
    }
}
