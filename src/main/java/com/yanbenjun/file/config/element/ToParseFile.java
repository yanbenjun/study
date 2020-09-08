package com.yanbenjun.file.config.element;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@XmlElement(name="toParseFile")
public class ToParseFile extends XElement
{
    @XmlAttribute(name="mode",isAttr=true)
    private String mode;
    
    private List<ToParseTemplate> toParseTemplates = new ArrayList<ToParseTemplate>();
    
    private Set<Integer> toParseIndexSet = new HashSet<Integer>();
    
    @XmlElement(name="toParseTemplate",subElement=ToParseTemplate.class)
    @XmlElement(name="refTemplate")
    private Map<Integer,ToParseTemplate> toParseTemplateMap = new HashMap<Integer,ToParseTemplate>();
    
    public ToParseFile()
    {
        
    }
    
    public ToParseFile(String path, List<ToParseTemplate> templates)
    {
        this.toParseTemplates.addAll(templates);
        toParseIndexSet.addAll(templates.stream().map(ToParseTemplate :: getSheetIndex).collect(Collectors.toList()));
        
        //TODO
        for(ToParseTemplate template : templates)
        {
            toParseTemplateMap.put(template.getSheetIndex(), template);
        }
    }
    
    public boolean needParse(int sheetIndex)
    {
        return toParseIndexSet.contains(sheetIndex);
    }
    
    
    @Override
    public void add(XElement xe)
    {
        add((ToParseTemplate)xe);
    }
    
    public void add(ToParseTemplate template)
    {
        this.toParseTemplates.add(template);
        this.toParseIndexSet.add(template.getSheetIndex());
        toParseTemplateMap.put(template.getSheetIndex(), template);
    }
    
    public ToParseTemplate getTemplateWith(int sheetIndex)
    {
        return toParseTemplateMap.get(sheetIndex);
    }

    public List<ToParseTemplate> getToParseTemplates()
    {
        return toParseTemplates;
    }
    
    public List<ToParseTemplate> getSortedTemplates()
    {
        return toParseTemplates.stream().sorted((o1,o2)->o1.getPriority() - o2.getPriority()).collect(Collectors.toList());
    }

    public String getMode()
    {
        return mode;
    }

    public void setMode(String mode)
    {
        this.mode = mode;
    }
}
