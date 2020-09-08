package com.yanbenjun.task.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yanbenjun.task.TaskPoolConfig;
import com.yanbenjun.task.inf.AsyncBranchTask;
import com.yanbenjun.task.inf.AsyncTask;
import com.yanbenjun.task.model.progress.TaskProgressManager;
import com.yanbenjun.task.model.request.ParamType;
import com.yanbenjun.task.model.request.TaskParam;
import com.yanbenjun.task.model.request.TaskRequest;
import com.yanbenjun.task.util.ThreadPoolExecutorBuilder;
import com.yanbenjun.task.util.UUIDGenerator;

public class EvaluateTask extends AsyncBranchTask
{
    private static ThreadPoolExecutor taskExecutePool = ThreadPoolExecutorBuilder.newPoolWithAbortPolicy(TaskPoolConfig.getDefaultConfig());

    protected EvaluateTask(TaskRequest taskRequest, TaskProgressManager taskProgressManager)
    {
        super(taskRequest, taskProgressManager, taskExecutePool);
    }

    @Override
    public List<AsyncTask> newAllChildrenTask()
    {
        TaskParam param = this.taskRequest.getTaskParam();
        String paramStr = param.getBody();
        Map<String,Object> paramMap = new Gson().fromJson(paramStr, new TypeToken<Map<String, Object>>(){}.getType());
        String projectId = (String) paramMap.get("projectId");
        List<String> indexIdList = (List<String>) paramMap.get("indexIds");
        List<AsyncTask> subTasks = new ArrayList<>();
        ThreadPoolExecutor indexEvaluatePool = ThreadPoolExecutorBuilder.newPoolWithAbortPolicy(TaskPoolConfig.getDefaultConfig());
        System.out.println(this.taskRequest.getTaskId());
        for(String indexId : indexIdList)
        {
            TaskRequest indexRequest = new TaskRequest();
            indexRequest.setTaskParam(EvaluateService.queryTaskParam(projectId, indexId));
            String uuid = UUIDGenerator.uuid();
            indexRequest.setTaskId(uuid);
            indexRequest.setTaskName("IndexEvaluate_"+uuid);
            System.out.println("Start add" +indexId +",uuid" + uuid);
            subTasks.add(new IndexEvaluateTask(indexRequest, taskProgressManager, indexEvaluatePool));
        }
        return subTasks;
    }
    
    private static class EvaluateService
    {
        /**
         * 模拟从数据库获取并构成子任务的必要参数
         * @param parentParam
         * @return
         */
        public static TaskParam queryTaskParam(Object... parentParam)
        {
            try
            {
                Thread.sleep(100);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
            return new TaskParam(parentParam.toString(), ParamType.JSON);
        }
    }


}
