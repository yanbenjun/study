package com.yanbenjun.map.geometry;

import com.yanbenjun.map.LngLat;

public class Sphere
{
    private double r;

    private double theta;

    private double phi;

    /**
     * 公式：arc cos[cosβ1cosβ2cos（α1-α2）+sinβ1sinβ2] β维度 α经度
     * 
     * @param p1
     * @param p2
     * @return p1点与圆心的半径；p2点与圆心的半径，的夹角
     */
    public static double angle(LngLat p1, LngLat p2)
    {
        double x1 = p1.getLatRd() == null ? Math.toRadians(p1.getLat()) : p1.getLatRd();
        double y1 = p1.getLngRd() == null ? Math.toRadians(p1.getLng()) : p1.getLngRd();
        double x2 = p2.getLatRd() == null ? Math.toRadians(p2.getLat()) : p2.getLatRd();
        double y2 = p2.getLngRd() == null ? Math.toRadians(p2.getLng()) : p2.getLngRd();
        return GeometryConstants.EARTH_RADIUS
                * Math.acos(Math.sin(x1) * Math.sin(x2) + Math.cos(x1) * Math.cos(x2) * Math.cos(y1 - y2));
    }

    /**
     * 球面上三点连接球心构成的立体角大小
     * 
     * @param p1
     * @param p2
     * @return 立体角大小
     */
    public static double solidAngle(LngLat a, LngLat b, LngLat c)
    {
        return Math.abs(signedSolidAngle(a, b, c));
    }

    /**
     * 球面上三点连接球心构成的立体角大小
     * 
     * @param p1
     * @param p2
     * @return 立体角大小
     */
    private static double signedSolidAngle(LngLat a, LngLat b, LngLat c)
    {
        LngLat[] llArr = new LngLat[] { a, b, c, a };
        double[] angleArr = new double[3];
        double f = 0;

        for (int i = 0; i < llArr.length - 1; i++)
        {
            angleArr[i] = angle(llArr[i], llArr[i + 1]);
            f += angleArr[i];
        }
        f /= 2;
        double g = Math.tan(f / 2);
        for (int j = 0; j < llArr.length - 1; j++)
        {
            g *= Math.tan((f - angleArr[j]) / 2);
        }
        return 4 * Math.atan(Math.sqrt(Math.abs(g)));
    }

    /**
     * 三个点构成的球面三角形立体角的符号
     * 
     * @param a
     * @param b
     * @param c
     * @return 符号
     */
    public static double signedOf(LngLat a, LngLat b, LngLat c)
    {
        LngLat[] d = new LngLat[] { a, b, c };
        double[][] e = new double[3][3];
        for (int i = 0; i < 3; i++)
        {
            LngLat f = d[i];
            double f1 = f.getLatRd() == null ? Math.toRadians(f.getLat()) : f.getLatRd();
            double f2 = f.getLngRd() == null ? Math.toRadians(f.getLng()) : f.getLngRd();
            e[i][0] = Math.cos(f1) * Math.cos(f2);
            e[i][1] = Math.cos(f1) * Math.sin(f2);
            e[i][2] = Math.sin(f1);
        }
        return 0 < e[0][0] * e[1][1] * e[2][2] + e[1][0] * e[2][1] * e[0][2] + e[2][0] * e[0][1] * e[1][2]
                - e[0][0] * e[2][1] * e[1][2] - e[1][0] * e[0][1] * e[2][2] - e[2][0] * e[1][1] * e[0][2] ? 1 : -1;
    }

    /**
     * 球面多边形的立体角(绝对值) 求面积用
     * 
     * @param lngLats
     * @return
     */
    public static double solidAngle(LngLat[] lngLats)
    {
        if (lngLats.length < 3)
        {
            return 0.0;
        }
        return Math.abs(signedSolidAngle(lngLats));
    }

    /**
     * 球面多边形的立体角(带符号)
     * 
     * @param lngLats
     * @return
     */
    private static double signedSolidAngle(LngLat[] lngLats)
    {
        double g = lngLats.length - 1;
        double e = 0;
        LngLat d = lngLats[0];
        for (int i = 1; i < g; i++)
        {
            e += signedSolidAngle(d, lngLats[i], lngLats[i + 1]) * signedOf(d, lngLats[i], lngLats[i + 1]);
        }
        return e;
    }

    public double getR()
    {
        return r;
    }

    public void setR(double r)
    {
        this.r = r;
    }

    public double getTheta()
    {
        return theta;
    }

    public void setTheta(double theta)
    {
        this.theta = theta;
    }

    public double getPhi()
    {
        return phi;
    }

    public void setPhi(double phi)
    {
        this.phi = phi;
    }
}
