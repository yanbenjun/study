package com.yanbenjun.task.model.response;

import java.util.concurrent.Future;

/**
 * 模仿HTTP请求的返回消息
 * @author Administrator
 *
 */
public class TaskResponse
{
    /**
     * 状态码，暂定200成功、400失败
     */
    private int statusCode;
    
    private String version;
    
    private TaskResponseHeader header;
    
    /**
     * 用于同一个执行任务的jvm和接收结果的jvm是同一个，直接获取保存Future对象，判断任务是否结束
     * 不能将此字段序列化
     */
    private transient Future<?> future;
    
    private Object body;
    
    public int getStatusCode()
    {
        return statusCode;
    }

    public void setStatusCode(int statusCode)
    {
        this.statusCode = statusCode;
    }

    public String getVersion()
    {
        return version;
    }

    public void setVersion(String version)
    {
        this.version = version;
    }

    public TaskResponseHeader getHeader()
    {
        return header;
    }

    public void setHeader(TaskResponseHeader header)
    {
        this.header = header;
    }

    public Object getBody()
    {
        return body;
    }

    public void setBody(Object body)
    {
        this.body = body;
    }

    public static void main(String[] args)
    {
    }

    public Future<?> getFuture()
    {
        return future;
    }

    public void setFuture(Future<?> future)
    {
        this.future = future;
    }
}
