package com.yanbenjun.file.service;

import java.util.List;

import com.yanbenjun.file.config.element.BaseParseFileInfo;
import com.yanbenjun.file.config.element.ParseSystem;
import com.yanbenjun.file.config.element.ToParseFile;
import com.yanbenjun.file.config.element.xml.XmlParseFileInfo;
import com.yanbenjun.file.model.parse.ParsedRow;
import com.yanbenjun.file.parse.FileParser;

public class ParseService
{
    public void singleFileParse(String filePath, Long parsePointId)
    {
        ParseSystem.singleton().load("", "fileParse-context.xml");
        ToParseFile tpf = ParseSystem.singleton().getParsePoint(parsePointId).getToParseFileList().get(0);
        BaseParseFileInfo baseFileInfo = new BaseParseFileInfo();
        baseFileInfo.setPath(filePath);
        baseFileInfo.setToParseFile(tpf);
        FileParser fp = new FileParser(baseFileInfo);
        fp.parse();
    }

    public void xmlParse(String filePath, Long parsePointId)
    {
        ParseSystem.singleton().load("", "fileParse-context.xml");
        ToParseFile tpf = ParseSystem.singleton().getParsePoint(parsePointId).getToParseFileList().get(0);
        XmlParseFileInfo baseFileInfo = new XmlParseFileInfo();
        baseFileInfo.setPath(filePath);
        baseFileInfo.setToParseFile(tpf);
        baseFileInfo.setRowTag("book");
        FileParser fp = new FileParser(baseFileInfo);
        List<ParsedRow> result = fp.parseXml();
        System.out.println(result);
    }

    public static void main(String[] args)
    {
        new ParseService().singleFileParse("F:\\test.xlsx", 20180715L);
       // new ParseService().xmlParse("F:\\bookinfo1.xml", 20180916L);
    }
}
