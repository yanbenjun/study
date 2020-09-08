package com.yanbenjun.db.type;

public enum TableType
{
    DATA(0),
    CORRELATION(1);
    
    private int type;
    
    private TableType(int type)
    {
        this.type = type;
    }

    public int getType()
    {
        return type;
    }
}
