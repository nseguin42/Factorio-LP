package com.FactorioLP;

import java.util.HashMap;
import java.util.Map;

public enum Item {
  COPPER_PLATE(
      null,
      Producer.BELT_SPEED,
      1,
      Producer.BELT
  ),
  IRON_PLATE(
      null,
      Producer.BELT_SPEED,
      1,
      Producer.BELT
  ),
  COAL(
      null,
      1,
      0,
      Producer.BELT
  ),
  STONE(
      null,
      10,
      0,
      Producer.BELT
  ),
  PETROLEUM_GAS(
      null,
      1,
      0,
      Producer.PIPE
  ),
  WATER(
      null,
      1,
      0,
      Producer.PIPE
  ),
  LUBRICANT(
      null,
      1,
      0,
      Producer.PIPE
  ),
  COPPER_CABLE(
      Map.of(
          COPPER_PLATE,
          1
      ),
      2,
      0.5,
      Producer.ASSEMBLY_MACHINE_3
  ),
  ELECTRONIC_CIRCUIT(
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
      Map.of(
          IRON_PLATE,
          1
      ),
      2,
      0.5,
      Producer.ASSEMBLY_MACHINE_3
  ),
  PIPE(
      Map.of(
          IRON_PLATE,
          1
      ),
      1,
      0.5,
      Producer.ASSEMBLY_MACHINE_3
  ),
  STEEL(
      Map.of(
          IRON_PLATE,
          5
      ),
      1,
      16,
      Producer.ELECTRIC_FURNACE
  ),
  RAIL(
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
      Map.of(
          IRON_PLATE,
          4
      ),
      1,
      1,
      Producer.ASSEMBLY_MACHINE_3
  ),
  PIERCING_ROUNDS_MAGAZINE(
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
      Map.of(
          IRON_PLATE,
          2
      ),
      1,
      0.5,
      Producer.ASSEMBLY_MACHINE_3
  ),
  ENGINE_UNIT(
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
      Map.of(
          STONE,
          2
      ),
      1,
      3.2,
      Producer.ELECTRIC_FURNACE
  ),
  WALL(
      Map.of(
          STONE_BRICK,
          5
      ),
      1,
      0.5,
      Producer.ASSEMBLY_MACHINE_3
  ),
  MILITARY_SCIENCE_PACK(
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

  static final Item[] ALL_ITEMS = Item.values();
  static final HashMap<String, Item> nameToItemMap = new HashMap<>(256);

  static {
    for (Item item : ALL_ITEMS) {
      nameToItemMap.put(item.toString(), item);
    }
  }

  final private Map<Item, Integer> dependencies;
  final private double craftingTime;
  final private Producer producer;
  final private int outputQuantity;
  // 2 iron bars makes 1 iron gear wheel, so ironBar.usedFor : ironGearWheel -> 2
  final private Map<Item, Integer> usedFor = new HashMap<>(256);
  final private Map<Item, Integer> transitiveDependencies = new HashMap<>(256);

  Item(Map<Item, Integer> dependencies, int outputQuantity, double craftingTime,
      Producer producer) {
    this.dependencies = dependencies;
    this.outputQuantity = outputQuantity;
    this.craftingTime = craftingTime;
    this.producer = producer;

    if (dependencies == null) {
      return;
    }

    this.transitiveDependencies.putAll(dependencies);

    for (Item item : dependencies.keySet()) {
      // Each of this' dependencies is usedFor this
      final Map<Item, Integer> usedFor = item.getUsedFor();
      usedFor.put(this, dependencies.get(item));

      // Each dependency's transitive dependencies are transitive dependencies
      final Map<Item, Integer> itemTransDeps = item.getTransitiveDependencies();
      // if this has keys in common with a dependency, add the values
      this.transitiveDependencies.replaceAll(
          (k, v) -> v + (itemTransDeps.get(k) == null ? 0 : itemTransDeps.get(k)));
      // throw in the rest of the keys
      for (Item dep : itemTransDeps.keySet()) {
        Integer val = itemTransDeps.get(dep);
        this.transitiveDependencies.putIfAbsent(dep, val);
      }
    }
  }

  public static Item nameToItem(String name) {
    return nameToItemMap.get(name);
  }

  public Map<Item, Integer> getTransitiveDependencies() {
    return this.transitiveDependencies;
  }

  public Map<Item, Integer> getUsedFor() {
    return this.usedFor;
  }

  public String toString() {
    // replace _ with space, and capitalize first letter of each word
    // e.g. LOGISTIC_SCIENCE_PACK -> "Logistic Science Pack"
    String[] words = name().split("_");
    StringBuilder sb = new StringBuilder();
    for (String word : words) {
      sb.append(word.substring(0, 1).toUpperCase()).append(word.substring(1).toLowerCase())
          .append(" ");
    }
    return sb.toString().trim();
  }

  public int getOutputQuantity() {
    return this.outputQuantity;
  }

  public double getCraftingTime() {
    return this.craftingTime;
  }

  public Producer getProducer() {
    return this.producer;
  }

  public Map<Item, Integer> getDependencies() {
    return this.dependencies;
  }
}
