package com.yanbenjun.file.parse.core.excel;

import java.util.ArrayList;
import java.util.List;

import org.apache.poi.xssf.model.SharedStringsTable;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.yanbenjun.file.config.element.ToParseFile;
import com.yanbenjun.file.model.RowHandleException;
import com.yanbenjun.file.model.parse.ColumnEntry;
import com.yanbenjun.file.model.parse.ParsedRow;
import com.yanbenjun.file.parse.core.post.infs.PostRowHandler;
import com.yanbenjun.file.parse.message.RowParseMessage;

public class XlsxSheetReadHandler extends DefaultHandler
{
    private ToParseFile toParseFile;
    /**
     * 从文件里提取出内容后的第一个处理器
     */
    private PostRowHandler startPostRowHandler;
    protected SharedStringsTable sst;
    private String lastContents;
    private boolean nextIsString;

    protected String r;
    protected boolean newLine;
    protected int sheetIndex;
    protected int row;
    protected int column;

    private List<ColumnEntry> dataRow = new ArrayList<ColumnEntry>();

    XlsxSheetReadHandler(ToParseFile toParseFile, PostRowHandler startPostRowHandler, SharedStringsTable sst)
    {
        this.toParseFile = toParseFile;
        this.startPostRowHandler = startPostRowHandler;
        this.sst = sst;
    }

    public void startElement(String uri, String localName, String name, Attributes attributes) throws SAXException
    {
        // c => cell
        if (name.equals("c"))
        {
            r = attributes.getValue("r");
            setPosition(r);
            // Figure out if the value is an index in the SST
            String cellType = attributes.getValue("t");
            if (cellType != null && cellType.equals("s"))
            {
                nextIsString = true;
            }
            else
            {
                nextIsString = false;
            }
        }
        // Clear contents cache
        lastContents = "";
    }

    public void endElement(String uri, String localName, String name) throws SAXException
    {
        // Process the last contents as required.
        // Do now, as characters() may be called more than once
        if (nextIsString)
        {
            int idx = Integer.parseInt(lastContents);
            lastContents = new XSSFRichTextString(sst.getEntryAt(idx)).toString();
            nextIsString = false;
        }

        // v => contents of a cell
        // Output after we've seen the string contents
        if (name.equals("v"))
        {
            if (newLine)
            {
                compute(dataRow, false);
                dataRow.clear();
            }
            dataRow.add(new ColumnEntry(this.column, lastContents));
        }
    }

    public void endDocument() throws SAXException
    {
        compute(dataRow, true);
        dataRow.clear();
    }

    public void characters(char[] ch, int start, int length) throws SAXException
    {
        lastContents += new String(ch, start, length);
    }

    private void compute(List<ColumnEntry> dataRow, boolean lastRow) throws RowHandleException
    {
        ParsedRow prow = new ParsedRow(toParseFile.getTemplateWith(sheetIndex), this.row - 1);
        prow.setRowIndex(this.row - 1);
        prow.setCells(dataRow);
        prow.setLastRow(lastRow);
        startPostRowHandler.processOne(prow, new RowParseMessage(this.row - 1));
    }

    private void setPosition(String r)
    {
        int curRow = Integer.parseInt(r.replaceAll("[A-Z]+", "")) - 1;
        int curCol = convertChars2Num(r.replaceAll("\\d+", "")) - 1;
        if (curRow > this.row)
        {
            this.newLine = true;
        }
        else
        {
            this.newLine = false;
        }
        this.row = curRow;
        this.column = curCol;
    }

    private static int convertChars2Num(String characters)
    {
        int firstNum = 'A';
        int total = 0;
        char[] chars = characters.toCharArray();
        int length = chars.length;
        for (int i = 0; i < length; i++)
        {
            char c = chars[length - i - 1];
            total += (c - firstNum + 1) * (int) Math.pow(26, i);
        }
        return total;
    }

    public int getSheetIndex()
    {
        return sheetIndex;
    }

    public void setSheetIndex(int sheetIndex)
    {
        this.sheetIndex = sheetIndex;
    }
}
