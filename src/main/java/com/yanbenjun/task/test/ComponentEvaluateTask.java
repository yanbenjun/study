package com.yanbenjun.task.test;

import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

import com.yanbenjun.task.inf.AsyncBranchTask;
import com.yanbenjun.task.inf.AsyncTask;
import com.yanbenjun.task.model.progress.TaskProgressManager;
import com.yanbenjun.task.model.request.TaskRequest;

public class ComponentEvaluateTask extends AsyncBranchTask
{
    protected ComponentEvaluateTask(TaskRequest taskRequest, TaskProgressManager taskProgressManager,
            ThreadPoolExecutor threadPool)
    {
        super(taskRequest, taskProgressManager, threadPool);
    }

    @Override
    public List<AsyncTask> newAllChildrenTask()
    {
        // TODO Auto-generated method stub
        return null;
    }

}
