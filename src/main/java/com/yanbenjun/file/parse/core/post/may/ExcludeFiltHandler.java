package com.yanbenjun.file.parse.core.post.may;

import com.yanbenjun.file.config.element.ToParseFile;
import com.yanbenjun.file.model.RowHandleException;
import com.yanbenjun.file.model.parse.ParsedRow;
import com.yanbenjun.file.parse.core.post.AbstractMidPostRowHandler;
import com.yanbenjun.file.parse.core.post.infs.PostRowHandler;
import com.yanbenjun.file.parse.message.ParseMessage;

public class ExcludeFiltHandler extends AbstractMidPostRowHandler
{
    public ExcludeFiltHandler()
    {
        super();
    }

    public ExcludeFiltHandler(ToParseFile toParseFile, PostRowHandler next)
    {
        super(next);
    }

    @Override
    public void processOne(ParsedRow parsedRow, ParseMessage parseMessage) throws RowHandleException
    {
        if(parsedRow.isEmpty())
        {
            return;
        }
        next.processOne(parsedRow, parseMessage);
        //TODO 具体的业务可以通过继承该类，复写该方法实现
    }

}
