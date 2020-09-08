package com.yanbenjun.map;

public class LngLatBoundaryFormatException extends Exception 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7655613680332551063L;
	
	public LngLatBoundaryFormatException()
	{
		
	}
	
	public LngLatBoundaryFormatException(String error)
	{
		super(error);
	}

}
