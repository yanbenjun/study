package com.yanbenjun.file.parse.core.validate;

import com.yanbenjun.file.model.RowHandleException;
import com.yanbenjun.file.parse.message.HeadParseMessage;

public class HeadParseException extends RowHandleException
{

    /**
     * 
     */
    private static final long serialVersionUID = -6736553105202281767L;
    
    private HeadParseMessage headParseMsg;
    
    public HeadParseException(HeadParseMessage hpMsg)
    {
        this.headParseMsg = hpMsg;
    }

    public HeadParseMessage getHeadParseMsg()
    {
        return headParseMsg;
    }

    public void setHeadParseMsg(HeadParseMessage headParseMsg)
    {
        this.headParseMsg = headParseMsg;
    }

}
