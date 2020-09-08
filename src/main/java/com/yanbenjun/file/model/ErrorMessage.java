package com.yanbenjun.file.model;

public class ErrorMessage extends Location
{
    private String error;
    
    public ErrorMessage(String error)
    {
        this(error,-1,-1);
    }
    
    public ErrorMessage(String error,int rowId, int columnId)
    {
        super(rowId, columnId);
        this.error = error;
    }

    public String getError()
    {
        return error;
    }

    public void setError(String error)
    {
        this.error = error;
    }
    
    public String toString()
    {
        return error;
    }
}
