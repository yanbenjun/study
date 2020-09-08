package com.yanbenjun.map;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;

import com.yanbenjun.map.geometry.Point2D;
import com.yanbenjun.map.geometry.Polyn2D;
import com.yanbenjun.map.geometry.Sphere;
import com.yanbenjun.util.Pair;

/**
 * GIS地图边界（无飞地） 集成边界长度和面积计算、边界中心点计算、边界压缩、边界的Rect计算、边界与点的关系计算等
 * 
 * @author Administrator
 *
 */
public class LngLatBoundary
{
    private static final int MIN_SIZE = 3;
    private static final String LNGLAT_SPLITTER = ";";
    private int size;

    private double area;

    private double length;

    private LngLat[] lngLats;

    private LngLat[] movedLngLats;
    private boolean spanAntimeridian;
    private Set<Integer> sphereSeqs = new HashSet<>();

    public LngLatBoundary(LngLat[] lngLats)
    {
        Objects.requireNonNull(lngLats);
        if (lngLats.length < MIN_SIZE)
        {
            throw new RuntimeException();
        }
        else if (lngLats.length == MIN_SIZE && lngLats[0].same(lngLats[MIN_SIZE - 1]))
        {
            throw new RuntimeException();
        }

        if (lngLats[0].same(lngLats[lngLats.length - 1]))
        {
            this.lngLats = new LngLat[lngLats.length];
            for (int i = 0; i < lngLats.length; i++)
            {
                this.lngLats[i] = lngLats[i].clone();
            }
        }
        else
        {
            this.lngLats = new LngLat[lngLats.length + 1];
            for (int i = 0; i < lngLats.length; i++)
            {
                this.lngLats[i] = lngLats[i].clone();
            }
            this.lngLats[lngLats.length] = lngLats[0].clone();
        }

        scan();
    }

    /**
     * 检测GIS相邻边界点连线是否跨180/-180度经线
     */
    private void scan()
    {
        this.movedLngLats = new LngLat[this.lngLats.length];
        this.movedLngLats[0] = this.lngLats[0].clone();
        int spanNum = 0;// 跨越的次数，向右+1，向左-1
        sphereSeqs.add(spanNum);
        for (int i = 1; i < this.lngLats.length; i++)
        {
            double detaLng = this.lngLats[i].getLng() - this.lngLats[i - 1].getLng();
            if (detaLng > LngLat.MAX_LNG)
            {
                spanNum--;
                sphereSeqs.add(spanNum);
            }
            if (detaLng < LngLat.MIN_LNG)
            {
                spanNum++;
                sphereSeqs.add(spanNum);
            }
            this.movedLngLats[i] = new LngLat(this.lngLats[i].getLng() + 2 * spanNum * LngLat.MAX_LNG,
                    this.lngLats[i].getLat());
        }
        this.spanAntimeridian = this.sphereSeqs.size() > 1;
    }

    /**
     * 返回包络边界的矩形，该矩形平行于经纬度
     * 
     * @return
     */
    public Point2D[] rectAngle()
    {
        double top = lngLats[0].getLat();
        double bottom = lngLats[0].getLat();
        double left = lngLats[0].getLng();
        double right = lngLats[0].getLng();

        for (int i = 0; i < this.lngLats.length; i++)
        {
            double lng = this.lngLats[i].getLng();
            double lat = this.lngLats[i].getLat();
            if (top < lat)
            {
                top = lat;
            }
            if (bottom > lat)
            {
                bottom = lat;
            }
            if (left > lng)
            {
                left = lng;
            }
            if (right < lng)
            {
                right = lng;
            }
        }

        if (spanAntimeridian)
        {
            left = LngLat.MIN_LNG;
            right = LngLat.MAX_LNG;
        }
        return new Point2D[] { new Point2D(left, bottom), new Point2D(right, top) };
    }

    /**
     * 某个点是否在边界内
     * 
     * @param lngLat
     * @return
     */
    public boolean covers(LngLat lngLat)
    {
        Point2D[] poly = new Point2D[this.movedLngLats.length - 1];
        for (int i = 0; i < this.movedLngLats.length - 1; i++)
        {
            poly[i] = new Point2D(this.movedLngLats[i]);
        }
        for (int sphereSeq : sphereSeqs)
        {
            Point2D p = new Point2D(lngLat.getLng() + sphereSeq * 2 * LngLat.MAX_LNG, lngLat.getLat());
            String coverType = Polyn2D.rayCasting(p, poly);
            if (!Polyn2D.OUT.equals(coverType))
            {
                return true;
            }
        }
        return false;
    }

