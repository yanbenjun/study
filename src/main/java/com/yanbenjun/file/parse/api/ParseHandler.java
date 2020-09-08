package com.yanbenjun.file.parse.api;

import com.yanbenjun.file.config.element.BaseParseFileInfo;
import com.yanbenjun.file.parse.message.ParseMessage;
import com.yanbenjun.file.parse.regist.ICanRegist;

public interface ParseHandler extends ICanRegist
{
    public ParseMessage handle(BaseParseFileInfo baseFileInfo);
}
