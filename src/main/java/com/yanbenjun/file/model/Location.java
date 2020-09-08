package com.yanbenjun.file.model;

public class Location
{
    private int fileId;
    
    private int sheetId;
    
    private int rowId;
    
    private int columnId;
    
    public Location()
    {
        
    }
    
    public Location(int rowId, int columnId)
    {
        this.rowId = rowId;
        this.columnId = columnId;
    }
    
    public int getFileId()
    {
        return fileId;
    }

    public void setFileId(int fileId)
    {
        this.fileId = fileId;
    }

    public int getSheetId()
    {
        return sheetId;
    }

    public void setSheetId(int sheetId)
    {
        this.sheetId = sheetId;
    }

    public int getRowId()
    {
        return rowId;
    }

    public void setRowId(int rowId)
    {
        this.rowId = rowId;
    }

    public int getColumnId()
    {
        return columnId;
    }

    public void setColumnId(int columnId)
    {
        this.columnId = columnId;
    }
}
