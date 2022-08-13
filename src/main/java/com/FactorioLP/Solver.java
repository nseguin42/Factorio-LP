package com.FactorioLP;

import static com.FactorioLP.Item.COAL;
import static com.FactorioLP.Item.COPPER_PLATE;
import static com.FactorioLP.Item.IRON_PLATE;
import static com.FactorioLP.Item.LUBRICANT;
import static com.FactorioLP.Item.PETROLEUM_GAS;
import static com.FactorioLP.Item.STONE;
import static com.FactorioLP.Item.WATER;
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

public final class Solver
{
	// input rates
	private static final double IRON_PER_MINUTE = IRON_PLATE.getProducer().getSpeed() * 60 * 4;
	private static final double COPPER_PER_MINUTE = COPPER_PLATE.getProducer().getSpeed() * 60 * 4;
	private static final double COAL_PER_MINUTE = COAL.getProducer().getSpeed() * 60 * 2;
	private static final double STONE_PER_MINUTE = STONE.getProducer().getSpeed() * 60 * 2;
	private static final double PETROLEUM_GAS_PER_MINUTE =
			PETROLEUM_GAS.getProducer().getSpeed() * 60 * 2;
	private static final double WATER_PER_MINUTE = WATER.getProducer().getSpeed() * 60 * 2;
	private static final double LUBRICANT_PER_MINUTE = LUBRICANT.getProducer().getSpeed() * 60 * 2;
	private static final Map<Item, Double> Sources = Map.of(COPPER_PLATE, COPPER_PER_MINUTE,
			Item.IRON_PLATE, IRON_PER_MINUTE, Item.COAL, COAL_PER_MINUTE, STONE, STONE_PER_MINUTE,
			PETROLEUM_GAS, PETROLEUM_GAS_PER_MINUTE, Item.WATER, WATER_PER_MINUTE, Item.LUBRICANT,
			LUBRICANT_PER_MINUTE
	);
	/*
	Item.AUTOMATION_SCIENCE_PACK,
	Item.LOGISTIC_SCIENCE_PACK,
	Item.MILITARY_SCIENCE_PACK,
	Item.CHEMICAL_SCIENCE_PACK,
	Item.PRODUCTION_SCIENCE_PACK,
	Item.UTILITY_SCIENCE_PACK
	*/
	private static final double infinity = java.lang.Double.POSITIVE_INFINITY;
	// only for testing
	private static final double MAX_PRODUCERS = infinity;
	static ArrayList<Item> Targets = new ArrayList<>();

