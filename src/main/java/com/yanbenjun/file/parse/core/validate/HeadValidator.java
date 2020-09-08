package com.yanbenjun.file.parse.core.validate;

import com.yanbenjun.file.model.RowHandleException;
import com.yanbenjun.file.model.parse.ParsedRow;
import com.yanbenjun.file.parse.api.Validator;
import com.yanbenjun.file.parse.message.HeadParseMessage;

public interface HeadValidator extends Validator
{
    public HeadParseMessage validate() throws Exception;
    
    public void validate(ParsedRow dataRow) throws RowHandleException;
}
