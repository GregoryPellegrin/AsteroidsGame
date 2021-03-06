/*
 * Gregory Pellegrin
 * pellegrin.gregory.work@gmail.com
 */

package Util;

import java.io.Serializable;

public class Vector implements Serializable
{
	public double x;
	public double y;
	
	public Vector (double angle)
	{
		this.x = Math.cos(angle);
		this.y = Math.sin(angle);
	}

	public Vector (double x, double y)
	{
		this.x = x;
		this.y = y;
	}

	public Vector (Vector vector)
	{
		this.x = vector.x;
		this.y = vector.y;
	}

	public Vector set (double x, double y)
	{
		this.x = x;
		this.y = y;
		
		return this;
	}

	public Vector add (Vector vector)
	{
		this.x = this.x + vector.x;
		this.y = this.y + vector.y;
		
		return this;
	}

	public Vector scale (double scalar)
	{
		this.x = this.x * scalar;
		this.y = this.y * scalar;
		
		return this;
	}
	
	public Vector normalize ()
	{
		double length = this.getLengthSquared();
		
		if ((length != 0.0f) && (length != 1.0f))
		{
			length = Math.sqrt(length);
			
			this.x = this.x / length;
			this.y = this.y / length;
		}
		
		return this;
	}

	public double getLengthSquared ()
	{
		return ((x * x) + (y * y));
	}

	public double getDistanceToSquared (Vector vector)
	{
		double dx = this.x - vector.x;
		double dy = this.y - vector.y;
		
		return ((dx * dx) + (dy * dy));
	}
}