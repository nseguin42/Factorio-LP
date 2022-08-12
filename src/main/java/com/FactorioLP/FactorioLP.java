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
	// input rates
	private static final double IRON_PER_MINUTE = Producer.BELT_SPEED * 60 * 4;
	private static final double COPPER_PER_MINUTE = Producer.BELT_SPEED * 60 * 4;
	private static final double COAL_PER_MINUTE = Producer.BELT_SPEED * 60 * 2;
	private static final double STONE_PER_MINUTE = Producer.BELT_SPEED * 60 * 2;
	private static final double PETROLEUM_GAS_PER_MINUTE = Producer.PIPE_SPEED * 60 * 2;
	private static final double WATER_PER_MINUTE = Producer.PIPE_SPEED * 60 * 2;
	private static final double LUBRICANT_PER_MINUTE = Producer.PIPE_SPEED * 60 * 2;
	private static final Map<Item, Double> Sources = Map.of(
			Item.COPPER_PLATE,
			COPPER_PER_MINUTE,
			Item.IRON_PLATE,
			IRON_PER_MINUTE,
			Item.COAL,
			COAL_PER_MINUTE,
			Item.STONE,
			STONE_PER_MINUTE,
			Item.PETROLEUM_GAS,
			PETROLEUM_GAS_PER_MINUTE,
			Item.WATER,
			WATER_PER_MINUTE,
			Item.LUBRICANT,
			LUBRICANT_PER_MINUTE
	);
	private static final List<Item> Targets = List.of(
			Item.AUTOMATION_SCIENCE_PACK,
			Item.LOGISTIC_SCIENCE_PACK,
			Item.MILITARY_SCIENCE_PACK,
			Item.CHEMICAL_SCIENCE_PACK,
			Item.PRODUCTION_SCIENCE_PACK,
			Item.UTILITY_SCIENCE_PACK
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
				-100.0,
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
