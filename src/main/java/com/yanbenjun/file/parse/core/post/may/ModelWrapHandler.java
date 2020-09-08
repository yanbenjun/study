package com.yanbenjun.file.parse.core.post.may;

import com.yanbenjun.file.config.element.ToParseFile;
import com.yanbenjun.file.model.RowHandleException;
import com.yanbenjun.file.model.parse.ParsedRow;
import com.yanbenjun.file.parse.core.post.AbstractMidPostRowHandler;
import com.yanbenjun.file.parse.core.post.infs.PostRowHandler;
import com.yanbenjun.file.parse.message.ParseMessage;

public class ModelWrapHandler extends AbstractMidPostRowHandler
{
    public ModelWrapHandler(ToParseFile toParseFile)
    {
    }

    public ModelWrapHandler(ToParseFile toParseFile, PostRowHandler next)
    {
        super(next);
    }

    @Override
    public void processOne(ParsedRow parsedRow, ParseMessage parseMessage) throws RowHandleException
    {
        // TODO Auto-generated method stub
        next.processOne(parsedRow, parseMessage);
    }

}
