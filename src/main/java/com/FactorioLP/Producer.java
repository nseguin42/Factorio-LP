package com.FactorioLP;

public enum Producer
{
	PIPE(3000, 0, "Pipe"),
	BLUE_BELT(45, 0, "Blue Belt"),
	ELECTRIC_FURNACE(2, 2, "Electric Furnace"),
	CHEMICAL_PLANT(1, 3, "Chemical Plant"),
	ASSEMBLY_MACHINE_3(1.25, 4, "Assembly Machine 3");
	static final int BELT_SPEED = 45;
	private static final double SPEED_MODULE_MULTIPLIER = 0.5;
	final double speed;
	final int slots;
	final String name;

	Producer(double speed, int slots, String name)
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
