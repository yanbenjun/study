package com.yanbenjun.request.collapse;

import java.util.concurrent.CompletableFuture;

public class Request<T>
{
    private Object[] params;
    
    private CompletableFuture<T> future;

    /**
     * @return the params
     */
    public Object[] getParams()
    {
        return params;
    }

    /**
     * @param params the params to set
     */
    public void setParams(Object[] params)
    {
        this.params = params;
    }

    /**
     * @return the future
     */
    public CompletableFuture<T> getFuture()
    {
        return future;
    }

    /**
     * @param future the future to set
     */
    public void setFuture(CompletableFuture<T> future)
    {
        this.future = future;
    }
    
    
}