    /**
     * 返回压缩后的另一个边界对象
     * 
     * @param rate
     *            压缩比例
     * @return
     */
    public LngLatBoundary compress(double rate)
    {
        this.size = this.lngLats.length - 1;
        List<Pair<LngLat, Double>> list = new ArrayList<>();
        long start = System.currentTimeMillis();
        for (int i = 0; i < this.size; i++)
        {
            Pair<LngLat, Double> pair = new Pair<LngLat, Double>(this.lngLats[i], Sphere
                    .solidAngle(this.lngLats[(i - 1 + size) % size], this.lngLats[i], this.lngLats[(i + 1) % size]));
            list.add(pair);
        }
        while (list.size() > size * rate)
        {
            int minIndex = minAreaIndexOf(list);
            Pair<LngLat, Double> prepre = list.get((minIndex - 2 + list.size()) % list.size());
            Pair<LngLat, Double> pre = list.get((minIndex - 1 + list.size()) % list.size());
            Pair<LngLat, Double> next = list.get((minIndex + 1) % list.size());
            Pair<LngLat, Double> nextnext = list.get((minIndex + 2) % list.size());
            // 先设置需要删除的点旁边的两个点对应的三角形的面积
            pre.setValue(Sphere.solidAngle(prepre.getKey(), pre.getKey(), next.getKey()));
            next.setValue(Sphere.solidAngle(pre.getKey(), next.getKey(), nextnext.getKey()));
            // 再删除需要删除的点
            list.remove(minIndex);
        }
        long end = System.currentTimeMillis();
        System.out.println("foreach cost: " + (end - start));
        LngLat[] result = new LngLat[list.size()];
        return new LngLatBoundary(list.stream().map(Pair::getKey).collect(Collectors.toList()).toArray(result));
    }

    private int minAreaIndexOf(List<Pair<LngLat, Double>> ring)
    {
        int minIndex = 0;
        Pair<LngLat, Double> node = ring.get(0);
        double minArea = node.getValue();
        Iterator<Pair<LngLat, Double>> iter = ring.iterator();
        int i = 0;
        while (iter.hasNext())
        {
            node = iter.next();// 不能通过get方式获取，很耗时间
            if (minArea > node.getValue())
            {
                minArea = node.getValue();
                minIndex = i;
            }
            i++;
        }
        return minIndex;
    }

    /**
     * 返回压缩后的另一个边界对象
     * 
     * @param limit
     *            需要压缩至的边界点数
     * @return 压缩后的另一个边界对象
     */
    public LngLatBoundary compress(int limit)
    {

        // TODO
        return null;
    }

    /**
     * Test Main
     * 
     * @param args
     */
    public static void main(String[] args)
    {
        try
        {
            LngLatBoundary llb = new LngLatBoundary(
                    LngLatBoundary.parse("-170,-1;0,-1;170,1;-175,1;-175,2;170,2;0,1;-170,1;"));
            System.out.println(llb);
            System.out.println(llb.moved2String());
            // Point2D[] ps1 = llb.rectAngle();
            LngLatBoundary llb1 = new LngLatBoundary(LngLatBoundary.parse("-1,-1;1,-1;1,1;-1,1;"));
            Point2D[] ps = llb1.rectAngle();
            System.out.println(llb1.covers(new LngLat(0.99999, -0.999999)));
            System.out.println(ps[0]);
            System.out.println(ps[1]);
        }
        catch (LngLatBoundaryFormatException e)
        {
            e.printStackTrace();
        }
    }

    public static LngLat[] parse(String border) throws LngLatBoundaryFormatException
    {
        if (StringUtils.isEmpty(border))
        {
            throw new LngLatBoundaryFormatException("空");
        }
        String[] lngLatStrArr = border.split(LNGLAT_SPLITTER);
        if (lngLatStrArr.length < 3)
        {
            throw new LngLatBoundaryFormatException("边界点个数小于3");
        }

        LngLat[] arr = new LngLat[lngLatStrArr.length];
        try
        {
            for (int i = 0; i < lngLatStrArr.length; i++)
            {
                LngLat lngLat = LngLat.parse(lngLatStrArr[i]);
                arr[i] = lngLat;
            }
        }
        catch (LngLatFormatException e)
        {
            throw new LngLatBoundaryFormatException("边界中有异常边界点");
        }

        return arr;
    }

    public int getSize()
    {
        return size;
    }

    public void setSize(int size)
    {
        this.size = size;
    }

    public double getArea()
    {
        return area;
    }

    public void setArea(double area)
    {
        this.area = area;
    }

    public double getLength()
    {
        return length;
    }

    public void setLength(double length)
    {
        this.length = length;
    }

    public boolean isSpanAntimeridian()
    {
        return spanAntimeridian;
    }

    public void setSpanAntimeridian(boolean spanAntimeridian)
    {
        this.spanAntimeridian = spanAntimeridian;
    }

    public LngLat[] getLngLats()
    {
        return lngLats;
    }

    public void setLngLats(LngLat[] lngLats)
    {
        this.lngLats = lngLats;
    }

    @Override
    public String toString()
    {
        StringBuffer sb = new StringBuffer();
        for (LngLat ll : lngLats)
        {
            sb.append(ll.toString()).append(LNGLAT_SPLITTER);
        }
        return sb.toString();
    }

    public String moved2String()
    {
        StringBuffer sb = new StringBuffer();
        for (LngLat ll : this.movedLngLats)
        {
            sb.append(ll.toString()).append(LNGLAT_SPLITTER);
        }
        return sb.toString();
    }
    
}
