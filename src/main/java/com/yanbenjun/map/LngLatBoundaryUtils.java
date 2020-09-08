package com.yanbenjun.map;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LngLatBoundaryUtils
{
    /**
     * 找到包含飞地或者挖洞边界中最大的一个单块边界
     * 
     * @param borders
     * @return
     */
    public static String findMaxBorder(String borders)
    {
        List<String> borderList = new ArrayList<>();
        String[] arr = borders.split("\\&");
        for (String s : arr)
        {
            String[] a = s.split("\\|");
            borderList.addAll(Arrays.asList(a));
        }
        try
        {
            int max = 0;
            String maxBorder = null;
            for (String border : borderList)
            {
                LngLat[] llb = LngLatBoundary.parse(border);
                if(llb.length > max) {
                    max = llb.length;
                    maxBorder = border;
                }
            }
            return maxBorder;
        }
        catch (LngLatBoundaryFormatException e)
        {
            e.printStackTrace();
        }
        return null;
    }
    
    public static void main(String[] args) throws IOException
    {
        Files.readAllLines(Paths.get("D://txt/testBoundary.txt")).stream().forEach(line->{
            System.out.println(findMaxBorder(line));
        });
    }
}
