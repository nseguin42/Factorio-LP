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
import java.util.Set;

public final class Solver {

  private static final double infinity = java.lang.Double.POSITIVE_INFINITY;
  // only for testing
  private static final double MAX_PRODUCERS = infinity;
  static Set<Item> Targets = new LinkedHashSet<>();
  static Map<Item, Double> Supplies = new HashMap<>();

  private Solver() {
  }


  public static String addItemConstraint(
      MPSolver solver, Item item, HashMap<Item, MPVariable> mapItemToVar, MPVariable waste
  ) {
    Map<Item, Integer> itemsUsing = item.getUsedFor();
    if (itemsUsing.isEmpty()) {
      return null;
    }

    MPConstraint ct = solver.makeConstraint(
        // set lb to -infinity if not using the waste variable
        -infinity, 0.0, item.toString());
    ct.setCoefficient(mapItemToVar.get(item), -1);

    StringBuilder sb = new StringBuilder();
    sb.append(item).append(" >=");
    for (Map.Entry<Item, Integer> mapElement : itemsUsing.entrySet()) {
      // e.g. 3 advanced circuits make 2 chemical science, so
      // advanced circuits >= (3/2) (chemical science) + ...
      final double coefficient =
          mapElement.getValue() / (double) mapElement.getKey().getOutputQuantity();
      ct.setCoefficient(mapItemToVar.get(mapElement.getKey()), coefficient);
      sb.append(" + ").append(coefficient).append("(").append(mapElement.getKey().toString())
          .append(")");
    }

    // attempt to maximize waste in the objective function, so we use as few items as possible
    ct.setCoefficient(waste, 1);

    return (sb.toString());
  }

