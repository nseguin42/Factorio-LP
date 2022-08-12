package com.FactorioLP;

import com.google.ortools.linearsolver.MPVariable;
public class Producer
{
	static final int PIPE_SPEED = 3000;
	public static final Producer PIPE = new Producer(
			PIPE_SPEED,
			0,
			"Pipe"
	);
	static final Producer BLUE_BELT = new Producer(
			PIPE_SPEED,
			0,
			"Red Belt"
	);
	static final Producer ELECTRIC_FURNACE = new Producer(
			2,
			2,
			"Electric Furnace"
	);
	static final Producer CHEMICAL_PLANT = new Producer(
			1,
			3,
			"Chemical Plant"
	);
	static final Producer ASSEMBLY_MACHINE_3 = new Producer(
			1.25,
			4,
			"Assembly Machine 3"
	);
	static final int BELT_SPEED = 45;
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
