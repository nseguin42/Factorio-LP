package com.FactorioLP;

import com.google.ortools.Loader;
import com.google.ortools.linearsolver.MPConstraint;
import com.google.ortools.linearsolver.MPObjective;
import com.google.ortools.linearsolver.MPSolver;
import com.google.ortools.linearsolver.MPVariable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

public final class FactorioLP
{
	private static final int BELT_SPEED = 30;
	// input rates
	private static final double IRON_PER_MINUTE = BELT_SPEED * 60 * 4;
	private static final double COPPER_PER_MINUTE = BELT_SPEED * 60 * 4;
	private static final double COAL_PER_MINUTE = BELT_SPEED * 60 * 2;
	private static final double STONE_PER_MINUTE = BELT_SPEED * 60 * 2;
	private static final int PIPE_SPEED = 3000;
	private static final double PETROLEUM_GAS_PER_MINUTE = PIPE_SPEED * 60;
	private static final double WATER_PER_MINUTE = PIPE_SPEED * 60;
	private static final double LUBRICANT_PER_MINUTE = PIPE_SPEED * 60;
	// item recipes
	private static final Producer PRODUCER_ELECTRIC_FURNACE = new Producer(
			2,
			2,
			"Electric Furnace"
	);
	private static final Producer PRODUCER_ASSEMBLY_MACHINE_3 = new Producer(
			1.25,
			4,
			"Assembly Machine 3"
	);
	private static final Producer PRODUCER_CHEMICAL_PLANT = new Producer(
			1,
			3,
			"Chemical Plant"
	);
	private static final Producer PRODUCER_BELT = new Producer(
			PIPE_SPEED,
			0,
			"Red Belt"
	);
	private static final Item COPPER_PLATE = new Item(
			"Copper Plate",
			null,
			BELT_SPEED,
			1,
			PRODUCER_BELT
	);
	private static final Item COPPER_CABLE = new Item(
			"Copper Cable",
			Map.of(
					COPPER_PLATE,
					1
			),
			2,
			0.5,
			PRODUCER_ASSEMBLY_MACHINE_3
	);
	private static final Item IRON_PLATE = new Item(
			"Iron Plate",
			null,
			BELT_SPEED,
			1,
			PRODUCER_BELT
	);
	private static final Item IRON_GEAR_WHEEL = new Item(
			"Iron Gear Wheel",
			Map.of(
					IRON_PLATE,
					2
			),
			1,
			0.5,
			PRODUCER_ASSEMBLY_MACHINE_3
	);
	private static final Item AUTOMATION_SCIENCE_PACK = new Item(
			"Automation Science Pack",
			Map.of(
					IRON_GEAR_WHEEL,
					1,
					COPPER_PLATE,
					1
			),
			1,
			5,
			PRODUCER_ASSEMBLY_MACHINE_3
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
			PRODUCER_ASSEMBLY_MACHINE_3
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
			PRODUCER_ASSEMBLY_MACHINE_3
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
			PRODUCER_ASSEMBLY_MACHINE_3
	);
	private static final Item LOGISTIC_SCIENCE_PACK = new Item(
			"Logistic Science Pack",
			Map.of(
					TRANSPORT_BELT,
					1,
					INSERTER,
					1
			),
			1,
			6,
			PRODUCER_ASSEMBLY_MACHINE_3
	);
	private static final Item FIREARM_MAGAZINE = new Item(
			"Firearm Magazine",
			Map.of(
					IRON_PLATE,
					4
			),
			1,
			1,
			PRODUCER_ASSEMBLY_MACHINE_3
	);
	private static final Item STEEL = new Item(
			"Steel",
			Map.of(
					IRON_PLATE,
					5
			),
			1,
			16,
			PRODUCER_ELECTRIC_FURNACE
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
			PRODUCER_ASSEMBLY_MACHINE_3
	);
	private static final Item PIPE = new Item(
			"Pipe",
			Map.of(
					IRON_PLATE,
					1
			),
			1,
			0.5,
			PRODUCER_ASSEMBLY_MACHINE_3
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
			PRODUCER_ASSEMBLY_MACHINE_3
	);
	private static final Item IRON_STICK = new Item(
			"Iron Stick",
			Map.of(
					IRON_PLATE,
					1
			),
			2,
			0.5,
			PRODUCER_ASSEMBLY_MACHINE_3
	);
	private static final Item COAL = new Item(
			"Coal",
			null,
			1,
			0,
			PRODUCER_BELT
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
			PRODUCER_ASSEMBLY_MACHINE_3
	);
	private static final Item STONE = new Item(
			"Stone",
			null,
			10,
			0,
			PRODUCER_BELT
	);
	private static final Item STONE_BRICK = new Item(
			"Stone Brick",
			Map.of(
					STONE,
					2
			),
			1,
			3.2,
			PRODUCER_ELECTRIC_FURNACE
	);
	private static final Item WALL = new Item(
			"Wall",
			Map.of(
					STONE_BRICK,
					5
			),
			1,
			0.5,
			PRODUCER_ASSEMBLY_MACHINE_3
	);
	private static final Item MILITARY_SCIENCE_PACK = new Item(
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
			PRODUCER_ASSEMBLY_MACHINE_3
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
			PRODUCER_ASSEMBLY_MACHINE_3
	);
	private static final Producer PRODUCER_PIPE = new Producer(
			PIPE_SPEED,
			0,
			"Pipe"
	);
	private static final Item PETROLEUM_GAS = new Item(
			"Petroleum Gas",
			null,
			1,
			0,
			PRODUCER_PIPE
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
			PRODUCER_CHEMICAL_PLANT
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
			PRODUCER_ASSEMBLY_MACHINE_3
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
			PRODUCER_ASSEMBLY_MACHINE_3
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
			PRODUCER_ASSEMBLY_MACHINE_3
	);
	private static final Item PRODUCTION_SCIENCE_PACK = new Item(
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
			PRODUCER_ASSEMBLY_MACHINE_3
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
			PRODUCER_ASSEMBLY_MACHINE_3
	);
	private static final Item WATER = new Item(
			"Water",
			null,
			1,
			0,
			PRODUCER_PIPE
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
			PRODUCER_CHEMICAL_PLANT
	);
	private static final Item CHEMICAL_SCIENCE_PACK = new Item(
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
			PRODUCER_ASSEMBLY_MACHINE_3
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
			PRODUCER_CHEMICAL_PLANT
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
			PRODUCER_ASSEMBLY_MACHINE_3
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
			PRODUCER_CHEMICAL_PLANT
	);
	private static final Item LUBRICANT = new Item(
			"Lubricant",
			null,
			1,
			0,
			PRODUCER_PIPE
	);
	private static final Map<Item, Double> Sources = Map.of(
			COPPER_PLATE,
			COPPER_PER_MINUTE,
			IRON_PLATE,
			IRON_PER_MINUTE,
			COAL,
			COAL_PER_MINUTE,
			STONE,
			STONE_PER_MINUTE,
			PETROLEUM_GAS,
			PETROLEUM_GAS_PER_MINUTE,
			WATER,
			WATER_PER_MINUTE,
			LUBRICANT,
			LUBRICANT_PER_MINUTE
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
			PRODUCER_ASSEMBLY_MACHINE_3
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
			PRODUCER_ASSEMBLY_MACHINE_3
	);
	private static final Item UTILITY_SCIENCE_PACK = new Item(
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
			PRODUCER_ASSEMBLY_MACHINE_3
	);
	private static final List<Item> Targets = List.of(
			AUTOMATION_SCIENCE_PACK,
			LOGISTIC_SCIENCE_PACK,
			MILITARY_SCIENCE_PACK,
			CHEMICAL_SCIENCE_PACK,
			PRODUCTION_SCIENCE_PACK,
			UTILITY_SCIENCE_PACK
	);
	private static final double infinity = java.lang.Double.POSITIVE_INFINITY;
	// only for testing
	private static final double MAX_PRODUCERS = infinity;

