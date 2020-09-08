package com.yanbenjun.file.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.yanbenjun.file.config.element.ColumnHead;
import com.yanbenjun.file.config.element.ParsePoint;
import com.yanbenjun.file.config.element.ParseSystem;
import com.yanbenjun.file.config.element.TagConstants;
import com.yanbenjun.file.config.element.ToParseFile;
import com.yanbenjun.file.config.element.ToParseTemplate;

/**
 * <!-- 
        *解析系统，集成到业务系统中，让业务系统能够方便的让开发整合excel、csv、xml数据解析功能到业务系统；
        *可以灵活的配置不同的模板、或者尽可能多的包络系统已知属性，形成一个通用模板；
        *具体用什么model装数据、存储到什么数据库、可以配置到xml里写死（适合固定模板的解析）；也可以不用配置，等用户上传
        *完文件后，让用户确认文件类型或者将类型写入到Excel自动识别（和业务有关），类型可以知道数据存储的java模型和具体入库
        *数据库。
     -->
     
 * 可以设置配置文件，默认为单sheet解析方式，到文件层就可以确定具体需要解析到的java模型或者数据库模型
 * 如果需要多sheet页模式，需要再toParseFile开启多sheet页解析模式，到sheet页层级才能确定需要解析到的java模型和数据库模型
 * @author Administrator
 *
 */
public class FileParseConfigLoader
{
    public static String fileName = "fileParse-context.xml";
    
    private static String[] TEXT_TAG = new String[]{"#text"};
    
    public static InputStream getClassAbsolutePath(String fileName)
    {
        InputStream ins = FileParseConfigLoader.class.getClassLoader().getResourceAsStream(fileName);
        return ins;
    }
    
