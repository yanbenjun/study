package com.yanbenjun.file.test;

import com.yanbenjun.file.model.RowHandleException;
import com.yanbenjun.file.model.parse.ParsedRow;
import com.yanbenjun.file.parse.core.post.infs.TeminationPostRowHandler;
import com.yanbenjun.file.parse.message.ParseMessage;

public class MyRowHandler implements TeminationPostRowHandler
{

    @Override
    public void processOne(ParsedRow parsedRow, ParseMessage parseMessage) throws RowHandleException
    {
        // TODO Auto-generated method stub
        
    }

}