	private FactorioLP()
	{
	}

	public static HashMap<Item, Integer> getItemsUsing(Item ingredient, LinkedHashSet<Item> Items)
	{
		HashMap<Item, Integer> out = new HashMap<>();
		//System.out.println("ALL ITEMS USING " + ingredient.getName() + " AS INPUT:");
		for (Item item : Items)
		{
			Map<Item, Integer> ingredients = item.getIngredients();
			if (ingredients == null)
			{
				continue;
			}

			if (ingredients.containsKey(ingredient))
			{
				final Integer quantity = ingredients.get(ingredient);
				//System.out.println(item.getName() + " (x" + item.getQuantity() + ")");
				out.put(
						item,
						quantity
				);
			}
		}
		return out;
	}

	public static List<Item> getDependenciesRecursively(Item item)
	{
		List<Item> out = new ArrayList<>();
		out.add(item);
		Map<Item, Integer> ingredients = item.getIngredients();
		if (ingredients == null)
		{
			return out;
		}
		for (Map.Entry<Item, Integer> mapElement : ingredients.entrySet())
		{
			if (!out.contains(mapElement.getKey()))
			{
				out.add(mapElement.getKey());
				out.addAll(getDependenciesRecursively(mapElement.getKey()));
			}

		}
		return out;
	}

	public static void addItemConstraint(
			MPSolver solver, Item item, LinkedHashSet<Item> dependencies, MPVariable waste
	)
	{
		HashMap<Item, Integer> itemsUsing = getItemsUsing(
				item,
				dependencies
		);
		if (itemsUsing.isEmpty())
		{
			return;
		}

		MPConstraint ct = solver.makeConstraint(
				// set lb to -infinity if not using the waste variable
				-10.0,
				0.0,
				item.getName()
		);
		ct.setCoefficient(
				item.getVariable(),
				-1
		);

		StringBuilder sb = new StringBuilder();
		sb.append(item.getName()).append(" >=");
		for (Map.Entry<Item, Integer> mapElement : itemsUsing.entrySet())
		{
			// e.g. 3 advanced circuits make 2 chemical science, so
			// advanced circuits >= (3/2) (chemical science) + ...
			final double coefficient =
					mapElement.getValue() / (double) mapElement.getKey().getQuantity();
			ct.setCoefficient(
					mapElement.getKey().getVariable(),
					coefficient
			);
			sb.append(" + ").append(coefficient).append("(").append(mapElement.getKey().getName())
					.append(")");
		}

		// attempt to maximize waste in the objective function, so we use as few items as possible
		ct.setCoefficient(
				waste,
				1
		);

		System.out.println(sb);
	}

