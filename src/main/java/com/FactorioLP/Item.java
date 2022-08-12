package com.FactorioLP;

import com.google.ortools.linearsolver.MPVariable;
import java.util.Map;
public class Item
{
	final private Map<Item, Integer> ingredients;
	final private double time;
	final private Producer producer;
	final private String name;
	final private int quantity;
	private MPVariable variable;
	private MPVariable numProducers;

	public Item(String name, Map<Item, Integer> ingredients, int quantity, double time, Producer producer)
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

	public void setVariable(MPVariable variable)
	{
		this.variable = variable;
	}

	public MPVariable getVariable()
	{
		return this.variable;
	}

	public void setNumProducers(MPVariable numProducers)
	{
		this.numProducers = numProducers;
	}

	public MPVariable getNumProducers()
	{
		return this.numProducers;
	}
}
