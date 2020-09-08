package com.yanbenjun.file.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;

import com.yanbenjun.file.config.element.ToParseFile;

public class ParseSystemLoader
{
    private static final String PARSE_CONFIG_FILE_NAME = "fileParse.xml";
    private static Map<String,ToParseFile> toParseFileMap = new HashMap<String,ToParseFile>();
    
    public static void load(String systemName)
    {
        InputStream in = ParseSystemLoader.class.getClassLoader().getResourceAsStream(PARSE_CONFIG_FILE_NAME);
        load(in,systemName);
    }
    
    public static void load(InputStream in, String systemName)
    {
        SAXParserFactory sParserFactory = SAXParserFactory.newInstance();  
        SAXParser parser;
        try
        {
            parser = sParserFactory.newSAXParser();
            XmlConfigHandler handler = new XmlConfigHandler(systemName);  
            parser.parse(in, handler);
            toParseFileMap.putAll(handler.getToParseFileMap());
        }
        catch (ParserConfigurationException e)
        {
            e.printStackTrace();
        }
        catch (SAXException e)
        {
            e.printStackTrace();
        }  
        catch (IOException e)
        {
            e.printStackTrace();
        } 
    }
    
    public static ToParseFile get(String fileId)
    {
        return toParseFileMap.get(fileId);
    }
}
