package com.FactorioLP;

import com.google.ortools.linearsolver.MPVariable;
public class Producer
{
	private static final double SPEED_MODULE_MULTIPLIER = 0.5;
	 double speed;
	int slots;
	MPVariable variable;
	String name;

	public Producer(double speed, int slots, String name)
	{
		this.speed = speed;
		this.slots = slots;
		this.name = name;
	}

	public double getSpeed()
	{
		return speed * (1 + slots * SPEED_MODULE_MULTIPLIER);
	}

	public String getName()
	{
		return this.name;
	}
}
