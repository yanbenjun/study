package com.yanbenjun.file.parse.core;

import com.yanbenjun.file.config.element.BaseParseFileInfo;
import com.yanbenjun.file.config.element.xml.XmlParseFileInfo;
import com.yanbenjun.file.parse.api.Reader;
import com.yanbenjun.file.parse.core.excel.XlsxReader;
import com.yanbenjun.file.parse.core.post.infs.PostRowHandler;
import com.yanbenjun.file.parse.core.xml.XmlReader;

public class ReaderFactory
{
    public static Reader getReader(BaseParseFileInfo baseFileInfo, PostRowHandler startPostRowHandler)
    {
        if(baseFileInfo.getPath().endsWith(".xlsx"))
        {
            return new XlsxReader(baseFileInfo, startPostRowHandler);
        }
        else if(baseFileInfo.getPath().endsWith(".xml"))
        {
            if(baseFileInfo instanceof XmlParseFileInfo)
            {
                return new XmlReader((XmlParseFileInfo)baseFileInfo, startPostRowHandler);
            }
            else
            {
                throw new RuntimeException("xml解析需要传入XmlParseFileInfo类型的文件");
            }
        }
        return null;
    }
}
