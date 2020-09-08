package com.yanbenjun.map.test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.commons.lang.StringUtils;

import com.yanbenjun.map.LngLat;
import com.yanbenjun.map.LngLatBoundary;
import com.yanbenjun.map.LngLatBoundaryFormatException;
import com.yanbenjun.map.LngLatFormatException;

public class Test
{
    public static final int SIZE = 1000000;

    public static void testDistance() throws Exception
    {

        LngLat min = LngLat.parse("150,20");
        LngLat max = LngLat.parse("180,50");
        long startTime = System.currentTimeMillis();
        BufferedWriter bw = new BufferedWriter(new FileWriter(new File("D://lnglats.txt")));
        List<LngLat> list1 = new ArrayList<>(150000);
        for (int i = 0; i < 150000; i++)
        {
            LngLat randLngLat = LngLat.randLngLat(min, max);
            list1.add(randLngLat);
            // System.out.println(randLngLat);
        }
        List<LngLat> list2 = new ArrayList<>(25);
        for (int i = 0; i < 50; i++)
        {
            LngLat randLngLat = LngLat.randLngLat(min, max);
            list2.add(randLngLat);
            // System.out.println(randLngLat);
        }
        bw.close();
        long endTime = System.currentTimeMillis();
        System.out.println("Generate Cost: " + (endTime - startTime) + "ms");

        for (LngLat l2 : list2)
        {
            for (LngLat l1 : list1)
            {
                double distance = l2.distance2(l1);
            }
        }
        long endTime2 = System.currentTimeMillis();
        System.out.println("Caculate distance Cost: " + (endTime2 - endTime) + "ms");

        long endTime3 = System.currentTimeMillis();
        System.out.println("Generate Cost: " + (endTime - startTime) + "ms");
        ExecutorService es = Executors.newFixedThreadPool(4);
        List<Future> fs = new ArrayList<Future>();
        for (int i = 0; i < 1; i++)
        {
            DistanceTask dt = new DistanceTask(list2, list1);
            Future<?> f = es.submit(dt);
            fs.add(f);
        }

        for (Future<?> f : fs)
        {
            try
            {
                f.get();
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
            catch (ExecutionException e)
            {
                e.printStackTrace();
            }
        }

        long endTime4 = System.currentTimeMillis();
        System.out.println("Caculate distance Cost async: " + (endTime4 - endTime3) + "ms");

    }
    
    public static void testCompress(String filePath) throws IOException, LngLatBoundaryFormatException
    {
    	Files.readAllLines(Paths.get(filePath)).stream().forEach(line ->{
    		try {
    			System.out.println(line.split(";").length);
    			long start0 = System.currentTimeMillis();
    			LngLat[] lls = LngLatBoundary.parse(line);
    			long start = System.currentTimeMillis();
    			System.out.println("parse cost:" + (start-start0));
    			LngLatBoundary compressed = new LngLatBoundary(lls).compress(0.5);
    			long end = System.currentTimeMillis();
    			System.out.println("compress cost:" + (end-start));
    			System.out.println("parse and compress cost:" + (end-start0));
    			System.out.println(compressed.getLngLats().length);
    			System.out.println(compressed);
    		} catch (Exception e) {
				e.printStackTrace();
			}
    	});;
    }
    
    public static String getString()
    {
    	StringBuffer sb = new StringBuffer();
    	for(int i=0; i<1; i++)
    	{
    		sb.append("s");
    	}
    	return sb.toString();
    }
    
    public static void testStringUtils()
    {
    	final int num = 10000000;
    	String str = getString();
    	long start = System.currentTimeMillis();
    	for(int i=0; i<num; i++)
    	{
    		if(StringUtils.isEmpty(str))
    		{
    			System.out.println(str);
    		}
    	}
    	long start0 = System.currentTimeMillis();
    	System.out.println("StringUtils cost: " + (start0-start));
    }

    public static void main(String[] args) throws LngLatFormatException, IOException, LngLatBoundaryFormatException
    {
    	for(int i=0; i<1; i++)
    	{
    		testCompress("D://txt/test.txt");
    	}
    	//testStringUtils();
        /*double d = Math.tan(3*Math.PI / 8)* Math.tan(Math.PI /8)* Math.tan(Math.PI /8)* Math.tan(Math.PI /8);
        double s = Math.atan(Math.sqrt(d))*4 - Math.PI/2;
        System.out.println(s);
        try
        {
            testDistance();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }*/
    }
}
