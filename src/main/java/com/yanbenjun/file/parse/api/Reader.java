package com.yanbenjun.file.parse.api;

import com.yanbenjun.file.parse.message.ParseMessage;

public interface Reader
{
    /**
     * 读取方法入口
     * @return
     * @throws Exception
     */
    public ParseMessage read() throws Exception;
}
