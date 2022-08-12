package com.FactorioLP;

import com.google.ortools.linearsolver.MPVariable;
import java.util.Map;
public class Item
{
	static final Item COPPER_PLATE = new Item(
			"Copper Plate",
			null,
			Producer.BELT_SPEED,
			1,
			Producer.BLUE_BELT
	);
	static final Item IRON_PLATE = new Item(
			"Iron Plate",
			null,
			Producer.BELT_SPEED,
			1,
			Producer.BLUE_BELT
	);
	static final Item COAL = new Item(
			"Coal",
			null,
			1,
			0,
			Producer.BLUE_BELT
	);
	static final Item STONE = new Item(
			"Stone",
			null,
			10,
			0,
			Producer.BLUE_BELT
	);
	static final Item PETROLEUM_GAS = new Item(
			"Petroleum Gas",
			null,
			1,
			0,
			Producer.PIPE
	);
	static final Item WATER = new Item(
			"Water",
			null,
			1,
			0,
			Producer.PIPE
	);
	static final Item LUBRICANT = new Item(
			"Lubricant",
			null,
			1,
			0,
			Producer.PIPE
	);
	private static final Item COPPER_CABLE = new Item(
			"Copper Cable",
			Map.of(
					COPPER_PLATE,
					1
			),
			2,
			0.5,
			Producer.ASSEMBLY_MACHINE_3
	);
	private static final Item ELECTRONIC_CIRCUIT = new Item(
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
	);
	private static final Item IRON_STICK = new Item(
			"Iron Stick",
			Map.of(
					IRON_PLATE,
					1
			),
			2,
			0.5,
			Producer.ASSEMBLY_MACHINE_3
	);
	private static final Item PIPE = new Item(
			"Pipe",
			Map.of(
					IRON_PLATE,
					1
			),
			1,
			0.5,
			Producer.ASSEMBLY_MACHINE_3
	);
	private static final Item STEEL = new Item(
			"Steel",
			Map.of(
					IRON_PLATE,
					5
			),
			1,
			16,
			Producer.ELECTRIC_FURNACE
	);
	private static final Item RAIL = new Item(
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
	);
	private static final Item FIREARM_MAGAZINE = new Item(
			"Firearm Magazine",
			Map.of(
					IRON_PLATE,
					4
			),
			1,
			1,
			Producer.ASSEMBLY_MACHINE_3
	);
	private static final Item PIERCING_ROUNDS_MAGAZINE = new Item(
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
	);
	private static final Item IRON_GEAR_WHEEL = new Item(
			"Iron Gear Wheel",
			Map.of(
					IRON_PLATE,
					2
			),
			1,
			0.5,
			Producer.ASSEMBLY_MACHINE_3
	);
	private static final Item ENGINE_UNIT = new Item(
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
	);
	private static final Item ELECTRIC_ENGINE_UNIT = new Item(
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
	);
	private static final Item INSERTER = new Item(
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
	);
	private static final Item TRANSPORT_BELT = new Item(
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
	);
	static final Item LOGISTIC_SCIENCE_PACK = new Item(
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
	);
	static final Item AUTOMATION_SCIENCE_PACK = new Item(
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
	);
	private static final Item GRENADE = new Item(
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
	);
	private static final Item STONE_BRICK = new Item(
			"Stone Brick",
			Map.of(
					STONE,
					2
			),
			1,
			3.2,
			Producer.ELECTRIC_FURNACE
	);
	private static final Item WALL = new Item(
			"Wall",
			Map.of(
					STONE_BRICK,
					5
			),
			1,
			0.5,
			Producer.ASSEMBLY_MACHINE_3
	);
	static final Item MILITARY_SCIENCE_PACK = new Item(
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
	);
	private static final Item PLASTIC_BAR = new Item(
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
	);
	private static final Item LOW_DENSITY_STRUCTURE = new Item(
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
	);
	private static final Item ADVANCED_CIRCUIT = new Item(
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
	);
	private static final Item PRODUCTIVITY_MODULE = new Item(
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
	);
	private static final Item ELECTRIC_FURNACE = new Item(
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
	);
	static final Item PRODUCTION_SCIENCE_PACK = new Item(
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
	);
	private static final Item SULFUR = new Item(
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
	);
	private static final Item SULFURIC_ACID = new Item(
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
	);
	private static final Item BATTERY = new Item(
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
	);
	private static final Item FLYING_ROBOT_FRAME = new Item(
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
	);
	private static final Item PROCESSING_UNIT = new Item(
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
	);
	static final Item UTILITY_SCIENCE_PACK = new Item(
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
	);
	static final Item CHEMICAL_SCIENCE_PACK = new Item(
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

	public Item(
			String name, Map<Item, Integer> ingredients, int quantity, double time,
			Producer producer
	)
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