	public static void main(String[] args)
	{
		Loader.loadNativeLibraries();
		MPSolver solver = MPSolver.createSolver("SCIP");
		if (solver == null)
		{
			System.out.println("Could not create solver SCIP");
			return;
		}

		LinkedHashSet<Item> dependencies = new LinkedHashSet<>();

		for (Item item : Targets)
		{
			dependencies.addAll(getDependenciesRecursively(item));
		}

		// initialize variables for each item
		for (Item item : dependencies)
		{
			item.setVariable(solver.makeIntVar(
					0.0,
					infinity,
					item.getName()
			));
		}

		// set item constraints
		System.out.println("==============================");
		System.out.println("ITEM CONSTRAINTS");
		System.out.println("==============================");
		MPVariable waste = solver.makeIntVar(
				0.0,
				infinity,
				"String"
		);
		for (Item item : dependencies)
		{
			addItemConstraint(
					solver,
					item,
					dependencies,
					waste
			);
		}

		// set supply constraints
		System.out.println();
		System.out.println("==============================");
		System.out.println("SUPPLY CONSTRAINTS");
		System.out.println("==============================");
		for (Map.Entry<Item, Double> mapElement : Sources.entrySet())
		{
			MPConstraint ct = solver.makeConstraint(
					0,
					mapElement.getValue()
			);
			ct.setCoefficient(
					mapElement.getKey().getVariable(),
					1
			);
			System.out.println(mapElement.getKey().getName() + " <= " + mapElement.getValue());
		}

		System.out.println();
		System.out.println("==============================");
		System.out.println("TARGET CONSTRAINTS");
		System.out.println("==============================");
		MPVariable science = solver.makeIntVar(
				0.0,
				infinity,
				"science"
		);

		for (Item item : Targets)
		{
			MPConstraint ct = solver.makeConstraint(
					-infinity,
					0.0
			);
			System.out.println("science <=  " + item.getName());
			ct.setCoefficient(
					science,
					1
			);
			ct.setCoefficient(
					item.getVariable(),
					-1
			);
		}

		System.out.println();
		System.out.println("==============================");
		System.out.println("PRODUCTION CONSTRAINTS");
		System.out.println("==============================");

		// put total producers into a variable to simplify the objective function later
		MPVariable totalProducers = solver.makeIntVar(
				0.0,
				MAX_PRODUCERS,
				"Total producers"
		);

		MPConstraint totalProducersConstraint = solver.makeConstraint(
				-infinity,
				0.0
		);
		totalProducersConstraint.setCoefficient(
				totalProducers,
				-1
		);

		for (Item item : dependencies)
		{
			Producer producer = item.getProducer();
			final double numProducedPerMinute = producer.getSpeed() * 60 / item.getTime();
			// initialize numProducers variable
			MPVariable numProducers = solver.makeIntVar(
					0.0,
					infinity,
					"numProducers " + item.getName()
			);
			item.setNumProducers(numProducers);

			MPConstraint ct = solver.makeConstraint(
					-infinity,
					0.0,
					"numPerMinute " + item.getName()
			);
			ct.setCoefficient(
					item.getVariable(),
					1.0
			);
			ct.setCoefficient(
					numProducers,
					-numProducedPerMinute
			);
			System.out.println(item.getName() + " <= " + (int) numProducedPerMinute +
					" * (# of producers for " + item.getName() + ")");

			totalProducersConstraint.setCoefficient(
					numProducers,
					1.0
			);
		}

		// set objective function as the minimum over all targets
		MPObjective objective = solver.objective();
		objective.setCoefficient(
				science,
				1
		);
		// minimize max producers but never at the cost of science
		objective.setCoefficient(
				totalProducers,
				-0.5 / MAX_PRODUCERS
		);
		// minimize waste last
		objective.setCoefficient(
				waste,
				0.0
		);
		objective.setMaximization();

		// print solution
		System.out.println();
		System.out.println("==============================");
		final MPSolver.ResultStatus resultStatus = solver.solve();
		if (resultStatus == MPSolver.ResultStatus.OPTIMAL)
		{
			System.out.println("SOLUTION:");
			System.out.println("==============================");
			for (Item item : dependencies)
			{
				System.out.println(
						item.getName() + " x" + (int) item.getNumProducers().solutionValue() +
								": " + (int) item.getVariable().solutionValue() + "/min");
			}
			System.out.println();
			System.out.println("==============================");
			System.out.println("SPM: " + (int) science.solutionValue());
		}
		else
		{
			System.out.println("The problem does not have an optimal solution!");
		}
		System.out.println("==============================");
	}
}