    /**
     * 利用DOM解析xml，局限性比较大，也有优势（可以先解析没有依赖关系的元素，后解析有依赖的元素，按照拓扑关系解析）
     */
    public static void domParse()
    {
        DocumentBuilderFactory dbfactory = DocumentBuilderFactory.newInstance();
        try
        {
            DocumentBuilder db = dbfactory.newDocumentBuilder();
            org.w3c.dom.Document document = db.parse(getClassAbsolutePath(fileName));
            ParseSystem parseSystem = new ParseSystem();
            //
            NodeList columnsNodes = document.getElementsByTagName(TagConstants.COLUMNS);
            parseSystem.addAllColumnHeads(createColumnHeads(columnsNodes,parseSystem));
            //
            NodeList templatesNodes = document.getElementsByTagName(TagConstants.TEMPLATES);
            parseSystem.addAllTemplates(createTemplates(templatesNodes,parseSystem));
            
            NodeList filesNodes = document.getElementsByTagName(TagConstants.FILES);
            parseSystem.addAllFiles(createFiles(parseSystem,filesNodes));
            
            NodeList parsePointNodes = document.getElementsByTagName(TagConstants.PARSE_POINT);
            parseSystem.addAllParsePoints(createParsePoints(parseSystem,parsePointNodes));
            
        }
        catch (ParserConfigurationException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (SAXException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }
    
    private static List<ParsePoint> createParsePoints(ParseSystem parseSystem, NodeList parsePointNodes)
    {
        List<ParsePoint> parsePoints = new ArrayList<>();
        if(parsePointNodes != null)
        {
            for(int i=0; i<parsePointNodes.getLength(); i++)
            {
                Node parsePointNode = parsePointNodes.item(i);
                String id = getNodeValue(parsePointNode.getAttributes().getNamedItem("id"));
                String mode = getNodeValue(parsePointNode.getAttributes().getNamedItem("mode"));
                ParsePoint parsePoint = new ParsePoint(Long.parseLong(id),mode);
                List<ToParseFile> toParseFiles = createFiles(parseSystem, parsePointNode);
                parsePoint.addAll(toParseFiles);
                parsePoints.add(parsePoint);
            }
        }
        return parsePoints;
    }
    
    private static List<ToParseFile> createFiles(ParseSystem parseSystem, Node toParseFileParentNode)
    {
        List<ToParseFile> files = new ArrayList<>();
        NodeList fileList = toParseFileParentNode.getChildNodes();
        if(fileList != null)
        {
            for(int j=0; j<fileList.getLength(); j++)
            {
                Node fileNode = fileList.item(j);
                if(check("parsePoint or files", fileNode, new String[]{"toParseFile", "refFile"}, TEXT_TAG))
                {
                     ToParseFile toParseFile = toParseFileExtend(parseSystem, fileNode);
                     files.add(toParseFile);
                }
            }
        }
        return files;
    }

    private static List<ToParseFile> createFiles(ParseSystem parseSystem, NodeList filesNodes)
    {
        List<ToParseFile> files = new ArrayList<>();
        if(filesNodes != null)
        {
            for(int i=0; i<filesNodes.getLength(); i++)
            {
                Node node = filesNodes.item(i);
                files.addAll(createFiles(parseSystem, node));
            }
        }
        return files;
    }

    private static List<ToParseTemplate> createTemplates(NodeList templatesNodes, ParseSystem parseSystem)
    {
        List<ToParseTemplate> templateList = new ArrayList<>();
        if(templatesNodes != null)
        {
            for(int i=0; i<templatesNodes.getLength(); i++)
            {
                Node nodes = templatesNodes.item(i);
                NodeList templateNodeList = nodes.getChildNodes();
                if(templateNodeList != null)
                {
                    for(int j=0; j<templateNodeList.getLength(); j++)
                    {
                        Node templateNode = templateNodeList.item(j);
                        if(check("toParseFile or templates", templateNode, new String[]{"toParseTemplate", "refTemplate"}, TEXT_TAG))
                        {
                            ToParseTemplate tpt = toParseTemplateExtend(parseSystem, templateNode);
                            templateList.add(tpt);
                        }
                    }
                }
            }
        }
        return templateList;
    }

    private static List<ColumnHead> createColumnHeads(NodeList columnsNodes, ParseSystem parseSystem)
    {
        List<ColumnHead> columnHeads = new ArrayList<>();
        if(columnsNodes != null)
        {
            for(int i=0; i<columnsNodes.getLength(); i++)
            {
                Node nodes = columnsNodes.item(i);
                NodeList columnHeadList = nodes.getChildNodes();
                if(columnHeadList != null)
                {
                    for(int j=0; j<columnHeadList.getLength(); j++)
                    {
                        Node columnHeadNode = columnHeadList.item(j);
                        if(check("toParseTemplate or columns", columnHeadNode, new String[]{"columnHead", "refColumn"}, TEXT_TAG))
                        {
                             ColumnHead columnHead = createColumnHeadExtend(parseSystem, columnHeadNode);
                             columnHeads.add(columnHead);
                        }
                    }
                }
            }
        }
        return columnHeads;
    }
    
    public static ToParseFile toParseFileExtend(ParseSystem parseSystem, Node node)
    {
        ToParseFile toParseFile = null;
        if(node.getNodeName().equals("toParseFile"))
        {
            toParseFile = toParseFile(parseSystem, node);
        }
        else if(node.getNodeName().equals("refFile"))
        {
            String name = getNodeValue(node.getAttributes().getNamedItem("name"));
            toParseFile = parseSystem.getToParseFile(name);
            if(toParseFile == null)
            {
                throw new ParseConfigurationException("没有找到name为：" + name +" 的toParseFile元素");
            }
        }
        return toParseFile;
    }

    public static ToParseFile toParseFile(ParseSystem parseSystem, Node node)
    {
        ToParseFile toParseFile = new ToParseFile();
        NodeList children = node.getChildNodes();
        NamedNodeMap attrs = node.getAttributes();
        String name = getNodeValue(attrs.getNamedItem("name"));
        String mode = getNodeValue(attrs.getNamedItem("mode"));
        toParseFile.setName(name);
        toParseFile.setMode(mode);
        if(children != null)
        {
            for(int j=0; j<children.getLength(); j++)
            {
                Node templateNode = children.item(j);
                if(check("toParseFile or templates", templateNode, new String[]{"toParseTemplate", "refTemplate"}, TEXT_TAG))
                {
                    ToParseTemplate toParseTemplate = toParseTemplateExtend(parseSystem, templateNode);
                    toParseFile.add(toParseTemplate);
                }
            }
        }
        
        return toParseFile;
    }
    
    private static ToParseTemplate toParseTemplateExtend(ParseSystem parseSystem, Node node)
    {
        ToParseTemplate toParseTemplate = null;
        if(node.getNodeName().equals("toParseTemplate"))
        {
            toParseTemplate = toParseTemplate(parseSystem, node);
        }
        else if(node.getNodeName().equals("refTemplate"))
        {
            String name = getNodeValue(node.getAttributes().getNamedItem("name"));
            toParseTemplate = parseSystem.getToParseTemplate(name);
            if(toParseTemplate == null)
            {
                throw new ParseConfigurationException("没有找到name为：" + name +" 的toParseTemplate元素");
            }
        }
        return toParseTemplate;
    }
    
    private static ToParseTemplate toParseTemplate(ParseSystem parseSystem, Node node)
    {
        NodeList children = node.getChildNodes();
        NamedNodeMap attrs = node.getAttributes();
        String name = getNodeValue(attrs.getNamedItem("name"));
        String sheetIndex = getNodeValue(attrs.getNamedItem("sheetIndex"));
        String headRow = getNodeValue(attrs.getNamedItem("headRow"));
        String contentStartRow = getNodeValue(attrs.getNamedItem("contentStartRow"));
        String modelClass = getNodeValue(attrs.getNamedItem("modelClass"));
        String typeEnum = getNodeValue(attrs.getNamedItem("typeEnum"));
        String type = getNodeValue(attrs.getNamedItem("type"));
        String userDefined = getNodeValue(attrs.getNamedItem("userDefined"));
        ToParseTemplate toParseTemplate = new ToParseTemplate(Integer.parseInt(sheetIndex));
        toParseTemplate.setName(name);
        toParseTemplate.setHeadRow(headRow == null ? 0 : Integer.parseInt(headRow));
        toParseTemplate.setStartContent(contentStartRow == null ? toParseTemplate.getHeadRow() + 1 : Integer.parseInt(contentStartRow));
        toParseTemplate.setModelClass(modelClass);
        toParseTemplate.setTypeEnm(typeEnum);
        toParseTemplate.setType(type);
        toParseTemplate.setUserDefined(userDefined);
        if(children != null)
        {
            for(int j=0; j<children.getLength(); j++)
            {
                Node columnNode = children.item(j);
                if(check("toParseTemplate", columnNode, new String[]{"columnHead", "refColumn"}, TEXT_TAG))
                {
                    ColumnHead columnHead = createColumnHeadExtend(parseSystem, children.item(j));
                    toParseTemplate.add(columnHead);
                }
            }
        }
        
        return toParseTemplate;
    }
    
    private static ColumnHead createColumnHeadExtend(ParseSystem parseSystem,Node columnHeadNode)
    {
        ColumnHead columnHead = null;
        if(columnHeadNode.getNodeName().equals("columnHead"))
        {
             columnHead = createColumnHead(columnHeadNode);
        }
        else if(columnHeadNode.getNodeName().equals("refColumn"))
        {
            NamedNodeMap attrs = columnHeadNode.getAttributes();
            String id = getNodeValue(attrs.getNamedItem("id"));
            columnHead = parseSystem.getColumnHead(id);
            if(columnHead == null)
            {
                throw new ParseConfigurationException("没有找到id为：" + id +" 的columnHead元素");
            }
        }
        return columnHead;
    }

    private static ColumnHead createColumnHead(Node node)
    {
        NamedNodeMap attrs = node.getAttributes();
        String id = getNodeValue(attrs.getNamedItem("id"));
        String titleName = getNodeValue(attrs.getNamedItem("titleName"));
        String fieldName = getNodeValue(attrs.getNamedItem("fieldName"));
        String required = getNodeValue(attrs.getNamedItem("required"));
        String type = getNodeValue(attrs.getNamedItem("type"));
        String horizontalMerger = getNodeValue(attrs.getNamedItem("horizontalMerger"));
        
        ColumnHead ch = new ColumnHead(titleName,fieldName);
        ch.setId(id == null ? null : Long.valueOf(id));
        ch.setRequired("true".equals(required) ? true : false);
        ch.setConvertorType(type);
        ch.setHorizontalMergerType(horizontalMerger);
        return ch;
    }
    
    private static String getNodeValue(Node attrNode)
    {
        return attrNode == null ? null : attrNode.getNodeValue();
    }
    
    private static boolean check(String parentNodeName, Node node, String[] supportTags, String[] ignoredTags)
    {
        String name = node.getNodeName();
        for(String supportTag : supportTags)
        {
            if(supportTag.equals(name))
            {
                return true;
            }
        }
        for(String ignoredTag : ignoredTags)
        {
            if(ignoredTag.equals(name))
            {
                return false;
            }
        }
        throw new ParseConfigurationException("父标签：‘" + parentNodeName +"’下不支持‘"+node.getNodeName()+"’子标签");
    }

    public static void main(String[] args) throws SAXException
    {
        domParse();
        
        SAXReader saxReader = new SAXReader();
        try
        {
            Document document = saxReader.read(getClassAbsolutePath(fileName));
            Iterator<Element> iter = document.nodeIterator();
            while(iter.hasNext())
            {
                Element node = iter.next();
                String str = node.getName();
                String typeName = node.getNodeTypeName();
                System.out.println(typeName);
                Iterator<Element> fileParse = node.elementIterator();
                while(fileParse.hasNext())
                {
                    Element e = fileParse.next();
                    System.out.println(e.getName());
                }
            }
        }
        catch (DocumentException e)
        {
            e.printStackTrace();
        }
    }
    
}