  public static void main(String[] args) {
    if (Targets.isEmpty()) {
      System.out.println("No targets specified");
      return;
    }

    List<String> itemConstraintsText = new ArrayList<>();
    List<String> supplyConstraintsText = new ArrayList<>();
    List<String> targetConstraintsText = new ArrayList<>();
    List<String> productionConstraintsText = new ArrayList<>();
    List<String> spmText = new ArrayList<>();
    List<String> log = new ArrayList<>();
    ArrayList<Object[]> solution = new ArrayList<>();

    HashMap<Item, MPVariable> mapItemToVar = new HashMap<>(256);
    HashMap<Item, MPVariable> mapItemToNumProducersVar = new HashMap<>(256);

    Loader.loadNativeLibraries();
    MPSolver solver = MPSolver.createSolver("SCIP");
    if (solver == null) {
      System.err.println("Could not create solver SCIP");
      log.add("Could not create solver SCIP");
      return;
    }

    Set<Item> ItemSet = new LinkedHashSet<>(256);
    ItemSet.addAll(Targets);
    for (Item item : Targets) {
      Map<Item, Integer> itemTransDeps = item.getTransitiveDependencies();
      ItemSet.addAll(itemTransDeps.keySet());
    }

    // waste variable which will count towards all item constraints.
    // by giving it a coefficient of 0.0 in the objective function, it will be
    // maximized (without changing the objective function) and minimize excess production
    MPVariable slack = solver.makeIntVar(0.0, infinity, "Waste");

    // initialize variables for each item
    for (Item item : ItemSet) {
      MPVariable var = solver.makeIntVar(0.0, infinity, item.toString());
      mapItemToVar.put(item, var);

      MPVariable numProducersVar = solver.makeIntVar(0.0, MAX_PRODUCERS, item + "_numProducers");
      mapItemToNumProducersVar.put(item, numProducersVar);

      log.add("Initialized variables for " + item);
    }

    // set item constraints
    for (Item item : ItemSet) {
      String constraintString = addItemConstraint(solver, item, mapItemToVar,
          slack
      );
      if (constraintString != null) {
        itemConstraintsText.add(constraintString);
        log.add("Added constraint: " + constraintString);
      }
    }

    // artificial variable which is at most as large as the smallest target quantity.
    // we maximize this to compute SPM
    MPVariable minTarget = solver.makeIntVar(0.0, infinity, "minTarget");

    for (Item item : Targets) {
      MPConstraint ct = solver.makeConstraint(-infinity, 0.0);
      ct.setCoefficient(minTarget, 1);
      ct.setCoefficient(mapItemToVar.get(item), -1);
      targetConstraintsText.add("minTarget <=  " + item);
      log.add("Added constraint: minTarget <=  " + item);
    }

    // put total producers into a variable to simplify the objective function later
    MPVariable totalProducers = solver.makeIntVar(0.0, MAX_PRODUCERS, "Total producers");

    // totalProducers >= (sum of all numProducers)
    MPConstraint totalProducersConstraint = solver.makeConstraint(-infinity, 0.0);
    totalProducersConstraint.setCoefficient(totalProducers, -1);
    log.add("Added constraint: totalProducers >= (sum of all numProducers)");

    for (Item item : ItemSet) {
      Producer producer = item.getProducer();
      MPVariable numItemVar = mapItemToVar.get(item);
      final double ratePerProducer = producer.getSpeed() * 60 / item.getCraftingTime();

      // initialize supply constraint: 0 <= numItem - supply <= maxProduction = numProducers
      // since maxProduction depends on the variable numProducers, we have to split into two
      // constraints:
      // ct1: (supply <= numItem <= infinity) &&
      // ct2: (-infinity <= numItem - maxProduction <= supply)

			/*
			MPConstraint ct1 = solver.makeConstraint(supply, infinity, "ct1 " + item.toString());
			ct1.setCoefficient(numItemVar, 1);
			// "Iron Plate >= 1800"
			supplyConstraintsText.add(item.toString() + " >= " + supply);
			 */

      if (Supplies.containsKey(item)) {
        double supplyPerMin = Supplies.get(item) * 60.0;
        MPConstraint ct = solver.makeConstraint(
            -infinity, supplyPerMin, "supply " + item);
        ct.setCoefficient(numItemVar, 1);
        ct.setCoefficient(slack, 1);

        // "Iron Plate <= 1800"
        final String supplyConstraintString = item + " <= " + supplyPerMin;
        supplyConstraintsText.add(supplyConstraintString);
        log.add("Added constraint: " + supplyConstraintString);
      } else {
        MPVariable numProducersVar = mapItemToNumProducersVar.get(item);
        MPConstraint ct = solver.makeConstraint(-infinity, 0, "production " + item);
        ct.setCoefficient(numItemVar, 1);
        ct.setCoefficient(slack, 1);
        ct.setCoefficient(numProducersVar, -ratePerProducer);

        // "Iron Gears <= Supply + 12 * #(Assembly Machine 3 supplying Iron Gears)"
        final String productionConstraintString =
            item + " <= " + ratePerProducer + " * #(" +
                item.getProducer() + " producing " + item +
                ")";
        productionConstraintsText.add(productionConstraintString);
        log.add("Added constraint: " + productionConstraintString);

        // update the total producers constraint
        totalProducersConstraint.setCoefficient(numProducersVar, 1.0);
      }
    }

    MPObjective objective = solver.objective();
    objective.setCoefficient(minTarget, 1);
    // minimize max producers but never at the cost of minTarget
    objective.setCoefficient(totalProducers, -0.5/MAX_PRODUCERS);
    // minimize waste last
    objective.setCoefficient(slack, 0.0);
    objective.setMaximization();
    log.add("Objective: maximize minTarget - totalProducers / 2 " + MAX_PRODUCERS +
        " + 0.0 waste");

		/*
		// do not solve if args are passed
		if (args.length > 0)
		{
			return;
		}
		*/

    final MPSolver.ResultStatus resultStatus = solver.solve();
    if (resultStatus == MPSolver.ResultStatus.OPTIMAL) {
      for (Item item : ItemSet) {
        String name = item.toString();
        if (!Supplies.containsKey(item)) {
          int numProducers = (int) mapItemToNumProducersVar.get(item).solutionValue();
          int perMinute = (int) mapItemToVar.get(item).solutionValue();
          Object[] solutionEntry = {name, numProducers, perMinute};
          solution.add(solutionEntry);
        }
      }
      spmText.add(String.valueOf((int) minTarget.solutionValue()));
      log.add("Solution: " + minTarget.solutionValue());
      log.add("Slack: " + slack.solutionValue());
      log.add("Total producers: " + totalProducers.solutionValue());

      List<List<String>> textList = List.of(itemConstraintsText, supplyConstraintsText,
          targetConstraintsText, productionConstraintsText, spmText);
      GUI.updateTextStrings(textList);
      GUI.updateSolutionMatrix(solution);
    } else {
      System.err.println("The problem does not have an optimal solution!");
    }
    log.forEach(System.out::println);
  }
}
