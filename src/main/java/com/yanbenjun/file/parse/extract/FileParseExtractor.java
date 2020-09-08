package com.yanbenjun.file.parse.extract;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import com.yanbenjun.file.config.element.ColumnHead;
import com.yanbenjun.file.config.element.ToParseTemplate;
import com.yanbenjun.file.model.RowHandleException;
import com.yanbenjun.file.model.parse.ColumnEntry;
import com.yanbenjun.file.model.parse.ParsedRow;
import com.yanbenjun.file.parse.core.AbstractFileParseHandler;
import com.yanbenjun.file.parse.core.post.infs.PostRowHandler;
import com.yanbenjun.file.parse.message.HeadParseMessage;
import com.yanbenjun.file.parse.message.ParseMessage;

public class FileParseExtractor extends AbstractFileParseHandler implements PostRowHandler
{
    protected Map<Integer,String> parsedHeadMap = new HashMap<Integer, String>();
    private PostRowHandler postHandler;
    public FileParseExtractor(PostRowHandler postHandler)
    {
        this.postHandler = postHandler;
    }

    /**
     * 后续可以引入对接参数，如某个Handler的对接上面是1，下路对接是3，则处理链上面只能是1结束的，处理链下端的只能是3开始的。
     */
    @Override
    public void processOne(ParsedRow parsedRow, ParseMessage parseMessage) throws RowHandleException
    {
        ToParseTemplate toParseTemplate = parsedRow.getCurTemplate();
        //为了兼容xml提取，xml提取rowIndex = -1，不会反在此返回
        if(parsedRow.getRowIndex() >= 0 && parsedRow.getRowIndex() < toParseTemplate.getHeadRow())
        {
            return;
        }
        //为了兼容xml提取,xml提取rowIndex = -1，xml提取不进行表头提取，直接进行内容读取
        if(toParseTemplate.getHeadRow() == parsedRow.getRowIndex())
        {
            readHead(parsedRow);
            return;
        }
        Iterator<ColumnEntry> iter = parsedRow.getCells().iterator();
        while(iter.hasNext())
        {
            ColumnEntry contentEntry = iter.next();
            Integer columnIndex = contentEntry.getKey();
            if(columnIndex == null)//xml提取没有列序号-列名称关系，直接跳过，避免在下一步数据被删除
            {
                continue;
            }
            if(parsedHeadMap.get(columnIndex) == null)
            {
                iter.remove();
            }
            else
            {
                contentEntry.setTitle(parsedHeadMap.get(columnIndex));
            }
        }
        /*if(parsedRow.isEmpty())
        {
            return;
        }*/
        postHandler.processOne(parsedRow, parseMessage);
    }
    
    /**
     * 表头提取
     * @param parsedRow
     * @return
     */
    protected HeadParseMessage readHead(ParsedRow parsedRow)
    {
        parsedHeadMap.clear();
        List<ColumnEntry> titles = parsedRow.getCells();
        if(titles.isEmpty())
        {
            System.out.println("没有可以解析的列，请检查表头或者更换模板类型为不识别表头解析。");
            return null;
        }
        ToParseTemplate toParseTemplate = parsedRow.getCurTemplate();
        List<String> templateHeads = toParseTemplate.getToParseHead().getColumnHeads().stream()
                .map(ColumnHead::getTitleName)
                .collect(Collectors.toList());
        
        for(Entry<Integer, String> titleEntry : parsedRow.getCells())
        {
            if(templateHeads.contains(titleEntry.getValue()))
            {
                parsedHeadMap.put(titleEntry.getKey(), toParseTemplate.getFieldName(titleEntry.getValue()));
            }
        }
        if(parsedHeadMap.isEmpty())
        {
            System.out.println("Sheet:" + parsedRow.getSheetIndex());
            System.out.println("模板与Excel表头不匹配，没有满足条件的列可以解析");
            return null;
        }
        return null;
    }

    @Override
    public PostRowHandler next(PostRowHandler next)
    {
        this.postHandler = next;
        return this.postHandler;
    }

    @Override
    public PostRowHandler next()
    {
        return this.postHandler;
    }
}
