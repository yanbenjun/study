package com.yanbenjun.task.model.request;

import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

public class TaskParam
{
    private ParamType type;
    
    private String body;
    
    private Map<String,Object> params = new HashMap<String,Object>();
    
    public TaskParam(String body,ParamType type)
    {
        this.type = type;
        this.body = body;
    }
    
    public <T> T toModel(Class<T> cls)
    {
        JSONObject json = JSONObject.fromObject(body);
        return (T) JSONObject.toBean(json, cls);
    }
    
    public ParamType getType()
    {
        return type;
    }

    public void setType(ParamType type)
    {
        this.type = type;
    }

    public String getBody()
    {
        return body;
    }

    public void setBody(String body)
    {
        this.body = body;
    }

    public Map<String, Object> getParams()
    {
        return params;
    }

    public void setParams(Map<String, Object> params)
    {
        this.params = params;
    }

    public static void main(String[] args)
    {
        TaskParam tp = new TaskParam("{\"body\":{\"test1\":111}}", ParamType.JSON);
        TaskRequest tm = tp.toModel(TaskRequest.class);
        System.out.println(tm);
    }
}