	private Solver()
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
				out.put(item, quantity);
			}
		}
		return out;
	}

	public static List<Item> getDependenciesRecursively(Item item)
	{
		List<Item> out = new ArrayList<>();
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

	public static String addItemConstraint(
			MPSolver solver, Item item, HashMap<Item, MPVariable> mapItemToVar,
			LinkedHashSet<Item> dependencies, MPVariable waste
	)
	{
		HashMap<Item, Integer> itemsUsing = getItemsUsing(item, dependencies);
		if (itemsUsing.isEmpty())
		{
			return null;
		}

		MPConstraint ct = solver.makeConstraint(
				// set lb to -infinity if not using the waste variable
				-100.0, 0.0, item.getName());
		ct.setCoefficient(mapItemToVar.get(item), -1);

		StringBuilder sb = new StringBuilder();
		sb.append(item.getName()).append(" >=");
		for (Map.Entry<Item, Integer> mapElement : itemsUsing.entrySet())
		{
			// e.g. 3 advanced circuits make 2 chemical science, so
			// advanced circuits >= (3/2) (chemical science) + ...
			final double coefficient =
					mapElement.getValue() / (double) mapElement.getKey().getQuantity();
			ct.setCoefficient(mapItemToVar.get(mapElement.getKey()), coefficient);
			sb.append(" + ").append(coefficient).append("(").append(mapElement.getKey().getName())
					.append(")");
		}

		// attempt to maximize waste in the objective function, so we use as few items as possible
		ct.setCoefficient(waste, 1);

		return (sb.toString());
	}

	public static void main(String[] args)
	{
		List<String> itemConstraintsText = new ArrayList<>();
		List<String> supplyConstraintsText = new ArrayList<>();
		List<String> targetConstraintsText = new ArrayList<>();
		List<String> productionConstraintsText = new ArrayList<>();
		List<String> spmText = new ArrayList<>();
		ArrayList<Object[]> solution = new ArrayList<>();

		HashMap<Item, MPVariable> mapItemToVar = new HashMap<>();
		HashMap<Item, MPVariable> mapItemToNumProducersVar = new HashMap<>();

		if (Targets.size() == 0)
		{
			System.err.println("No targets specified");
			return;
		}

		Loader.loadNativeLibraries();
		MPSolver solver = MPSolver.createSolver("SCIP");
		if (solver == null)
		{
			System.err.println("Could not create solver SCIP");
			return;
		}

		LinkedHashSet<Item> ItemSet = new LinkedHashSet<>();
		for (Item item : Targets)
		{
			ItemSet.add(item);
			ItemSet.addAll(getDependenciesRecursively(item));
		}

		// waste variable which will count towards all item constraints.
		// by giving it a coefficient of 0.0 in the objective function, it will be
		// maximized (without changing the objective function) and minimize excess production
		MPVariable waste = solver.makeIntVar(0.0, infinity, "Waste");

		// initialize variables for each item
		for (Item item : ItemSet)
		{
			if (mapItemToVar.get(item) == null)
			{
				MPVariable var = solver.makeIntVar(0.0, infinity, item.getName());
				mapItemToVar.put(item, var);
			}
		}

		// set item constraints
		for (Item item : ItemSet)
		{
			MPVariable var = mapItemToVar.get(item);
			String constraintString = addItemConstraint(solver, item, mapItemToVar, ItemSet,
					waste);
			if (constraintString != null)
			{
				itemConstraintsText.add(constraintString);
			}
		}

		// set supply constraints
		for (Map.Entry<Item, Double> mapElement : Sources.entrySet())
		{
			MPConstraint ct = solver.makeConstraint(0, mapElement.getValue());
			ct.setCoefficient(mapItemToVar.get(mapElement.getKey()), 1);
			supplyConstraintsText.add(
					mapElement.getKey().getName() + " <= " + mapElement.getValue());
		}

		// artificial variable which is at most as large as the smallest target quantity.
		// maximize this to compute SPM
		MPVariable minTarget = solver.makeIntVar(0.0, infinity, "minTarget");

		for (Item item : Targets)
		{
			MPConstraint ct = solver.makeConstraint(-infinity, 0.0);
			targetConstraintsText.add("minTarget <=  " + item.getName());
			ct.setCoefficient(minTarget, 1);
			ct.setCoefficient(mapItemToVar.get(item), -1);
		}

		// put total producers into a variable to simplify the objective function later
		MPVariable totalProducers = solver.makeIntVar(0.0, MAX_PRODUCERS, "Total producers");

		// constraint that will enforce totalProducers >= (sum of all numProducers)
		MPConstraint totalProducersConstraint = solver.makeConstraint(-infinity, 0.0);
		totalProducersConstraint.setCoefficient(totalProducers, -1);

		for (Item item : ItemSet)
		{
			Producer producer = item.getProducer();
			final double numProducedPerMinute = producer.getSpeed() * 60 / item.getTime();

			// initialize numProducers variable
			MPVariable numProducers = solver.makeIntVar(0.0, infinity,
					"numProducers " + item.getName()
			);
			mapItemToNumProducersVar.put(item, numProducers);
			item.setNumProducers(numProducers);

			// initialize supply constraint
			MPConstraint ct = solver.makeConstraint(-infinity, 0.0,
					"numPerMinute " + item.getName()
			);
			ct.setCoefficient(mapItemToVar.get(item), 1.0);
			ct.setCoefficient(numProducers, -numProducedPerMinute);

			// "Iron Gears <= 12 * #(Assembly Machine 3 supplying Iron Gears)"
			productionConstraintsText.add(
					item.getName() + " <= " + (int) numProducedPerMinute + " * #(" +
							item.getProducer().getName() + " supplying " + item.getName() + ")");

			// update the total producers constraint
			totalProducersConstraint.setCoefficient(numProducers, 1.0);
		}

		MPObjective objective = solver.objective();
		objective.setCoefficient(minTarget, 1);
		// minimize max producers but never at the cost of minTarget
		objective.setCoefficient(totalProducers, -0.5 / MAX_PRODUCERS);
		// minimize waste last
		objective.setCoefficient(waste, 0.0);
		objective.setMaximization();

		/*
		// do not solve if args are passed
		if (args.length > 0)
		{
			return;
		}
		*/

		final MPSolver.ResultStatus resultStatus = solver.solve();
		if (resultStatus == MPSolver.ResultStatus.OPTIMAL)
		{
			for (Item item : ItemSet)
			{
				String name = item.getName();
				int numProducers = (int) mapItemToNumProducersVar.get(item).solutionValue();
				int perMinute = (int) mapItemToVar.get(item).solutionValue();
				Object[] solutionEntry = {name, numProducers, perMinute};
				solution.add(solutionEntry);
			}
			spmText.add(String.valueOf((int) minTarget.solutionValue()));

			List<String>[] textList = new List[] {itemConstraintsText, supplyConstraintsText,
					targetConstraintsText, productionConstraintsText, spmText};
			GUI.updateTextStrings(textList);
			GUI.updateSolutionMatrix(solution);
		}
		else
		{
			System.err.println("The problem does not have an optimal solution!");
		}
	}

}
