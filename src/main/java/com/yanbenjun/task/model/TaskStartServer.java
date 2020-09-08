package com.yanbenjun.task.model;

/**
 * 任务
 * @author Administrator
 *
 */
public class TaskStartServer
{
    private String ip;
    
    private int port;
    
    /**
     * 这个和通讯方式有关
     * 不同java进程之间的通讯
     * webservice、socket等等用到的属性可能不一样
     */
    private String remark;
    
    public static TaskStartServer local()
    {
        TaskStartServer localServer = new TaskStartServer();
        
        return localServer;
    }
    
    /**
     * 根据IP和port信息，通知在IP位置的启动的任务结束
     * @param taskId 任务ID
     */
    public void notifyFinish(String taskId)
    {
        // TODO 待实现
    }

    public String getIp()
    {
        return ip;
    }

    public void setIp(String ip)
    {
        this.ip = ip;
    }

    public int getPort()
    {
        return port;
    }

    public void setPort(int port)
    {
        this.port = port;
    }

    public String getRemark()
    {
        return remark;
    }

    public void setRemark(String remark)
    {
        this.remark = remark;
    }
}
