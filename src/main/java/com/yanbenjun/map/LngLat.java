package com.yanbenjun.map;

import java.util.Objects;

import com.yanbenjun.map.geometry.GeometryConstants;

public class LngLat implements Cloneable
{
    public static final int MAX_LNG = 180;

    public static final int MIN_LNG = -180;

    public static final int MAX_LAT = 90;

    public static final int MIN_LAT = -90;

    public static final String DEFAULT_DELIMITER = ",";

    private double lng;

    private double lat;

    private Double lngRd;

    private Double latRd;

    private Double sinLatRd;

    private Double cosLatRd;

    private Double sinLngRd;

    private Double cosLngRd;

    LngLat(double lng, double lat)
    {
        this.lng = lng;
        this.lat = lat;
        this.lngRd = Math.toRadians(lng);
        this.latRd = Math.toRadians(lat);
        this.setSinLatRd(Math.sin(latRd));
        this.setCosLatRd(Math.cos(latRd));
        this.sinLngRd = Math.sin(lngRd);
        this.cosLngRd = Math.cos(lngRd);
    }

    LngLat(double lng, double lat, boolean extend)
    {
        this(lng, lat);
        if (extend)
        {
            this.lngRd = Math.toRadians(lng);
            this.latRd = Math.toRadians(lat);
            this.setSinLatRd(Math.sin(latRd));
            this.setCosLatRd(Math.cos(latRd));
            this.sinLngRd = Math.sin(lngRd);
            this.cosLngRd = Math.cos(lngRd);
        }
    }

    public LngLat(String lngLatStr) throws LngLatFormatException
    {
        LngLat tLngLat = parse(lngLatStr, DEFAULT_DELIMITER);
        this.lng = tLngLat.getLng();
        this.lat = tLngLat.getLat();
    }

    public LngLat(String lngLatStr, String delimiter) throws LngLatFormatException
    {
        LngLat tLngLat = parse(lngLatStr, delimiter);
        this.lng = tLngLat.getLng();
        this.lat = tLngLat.getLat();
    }
    
    @Override
    public LngLat clone()
    {
    	try {
			return (LngLat) super.clone();
		} catch (CloneNotSupportedException e) {
		}
    	return null;
    }
    
    /**
     * 比较两个经纬度点是否在一个位置
     * @param another
     * @return
     */
    public boolean same(LngLat another)
    {
    	return Double.valueOf(lng).equals(Double.valueOf(another.lng)) && Double.valueOf(lat).equals(Double.valueOf(another.lat));
    }
    
    /**
     * 比较两个经纬度点是否在一个小正方形内
     * @param another
     * @return
     */
    public boolean same(LngLat another, double threhold)
    {
    	return Math.abs(this.lng - another.lng) < threhold && Math.abs(this.lat - another.lat) < threhold;
    }
    
    public double distance(LngLat another)
    {
        return GeometryConstants.EARTH_RADIUS * angle(another);
    }

    public double distance1(LngLat another)
    {
        return GeometryConstants.EARTH_RADIUS * angle1(another);
    }

    public double distance2(LngLat another)
    {
        return GeometryConstants.EARTH_RADIUS * angle2(another);
    }

    /**
     * 球面距离计算公式：d(x1,y1,x2,y2)=r*arccos(sin(x1)*sin(x2)+cos(x1)*cos(x2)*cos(y1-
     * y2))，其中，x1，y1是纬度\经度的弧度单位，r为地球半径
     * 
     * @param anotherLngLat
     * @return 夹角
     */
    private double angle(LngLat anotherLngLat)
    {
        Objects.requireNonNull(anotherLngLat);
        double x1 = Math.toRadians(this.lat);
        double y1 = Math.toRadians(this.lng);
        double x2 = Math.toRadians(anotherLngLat.lat);
        double y2 = Math.toRadians(anotherLngLat.lng);
        return GeometryConstants.EARTH_RADIUS
                * Math.acos(Math.sin(x1) * Math.sin(x2) + Math.cos(x1) * Math.cos(x2) * Math.cos(y1 - y2));
    }

    /**
     * 球面距离计算公式：d(x1,y1,x2,y2)=r*arccos(sin(x1)*sin(x2)+cos(x1)*cos(x2)*cos(y1-
     * y2))，其中，x1，y1是纬度\经度的弧度单位，r为地球半径 缓存了中间结果
     * 
     * @param anotherLngLat
     * @return
     */
    private double angle1(LngLat anotherLngLat)
    {
        Objects.requireNonNull(anotherLngLat);
        return Math.acos(this.sinLatRd * anotherLngLat.sinLatRd
                + this.cosLatRd * anotherLngLat.cosLatRd * (Math.cos(this.lngRd - anotherLngLat.lngRd)));
    }

    /**
     * 球面距离计算公式：d(x1,y1,x2,y2)=r*arccos(sin(x1)*sin(x2)+cos(x1)*cos(x2)*cos(y1-
     * y2))，其中，x1，y1是纬度\经度的弧度单位，r为地球半径 缓存了中间结果
     * 
     * @param anotherLngLat
     * @return
     */
    private double angle2(LngLat anotherLngLat)
    {
        Objects.requireNonNull(anotherLngLat);
        return Math.acos(this.sinLatRd * anotherLngLat.sinLatRd + this.cosLatRd * anotherLngLat.cosLatRd
                * (this.cosLngRd * anotherLngLat.cosLngRd + this.sinLngRd * anotherLngLat.sinLngRd));
    }

