package com.yanbenjun.file.parse.core.xml;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import com.yanbenjun.file.config.element.xml.XmlParseFileInfo;
import com.yanbenjun.file.parse.core.AbstractReader;
import com.yanbenjun.file.parse.core.post.infs.PostRowHandler;
import com.yanbenjun.file.parse.message.HeadParseMessage;
import com.yanbenjun.file.parse.message.ParseMessage;

public class XmlReader extends AbstractReader
{
    private  XmlParseFileInfo xmlFileInfo;
    public XmlReader(XmlParseFileInfo xmlFileInfo, PostRowHandler startPostRowHandler)
    {
        this.xmlFileInfo = xmlFileInfo;
        this.startPostRowHandler = startPostRowHandler;
    }

    @Override
    public ParseMessage read() throws Exception
    {
        SAXParserFactory sParserFactory = SAXParserFactory.newInstance();
        SAXParser parser = sParserFactory.newSAXParser();
        String fileName = xmlFileInfo.getPath();
        String tagName = xmlFileInfo.getRowTag();
        XmlHandler handler = new XmlHandler(tagName, xmlFileInfo.getToParseFile(), startPostRowHandler);
        HeadParseMessage hpMsg = new HeadParseMessage();
        try
        {
            parser.parse(fileName, handler);
        }
        catch (IllegalArgumentException e)
        {
            System.out.println("配置的模板与实际上传的模板不匹配，请检查！");
        }
        return hpMsg;
    }

}
