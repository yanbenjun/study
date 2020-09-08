package com.yanbenjun.task.test;

import com.yanbenjun.task.model.progress.SystemTaskProgressManager;
import com.yanbenjun.task.model.progress.TaskProgressManager;
import com.yanbenjun.task.model.request.ParamType;
import com.yanbenjun.task.model.request.TaskParam;
import com.yanbenjun.task.model.request.TaskRequest;
import com.yanbenjun.task.util.UUIDGenerator;

public class EvaluateMainTask
{
    public static void main(String[] args)
    {
        TaskRequest taskRequest = new TaskRequest();
        TaskParam param = new TaskParam("{\"projectId\":\"123\",\"indexIds\":[\"1\",\"2\",\"3\"]}", ParamType.JSON);
        taskRequest.setTaskParam(param);
        taskRequest.setTaskId(UUIDGenerator.uuid());
        taskRequest.setTaskName("EvaluateTask_" + 1);
        TaskProgressManager tpm = new SystemTaskProgressManager();
        EvaluateTask et = new EvaluateTask(taskRequest, tpm);
        et.execute();
    }
}
