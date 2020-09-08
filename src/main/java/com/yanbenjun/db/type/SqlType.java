package com.yanbenjun.db.type;

import java.util.HashMap;
import java.util.Map;

public enum SqlType
{
    STRING(1,"varchar", 255),
    INTEGER(2,"int", 11),
    LONG(3,"bigint", 20),
    DOUBLE(4,"double"),
    BOOLEAN(5,"bit",1),
    TEXT(6,"text"),
    LONGTEXT(7,"longtext"),
    MEDIUMTEXT(8,"mediumtext"),
    ;
    
    private static Map<Integer,SqlType> map = new HashMap<>();
    static
    {
        for(SqlType st : SqlType.values())
        {
            map.put(st.getValue(), st);
        }
    }
    
    private int value;
    
    private String name;
    
    private int defaultLenght;
    
    private SqlType(int value, String name)
    {
        this.value = value;
        this.name= name;
    }
    
    private SqlType(int value, String name, int defaultLenght)
    {
        this.value = value;
        this.name= name;
        this.defaultLenght = defaultLenght;
    }
    
    public static SqlType valueOfType(int value)
    {
        SqlType type = map.get(value);
        if(type == null)
        {
            throw new IllegalArgumentException("Illegal sql type value: " + value);
        }
        return type;
    }
    
    public int getValue()
    {
        return this.value;
    }
    
    public String getName()
    {
        return this.name;
    }

    public int getDefaultLength()
    {
        return defaultLenght;
    }
}
