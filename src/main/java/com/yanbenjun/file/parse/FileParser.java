package com.yanbenjun.file.parse;

import java.util.List;

import com.yanbenjun.file.config.element.BaseParseFileInfo;
import com.yanbenjun.file.config.element.xml.XmlParseFileInfo;
import com.yanbenjun.file.model.parse.ParsedRow;
import com.yanbenjun.file.parse.api.Parser;
import com.yanbenjun.file.parse.core.PostHandleChain;
import com.yanbenjun.file.parse.core.validate.FileHeadValidator;

public class FileParser implements Parser
{
    private BaseParseFileInfo baseFileInfo;

    public FileParser(BaseParseFileInfo baseFileInfo)
    {
        this.baseFileInfo = baseFileInfo;
    }

    @Override
    public void parse()
    {
        FileHeadValidator fhv = new FileHeadValidator(baseFileInfo);
        try
        {
            fhv.validate();
        }
        catch (Exception e)
        {
            System.out.println("系统错误");
            return;
        }
        PostHandleChain.getDefaultChain().run(baseFileInfo);
    }

    public List<ParsedRow> parseXml()
    {
        if (baseFileInfo instanceof XmlParseFileInfo)
        {
            PostHandleChain chain = PostHandleChain.getXmlDefaultChain();
            chain.run(baseFileInfo);
            return chain.getCacheHandler().getRowCache();
        }
        return null;
    }

}
