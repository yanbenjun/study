package com.yanbenjun.db;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

public class TestConnector
{
    private static String sql = "select * from table_tuan_ju_info";
    
    public static void main(String[] args)
    {
        try
        {
            Class.forName("com.mysql.jdbc.Driver");
            
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/study?useUnicode=true&characterEncoding=utf-8", "root", "root");
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while(rs.next())
            {
                ResultSetMetaData metaData = rs.getMetaData();
                //System.out.println(metaData.getColumnCount());
                System.out.print(rs.getString(1) +"\t");
                System.out.print(rs.getString(2) +"\t");
                System.out.print(rs.getString(3) +"\t");
                System.out.print(rs.getString(4) +"\t");
                System.out.print(rs.getString(5) +"\t");
                System.out.println();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
