package org.nammy.cde.model;

public abstract class Resource<T extends Resource> implements Comparable<T> {
  private final String name;
  private final long capacity;

  private double load;

  public Resource(String name, long capacity) {
    this.name = name;
    this.capacity = capacity;
  }

  public String getName() {
    return name;
  }

  public double getLoad() {
    return load;
  }

  protected void addLoad(double increment) {
    load += increment;
  }

  public double getWeightedLoad() {
    return 1.0 * load / capacity;
  }

  @Override
  public int compareTo(T other) {
    int diff = Double.compare(getWeightedLoad(), other.getWeightedLoad());

    if (diff != 0) {
      return diff;
    } else {
      return name.compareTo(other.getName());
    }
  }
}
