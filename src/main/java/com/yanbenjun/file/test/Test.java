package com.yanbenjun.file.test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import com.yanbenjun.file.config.ParseSystemLoader;
import com.yanbenjun.file.config.element.BaseParseFileInfo;
import com.yanbenjun.file.config.element.ToParseFile;
import com.yanbenjun.file.parse.FileParser;

public class Test
{
    public static void test()
    {
        List<Object> objs = new ArrayList<Object>();
        objs.add(1);
        objs.add(2);
        Object[] os = objs.toArray();
        Stream<Object> s = Stream.of(os).filter(v -> v != null);
        Double d = s.count() == 0 ? null : s.mapToDouble(v -> ((Double)v)).sum();
        System.out.println(s.findFirst());
        System.out.println(s.mapToDouble(v -> ((Double)v)));
        System.out.println(d);
    
    }
    
    public static void main(String[] args) throws Exception
    {
        ParseSystemLoader.load("test");
        ToParseFile toParseFile = ParseSystemLoader.get("test");
        BaseParseFileInfo baseFileInfo = new BaseParseFileInfo();
        baseFileInfo.setPath("F:\\test.xlsx");
        baseFileInfo.setToParseFile(toParseFile);
        new FileParser(baseFileInfo).parse();
        
        //TODO
        //1。错误信息系统需要改进，属性校验系统融入（自定义校验等），表头校验系统融入
        //2。依次完善xls，txt，xml解析
        //3。删除无用代码，添加注释，注意代码规范，多使用JAVA8新特性
    }
}
