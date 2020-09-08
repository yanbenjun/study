package com.yanbenjun.db.util;

import org.apache.commons.lang.StringUtils;

public class SqlStringUtil
{
    public static String legalQuote(String name)
    {
        if(StringUtils.isEmpty(name))
        {
            return name;
        }
        return name.replaceAll("'", "''");
    }
    
    public static String removeIllegalSpecialChars(String name)
    {
        //TODO
        return name;
    }
    
    public static void main(String[] args)
    {
        System.out.println(legalQuote("na\'m''e"));
    }
}
