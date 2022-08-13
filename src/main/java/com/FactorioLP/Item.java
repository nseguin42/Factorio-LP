package com.FactorioLP;

import com.google.ortools.linearsolver.MPVariable;
import java.util.Map;
public enum Item
{
	COPPER_PLATE(
			"Copper Plate",
			null,
			Producer.BELT_SPEED,
			1,
			Producer.BLUE_BELT
	),
	IRON_PLATE(
			"Iron Plate",
			null,
			Producer.BELT_SPEED,
			1,
			Producer.BLUE_BELT
	),
	COAL(
			"Coal",
			null,
			1,
			0,
			Producer.BLUE_BELT
	),
	STONE(
			"Stone",
			null,
			10,
			0,
			Producer.BLUE_BELT
	),
	PETROLEUM_GAS(
			"Petroleum Gas",
			null,
			1,
			0,
			Producer.PIPE
	),
	WATER(
			"Water",
			null,
			1,
			0,
			Producer.PIPE
	),
	LUBRICANT(
			"Lubricant",
			null,
			1,
			0,
			Producer.PIPE
	),
	COPPER_CABLE(
			"Copper Cable",
			Map.of(
					COPPER_PLATE,
					1
			),
			2,
			0.5,
			Producer.ASSEMBLY_MACHINE_3
	),
	ELECTRONIC_CIRCUIT(
			"Electronic Circuit",
			Map.of(
					COPPER_CABLE,
					3,
					IRON_PLATE,
					1
			),
			1,
			0.5,
			Producer.ASSEMBLY_MACHINE_3
	),
	IRON_STICK(
			"Iron Stick",
			Map.of(
					IRON_PLATE,
					1
			),
			2,
			0.5,
			Producer.ASSEMBLY_MACHINE_3
	),
	PIPE(
			"Pipe",
			Map.of(
					IRON_PLATE,
					1
			),
			1,
			0.5,
			Producer.ASSEMBLY_MACHINE_3
	),
	STEEL(
			"Steel",
			Map.of(
					IRON_PLATE,
					5
			),
			1,
			16,
			Producer.ELECTRIC_FURNACE
	),
	RAIL(
			"Rail",
			Map.of(
					IRON_STICK,
					1,
					STEEL,
					1,
					STONE,
					1
			),
			2,
			0.5,
			Producer.ASSEMBLY_MACHINE_3
	),
	FIREARM_MAGAZINE(
			"Firearm Magazine",
			Map.of(
					IRON_PLATE,
					4
			),
			1,
			1,
			Producer.ASSEMBLY_MACHINE_3
	),
	PIERCING_ROUNDS_MAGAZINE(
			"Piercing Rounds Magazine",
			Map.of(
					COPPER_PLATE,
					5,
					FIREARM_MAGAZINE,
					1,
					STEEL,
					1
			),
			1,
			3,
			Producer.ASSEMBLY_MACHINE_3
	),
	IRON_GEAR_WHEEL(
			"Iron Gear Wheel",
			Map.of(
					IRON_PLATE,
					2
			),
			1,
			0.5,
			Producer.ASSEMBLY_MACHINE_3
	),
	ENGINE_UNIT(
			"Engine Unit",
			Map.of(
					IRON_GEAR_WHEEL,
					1,
					PIPE,
					2,
					STEEL,
					1
			),
			1,
			10,
			Producer.ASSEMBLY_MACHINE_3
	),
	ELECTRIC_ENGINE_UNIT(
			"Electric Engine Unit",
			Map.of(
					ELECTRONIC_CIRCUIT,
					2,
					ENGINE_UNIT,
					1,
					LUBRICANT,
					15
			),
			1,
			10,
			Producer.ASSEMBLY_MACHINE_3
	),
	INSERTER(
			"Inserter",
			Map.of(
					ELECTRONIC_CIRCUIT,
					1,
					IRON_GEAR_WHEEL,
					1
			),
			1,
			0.5,
			Producer.ASSEMBLY_MACHINE_3
	),
	TRANSPORT_BELT(
			"Transport Belt",
			Map.of(
					IRON_GEAR_WHEEL,
					1,
					IRON_PLATE,
					1
			),
			2,
			0.5,
			Producer.ASSEMBLY_MACHINE_3
	),
	LOGISTIC_SCIENCE_PACK(
			"Logistic Science Pack",
			Map.of(
					TRANSPORT_BELT,
					1,
					INSERTER,
					1
			),
			1,
			6,
			Producer.ASSEMBLY_MACHINE_3
	),
	AUTOMATION_SCIENCE_PACK(
			"Automation Science Pack",
			Map.of(
					IRON_GEAR_WHEEL,
					1,
					COPPER_PLATE,
					1
			),
			1,
			5,
			Producer.ASSEMBLY_MACHINE_3
	),
	GRENADE(
			"Grenade",
			Map.of(
					COAL,
					10,
					IRON_PLATE,
					5
			),
			1,
			8,
			Producer.ASSEMBLY_MACHINE_3
	),
	STONE_BRICK(
			"Stone Brick",
			Map.of(
					STONE,
					2
			),
			1,
			3.2,
			Producer.ELECTRIC_FURNACE
	),
	WALL(
			"Wall",
			Map.of(
					STONE_BRICK,
					5
			),
			1,
			0.5,
			Producer.ASSEMBLY_MACHINE_3
	),
	MILITARY_SCIENCE_PACK(
			"Military Science Pack",
			Map.of(
					PIERCING_ROUNDS_MAGAZINE,
					1,
					GRENADE,
					1,
					WALL,
					2
			),
			2,
			10,
			Producer.ASSEMBLY_MACHINE_3
	),
	PLASTIC_BAR(
			"Plastic Bar",
			Map.of(
					COAL,
					1,
					PETROLEUM_GAS,
					20
			),
			2,
			1,
			Producer.CHEMICAL_PLANT
	),
	LOW_DENSITY_STRUCTURE(
			"Low Density Structure",
			Map.of(
					COPPER_PLATE,
					20,
					PLASTIC_BAR,
					5,
					STEEL,
					2
			),
			1,
			20,
			Producer.ASSEMBLY_MACHINE_3
	),
	ADVANCED_CIRCUIT(
			"Advanced Circuit",
			Map.of(
					COPPER_CABLE,
					4,
					ELECTRONIC_CIRCUIT,
					2,
					PLASTIC_BAR,
					2
			),
			1,
			6,
			Producer.ASSEMBLY_MACHINE_3
	),
	PRODUCTIVITY_MODULE(
			"Productivity Module",
			Map.of(
					ADVANCED_CIRCUIT,
					5,
					ELECTRONIC_CIRCUIT,
					5
			),
			1,
			15,
			Producer.ASSEMBLY_MACHINE_3
	),
	ELECTRIC_FURNACE(
			"Electric Furnace",
			Map.of(
					ADVANCED_CIRCUIT,
					5,
					STEEL,
					10,
					STONE_BRICK,
					10
			),
			1,
			5,
			Producer.ASSEMBLY_MACHINE_3
	),
	PRODUCTION_SCIENCE_PACK(
			"Production Science Pack",
			Map.of(
					RAIL,
					30,
					ELECTRIC_FURNACE,
					1,
					PRODUCTIVITY_MODULE,
					1
			),
			3,
			21,
			Producer.ASSEMBLY_MACHINE_3
	),
	SULFUR(
			"Sulfur",
			Map.of(
					PETROLEUM_GAS,
					30,
					WATER,
					30
			),
			2,
			1,
			Producer.CHEMICAL_PLANT
	),
	SULFURIC_ACID(
			"Sulfuric Acid",
			Map.of(
					IRON_PLATE,
					1,
					SULFUR,
					5,
					WATER,
					100
			),
			50,
			1,
			Producer.CHEMICAL_PLANT
	),
	BATTERY(
			"Battery",
			Map.of(
					COPPER_PLATE,
					1,
					IRON_PLATE,
					1,
					SULFURIC_ACID,
					20
			),
			1,
			4,
			Producer.CHEMICAL_PLANT
	),
	FLYING_ROBOT_FRAME(
			"Flying Robot Frame",
			Map.of(
					BATTERY,
					2,
					ELECTRIC_ENGINE_UNIT,
					1,
					ELECTRONIC_CIRCUIT,
					3,
					STEEL,
					1
			),
			1,
			20,
			Producer.ASSEMBLY_MACHINE_3
	),
	PROCESSING_UNIT(
			"Processing Unit",
			Map.of(
					ADVANCED_CIRCUIT,
					2,
					ELECTRONIC_CIRCUIT,
					20,
					SULFURIC_ACID,
					5
			),
			1,
			10,
			Producer.ASSEMBLY_MACHINE_3
	),
	UTILITY_SCIENCE_PACK(
			"Utility Science Pack",
			Map.of(
					PROCESSING_UNIT,
					2,
					FLYING_ROBOT_FRAME,
					1,
					LOW_DENSITY_STRUCTURE,
					3
			),
			3,
			21,
			Producer.ASSEMBLY_MACHINE_3
	),
	CHEMICAL_SCIENCE_PACK(
			"Chemical Science Pack",
			Map.of(
					SULFUR,
					1,
					ADVANCED_CIRCUIT,
					3,
					ENGINE_UNIT,
					2
			),
			2,
			24,
			Producer.ASSEMBLY_MACHINE_3
	);

	final private Map<Item, Integer> ingredients;
	final private double time;
	final private Producer producer;
	final private String name;
	final private int quantity;
	private MPVariable variable;
	private MPVariable numProducers;

	Item(String name, Map<Item, Integer> ingredients, int quantity, double time, Producer producer)
	{
		this.name = name;
		this.ingredients = ingredients;
		this.quantity = quantity;
		this.time = time;
		this.producer = producer;
	}

	public int getQuantity()
	{
		return this.quantity;
	}

	public double getTime()
	{
		return this.time;
	}

	public Producer getProducer()
	{
		return this.producer;
	}

	public Map<Item, Integer> getIngredients()
	{
		return this.ingredients;
	}

	public String getName()
	{
		return this.name;
	}

	public MPVariable getVariable()
	{
		return this.variable;
	}

	public void setVariable(MPVariable variable)
	{
		this.variable = variable;
	}

	public MPVariable getNumProducers()
	{
		return this.numProducers;
	}

	public void setNumProducers(MPVariable numProducers)
	{
		this.numProducers = numProducers;
	}
}
