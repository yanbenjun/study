package com.yanbenjun.file.config;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.yanbenjun.file.config.element.ColumnHead;
import com.yanbenjun.file.config.element.ToParseFile;
import com.yanbenjun.file.config.element.ToParseTemplate;

public class XmlConfigHandler extends DefaultHandler
{
    private Map<String, ToParseFile> toParseFileMap = new HashMap<String, ToParseFile>();

    private String systemName;
    private boolean isSysElement;

    private ToParseFile curToParseFile;
    private ToParseTemplate curToParseTemplate;

    public XmlConfigHandler(String systemName)
    {
        this.systemName = systemName;
    }

    @Override
    public void startElement(String uri, String localName, String name, Attributes attributes) throws SAXException
    {
        // c => cell
        if (name.equals("parseSystem"))
        {
            String curSysName = attributes.getValue("name");
            if (this.systemName.equals(curSysName))
            {
                isSysElement = true;
            }
        }
        if (isSysElement && name.equals("toParseFile"))
        {
            String curFileName = attributes.getValue("name");
            curToParseFile = new ToParseFile();
            curToParseFile.setName(curFileName);
            toParseFileMap.put(curFileName, curToParseFile);
        }
        if (isSysElement && name.equals("toParseTemplate"))
        {
            String curIndex = attributes.getValue("index");
            String curPriority = attributes.getValue("priority");
            curToParseTemplate = new ToParseTemplate(Integer.parseInt(curIndex));
            int priority = StringUtils.isEmpty(curPriority) ? 0 : Integer.parseInt(curPriority);
            curToParseTemplate.setPriority(priority);
            curToParseFile.add(curToParseTemplate);
        }
        if (isSysElement && name.equals("columnHead"))
        {
            String curTitleName = attributes.getValue("titleName");
            String curFieldName = attributes.getValue("fieldName");
            String curRequired = attributes.getValue("required");
            boolean required = StringUtils.equals(curRequired, "true");
            String curType = attributes.getValue("type");
            String curHorizontalMerger = attributes.getValue("horizontalMerger");
            ColumnHead columnHead = new ColumnHead(curTitleName, curFieldName);
            columnHead.setRequired(required);
            if (StringUtils.isNotEmpty(curType))
            {
                columnHead.setConvertorType(curType);
            }
            if (StringUtils.isNotEmpty(curHorizontalMerger))
            {
                columnHead.setHorizontalMergerType(curHorizontalMerger);
            }
            curToParseTemplate.add(columnHead);
        }
    }

    @Override
    public void endElement(String uri, String localName, String name) throws SAXException
    {
        if (name.equals("parseSystem"))
        {
            isSysElement = false;
        }
    }

    @Override
    public void endDocument() throws SAXException
    {
    }

    @Override
    public InputSource resolveEntity(String publicId, String systemId) throws IOException, SAXException
    {
        return new InputSource(this.getClass().getClassLoader().getResourceAsStream("fileParse.dtd"));
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException
    {
    }

    public Map<String, ToParseFile> getToParseFileMap()
    {
        return toParseFileMap;
    }

    public void setToParseFileMap(Map<String, ToParseFile> toParseFileMap)
    {
        this.toParseFileMap = toParseFileMap;
    }
}
