package com.yanbenjun.file.parse.regist.type;

public class BooleanConvertor implements TypeConvertor<Boolean>
{
    public static final String REGIST_KEY = "boolean";
    @Override
    public String getRegistKey()
    {
        return REGIST_KEY;
    }

    @Override
    public Boolean convert(String value) throws TypeHandleException
    {
        return "true".equals(value) || "是".equals(value) || "1".equals(value);
    }

}
