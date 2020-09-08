package com.yanbenjun.file.parse.core.excel;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.model.SharedStringsTable;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import com.yanbenjun.file.config.element.BaseParseFileInfo;
import com.yanbenjun.file.config.element.ToParseFile;
import com.yanbenjun.file.config.element.ToParseTemplate;
import com.yanbenjun.file.model.RowHandleException;
import com.yanbenjun.file.parse.core.AbstractReader;
import com.yanbenjun.file.parse.core.post.infs.PostRowHandler;
import com.yanbenjun.file.parse.core.validate.HeadParseException;
import com.yanbenjun.file.parse.message.HeadParseMessage;
import com.yanbenjun.file.parse.message.ParseMessage;

public class XlsxReader extends AbstractReader
{
    private boolean sortedRead;
    private BaseParseFileInfo baseFileInfo;

    public XlsxReader(BaseParseFileInfo baseFileInfo, PostRowHandler startPostRowHandler)
    {
        this.baseFileInfo = baseFileInfo;
        this.startPostRowHandler = startPostRowHandler;
    }

    public ParseMessage read() throws Exception
    {
        if (!sortedRead)
        {
            return parseWithIndex();
        }
        return parseWithPriority();
    }

    /**
     * 按照toParseFile中配置的sheet解析优先级（toParseTemplate中的priority字段）来解析sheet
     * 
     * @return
     * @throws Exception
     */
    private ParseMessage parseWithPriority() throws Exception
    {
        String fileName = baseFileInfo.getPath();
        ToParseFile toParseFile = baseFileInfo.getToParseFile();
        OPCPackage pkg = OPCPackage.open(fileName);
        XSSFReader r = new XSSFReader(pkg);
        SharedStringsTable sst = r.getSharedStringsTable();

        XMLReader parser = XMLReaderFactory.createXMLReader("com.sun.org.apache.xerces.internal.parsers.SAXParser");
        XlsxSheetReadHandler handler = new XlsxSheetReadHandler(toParseFile, startPostRowHandler, sst);
        parser.setContentHandler(handler);

        HeadParseMessage hpMsg = new HeadParseMessage();
        try
        {
            for (ToParseTemplate template : toParseFile.getSortedTemplates())
            {
                int sheetIndex = template.getSheetIndex();
                InputStream sheet = r.getSheet("rId" + (sheetIndex + 1));
                handler.setSheetIndex(sheetIndex);
                if (!toParseFile.needParse(sheetIndex))
                {
                    continue;
                }
                parseSheet(parser, sheet, hpMsg);
            }
        }
        catch (IllegalArgumentException e)
        {
            System.out.println("配置的模板与实际上传的模板不匹配，请检查！");
        }
        return hpMsg;
    }

    /**
     * 未使用，依次从第一个sheet也读取数据
     * 
     * @return
     * @throws Exception
     */
    private ParseMessage parseWithIndex() throws Exception
    {
        String fileName = baseFileInfo.getPath();
        ToParseFile toParseFile = baseFileInfo.getToParseFile();
        OPCPackage pkg = OPCPackage.open(fileName);
        XSSFReader r = new XSSFReader(pkg);
        SharedStringsTable sst = r.getSharedStringsTable();

        XMLReader parser = XMLReaderFactory.createXMLReader("com.sun.org.apache.xerces.internal.parsers.SAXParser");
        XlsxSheetReadHandler handler = new XlsxSheetReadHandler(toParseFile, startPostRowHandler, sst);
        parser.setContentHandler(handler);

        Iterator<InputStream> sheets = r.getSheetsData();
        int i = 0;
        HeadParseMessage hpMsg = new HeadParseMessage();
        while (sheets.hasNext())
        {
            handler.setSheetIndex(i);
            InputStream sheet = sheets.next();
            if (!toParseFile.needParse(i++))
            {
                continue;
            }
            parseSheet(parser, sheet, hpMsg);
        }
        return hpMsg;
    }

    private void parseSheet(XMLReader parser, InputStream sheet, HeadParseMessage hpMsg) throws IOException
    {
        InputSource sheetSource = new InputSource(sheet);
        try
        {
            parser.parse(sheetSource);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        catch (HeadParseException e)
        {
            hpMsg.addAll(e.getHeadParseMsg().getErrorMsgs());
            System.out.println("Find Head Error:" + e.getHeadParseMsg().getErrorMsgs());
        }
        catch (RowHandleException e)
        {
            System.out.println("表头行验证完毕，开始读取内容行");
        }
        catch (SAXException e)
        {
        }
        finally
        {
            sheet.close();
        }
    }

}