    /**
     * 生成在一个矩形区域内的随机经纬度点
     * 
     * @param min
     *            矩形左下角
     * @param max
     *            矩形右上角
     * @return 随机经纬度点
     */
    public static LngLat randLngLat(LngLat min, LngLat max)
    {
        double minX = 0;
        double minY = 0;
        double maxX = 0;
        double maxY = 0;
        if (min == null)
        {
            minX = -180;
            minY = -90;
        }
        else
        {
            minX = min.getLng();
            minY = min.getLat();
        }
        if (max == null)
        {
            maxX = 180;
            maxY = 90;
        }
        else
        {
            maxX = max.getLng();
            maxY = max.getLat();
        }
        double detaX = maxX - minX;
        double detaY = maxY - minY;
        double ln = Math.random() * detaX + minX;
        double la = Math.random() * detaY + minY;
        return new LngLat(ln, la, true);
    }

    public static LngLat parse(String arg) throws LngLatFormatException
    {
        return parse(arg, DEFAULT_DELIMITER);
    }

    public static LngLat parse(String arg, String delimiter) throws LngLatFormatException
    {
        Objects.requireNonNull(arg);
        String[] lngLatArr = arg.split(delimiter);
        if (lngLatArr.length < 2)
        {
            throw new LngLatFormatException(
                    "For input string: \"" + arg + "\" can't be splitted with delimiter: \"" + delimiter + "\"");
        }
        if (lngLatArr.length > 2)
        {
            throw new LngLatFormatException("For input string: \"" + arg
                    + "\" be splitted larger than lng,lat's size with delimiter: \"" + delimiter + "\"");
        }
        double tlng = 0, tlat = 0;
        String lngError = null;
        String latError = null;
        try
        {
            tlng = parseLng(lngLatArr[0]);
        }
        catch (LngLatFormatException e)
        {
            lngError = e.getErrorInfo();
        }
        try
        {
            tlat = parseLat(lngLatArr[1]);
        }
        catch (LngLatFormatException e)
        {
            latError = e.getErrorInfo();
        }
        if (lngError != null || latError != null)
        {
            throw new LngLatFormatException(getErrorInfo(arg, delimiter, lngError, latError));
        }
        return new LngLat(tlng, tlat);
    }

    public static double parseLng(String lngStr) throws LngLatFormatException
    {
        Objects.requireNonNull(lngStr);
        String trimLng = lngStr.trim();
        double tlng = 0;
        try
        {
            tlng = Double.parseDouble(trimLng);
        }
        catch (NumberFormatException e)
        {
            throw new LngLatFormatException("The lng string: \"" + trimLng + "\" is not a number.");
        }
        if (tlng > MAX_LNG || tlng < MIN_LNG)
        {
            throw new LngLatFormatException(
                    "The lng number: \"" + tlng + "\" is not between " + MIN_LNG + " and " + MAX_LNG + ".");
        }
        return tlng;
    }

    public static double parseLat(String latStr) throws LngLatFormatException
    {
        Objects.requireNonNull(latStr);
        String trimLat = latStr.trim();
        double tlat = 0;
        try
        {
            tlat = Double.parseDouble(trimLat);
        }
        catch (NumberFormatException e)
        {
            throw new LngLatFormatException("The lat string: \"" + trimLat + "\" is not a number.");
        }
        if (tlat > MAX_LAT || tlat < MIN_LAT)
        {
            throw new LngLatFormatException(
                    "The lat number: \"" + tlat + "\" is not between " + MIN_LAT + " and " + MAX_LAT + ".");
        }
        return tlat;
    }

    private static String getErrorInfo(String arg, String delimiter, String lngError, String latError)
    {
        StringBuffer sb = new StringBuffer(
                "For input string: \"" + arg + "\", after splitted with delimiter: \"" + delimiter + "\". ");
        if (lngError != null)
        {
            sb.append(" ").append(lngError);
        }
        if (latError != null)
        {
            sb.append(" ").append(latError);
        }
        return sb.toString();
    }
    
    public double getLng()
    {
        return lng;
    }

    public void setLng(double lng)
    {
        this.lng = lng;
    }

    public double getLat()
    {
        return lat;
    }

    public void setLat(double lat)
    {
        this.lat = lat;
    }

    public Double getLngRd()
    {
        return lngRd;
    }

    public void setLngRd(Double lngRd)
    {
        this.lngRd = lngRd;
    }

    public Double getLatRd()
    {
        return latRd;
    }

    public void setLatRd(Double latRd)
    {
        this.latRd = latRd;
    }

    public String toString()
    {
        return this.lng + "," + this.lat;
    }

    public static void main(String[] args) throws Exception
    {
        try
        {
            Double ss = new Double("111s");
            System.out.println(ss);
            Double.parseDouble("fdafs");
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        try
        {

            LngLat.parse("s,220");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public Double getSinLatRd()
    {
        return sinLatRd;
    }

    public void setSinLatRd(Double sinLatRd)
    {
        this.sinLatRd = sinLatRd;
    }

    public Double getCosLatRd()
    {
        return cosLatRd;
    }

    public void setCosLatRd(Double cosLatRd)
    {
        this.cosLatRd = cosLatRd;
    }

    public Double getSinLngRd()
    {
        return sinLngRd;
    }

    public void setSinLngRd(Double sinLngRd)
    {
        this.sinLngRd = sinLngRd;
    }

    public Double getCosLngRd()
    {
        return cosLngRd;
    }

    public void setCosLngRd(Double cosLngRd)
    {
        this.cosLngRd = cosLngRd;
    }
}
