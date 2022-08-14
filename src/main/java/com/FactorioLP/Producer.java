package com.FactorioLP;

public enum Producer {
  PIPE(3000, 0),
  BELT(45, 0),
  ELECTRIC_FURNACE(2, 2),
  CHEMICAL_PLANT(1, 3),
  ASSEMBLY_MACHINE_3(1.25, 4);
  static final int BELT_SPEED = 45;
  private static final double SPEED_MODULE_MULTIPLIER = 0.5;
  final double speed;
  final int slots;

  Producer(double speed, int slots) {
    this.speed = speed;
    this.slots = slots;
  }

  public double getSpeed() {
    return speed * (1 + slots * SPEED_MODULE_MULTIPLIER);
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

}
