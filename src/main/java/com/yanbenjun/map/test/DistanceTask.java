package com.yanbenjun.map.test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yanbenjun.map.LngLat;

public class DistanceTask implements Runnable
{
    private List<LngLat> list2;

    private List<LngLat> list1;

    public DistanceTask(List<LngLat> list2, List<LngLat> list1)
    {
        this.list2 = list2;
        this.list1 = list1;
    }

    @Override
    public void run()
    {
        sort(100);
    }
    
    private void unsort()
    {
        Map<LngLat, double[]> distanceMap = initDistanceMap(list1.size());

        for (LngLat l2 : list2)
        {
            int i = 0;
            double[] arr = distanceMap.get(l2);
            for (LngLat l1 : list1)
            {
                double distance = l2.distance2(l1);
                arr[i++] = distance;
            }
        }
    }

    private void sort(int size)
    {
        Map<LngLat, double[]> distanceMap = initDistanceMap(size);

        for (LngLat l2 : list2)
        {
            int i = 0;
            double[] arr = distanceMap.get(l2);
            for (LngLat l1 : list1)
            {
                double distance = l2.distance2(l1);
                insertArr(arr,distance,i++,size);
            }
        }
        System.out.println(1);
    }
    
    private void insertArr(double[] arr, double value, int k, int size)
    {
        if(k < size)
        {
            initInsert(arr,value);
            return;
        }
        double max = arr[0];
        if(max > value)
        {
            insert(arr,value);
        }
    }
    
    private void initInsert(double[] arr, double value)
    {
        int index = 0;
        
        for(int i=0; i<arr.length; i++)
        {
            if(arr[i] == 0)
            {
                index = i;
                break;
            }
            else
            {
                if(arr[i] > value)
                {
                    index = i;
                }
                else
                {
                    break;
                }
            }
        }
        for(int j=arr.length-1; j>index; j--)
        {
            arr[j] = arr[j-1];
        }
        arr[index] = value;
    }
    
    private void insert(double[] arr, double value)
    {
        int index = 0;
        for(int i=0; i<arr.length; i++)
        {
            if(arr[i] > value)
            {
                index = i;
            }
            else
            {
                break;
            }
        }
        
        for(int j=0; j<index; j++)
        {
            arr[j] = arr[j+1];
        }
        arr[index] = value;
    }

    private Map<LngLat, double[]> initDistanceMap(int size)
    {
        long startTime = System.currentTimeMillis();
        Map<LngLat, double[]> distanceMap = new HashMap<>(list2.size());
        for (LngLat l2 : list2)
        {
            double[] distances = new double[size];
            distanceMap.put(l2, distances);
        }
        System.out.println("Init map cost time: " + (System.currentTimeMillis() - startTime) + "ms");
        return distanceMap;
    }

}
