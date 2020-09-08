package com.yanbenjun.map.geometry;

import com.yanbenjun.map.LngLat;

public class Point2D {
    double x;

    double y;

	public Point2D(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public Point2D(LngLat lngLat) {
		this.x = lngLat.getLng();
		this.y = lngLat.getLat();
	}

	@Override
	public String toString() {
		return "(" + this.x + ", " + this.y + ")";
	}
}
