package com.loris.base.analysis.base;

public interface Point
{
	/**
	 * Compute the distance of the point.
	 * 
	 * @param p
	 * @return
	 */
	double getDistance(Point p);
	
	/**
	 * Add the point.
	 * 
	 * @param p
	 */
	void addPoint(Point p);
	
	/**
	 * The point multiply a value.
	 * 
	 * 
	 * @param value
	 */
	void multiply(double value);
}
