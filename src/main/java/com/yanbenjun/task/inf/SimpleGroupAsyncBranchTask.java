package com.yanbenjun.task.inf;

import java.util.ArrayList;
import java.util.List;

import com.yanbenjun.task.error.TaskExecutionExceptionModel;
import com.yanbenjun.task.model.progress.TaskProgress;
import com.yanbenjun.task.model.progress.TaskProgressManager;
import com.yanbenjun.task.model.request.TaskRequest;

/**
 * 一组异步任务的执行 async = true 提交了这组异步任务，直接返回提交结果，让其自行执行 async = false
 * 提交这组异步任务，等待所有任务执行完毕，返回最终结果
 * 
 * @author Administrator
 *
 */
public class SimpleGroupAsyncBranchTask extends SimpleAsyncBranchTask
{
    private List<SimpleAsyncBranchTask> subTasks = new ArrayList<>();
    private List<SimpleAsyncBranchTask> awaitTasks = new ArrayList<>();
    private List<TaskExecutionExceptionModel> taskExceptionModels = new ArrayList<>();

    protected SimpleGroupAsyncBranchTask(TaskRequest taskRequest, TaskProgressManager taskProgressManager)
    {
        super(taskRequest, taskProgressManager);
    }
    
    public void add(SimpleAsyncBranchTask saLeafTask)
    {
        subTasks.add(saLeafTask);
        awaitTasks.add(saLeafTask);
    }

    public String getSubTaskExecutionExceptionStr()
    {
        StringBuilder sb = new StringBuilder();
        for (TaskExecutionExceptionModel tee : taskExceptionModels)
        {
            sb.append("Task: " + tee.getTaskId() + "\n");
            sb.append("task error information: " + tee.getErrorInfo() + "\n");
            sb.append(tee.getException().getMessage() + "\n");
        }
        return sb.toString();
    }

    @Override
    public TaskProgress progress()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<AsyncTask> newAllChildrenTask()
    {
        // TODO Auto-generated method stub
        return null;
    }

}
