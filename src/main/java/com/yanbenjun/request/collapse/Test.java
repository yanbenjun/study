package com.yanbenjun.request.collapse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;



public class Test
{
    static LinkedBlockingQueue<Request<List<Map<String, Object>>>> waitingRequests = new LinkedBlockingQueue<>();
    static {
        ScheduledThreadPoolExecutor pool = new ScheduledThreadPoolExecutor(10);
        pool.scheduleAtFixedRate(()->{
            int size = waitingRequests.size();
            if(size == 0) {
                return;
            }
            List<Request<List<Map<String, Object>>>> rs = new ArrayList<>();
            for(int i=0; i<size; i++) {
                rs.add(waitingRequests.poll());
            }
            Map<String, Request> map = rs.stream().collect(Collectors.toMap(item ->{
                return (String) item.getParams()[0];
            }, Function.identity()));
            List<String> param = rs.stream().map(item ->{
                return (String) item.getParams()[0];
            }).collect(Collectors.toList());
            
            Thread t = new Thread(() -> {
                List<List<Map<String, Object>>> result = batchQueryByCodes(param);
                int i=0;
                for (Request<List<Map<String, Object>>> r : rs) {
                    //TODO
                    r.getFuture().complete(result.get(i++));
                }
            });
            t.setName("Batch Request Thread");
            t.start();
            System.out.println("合并了" + size+"个请求");
        }, 0, 10, TimeUnit.MILLISECONDS);
    }
    public List<Map<String, Object>> queryByCode(String code) throws InterruptedException, ExecutionException {
        Request<List<Map<String, Object>>> request = new Request<>();
        request.setParams(new String[]{code});
        CompletableFuture<List<Map<String, Object>>> future = new CompletableFuture<>();
        request.setFuture(future);
        waitingRequests.add(request);
        return future.get();
    }
    
    public static List<List<Map<String, Object>>> batchQueryByCodes(List<String> code) {
        try
        {
            Thread.sleep(199);
        }
        catch (InterruptedException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
       return  code.stream().map(item -> {
            Map<String, Object> map = new HashMap<>();
            map.put(item, item);
            return Stream.of(map).collect(Collectors.toList());
        }).collect(Collectors.toList());
        
    }
    
    public static void main(String[] args)
    {
        Test test = new Test();
        for(int i=0;i<1000; i++) {
            final int index = i;
            new Thread() {
                public void run() {
                    try
                    {
                        long startTime = System.currentTimeMillis();
                        List<Map<String,Object>> list = test.queryByCode("code" + index);
                        System.out.println("返会结果：" + list);
                        System.out.println(System.currentTimeMillis() - startTime);
                    }
                    catch (InterruptedException e)
                    {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    catch (ExecutionException e)
                    {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }.start();
        }
    }
}
