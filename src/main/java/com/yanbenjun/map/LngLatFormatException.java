package com.yanbenjun.map;

public class LngLatFormatException extends Exception
{
    /**
     * 
     */
    private static final long serialVersionUID = -8818464349650943452L;
    
    private String errorInfo;
    
    public LngLatFormatException()
    {
        super();
        this.errorInfo = this.getMessage();
    }
    
    public LngLatFormatException(String message)
    {
        super(message);
        this.errorInfo = message;
    }

    public String getErrorInfo()
    {
        return errorInfo;
    }

    public void setErrorInfo(String errorInfo)
    {
        this.errorInfo = errorInfo;
    }

}
