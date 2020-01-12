package org.nammy.model;

public class Node {
  private final String name;
  private final Region region;
  private final Zone zone;

  public Node(String name, Region region, Zone zone) {
    this.name = name;
    this.region = region;
    this.zone = zone;
  }

  public String getName() {
    return name;
  }

  public Region getRegion() {
    return region;
  }

  public Zone getZone() {
    return zone;
  }

  @Override
  public String toString() {
    return getClass().getSimpleName()
            + "["
            + "name=" + name
						+ ", region=" + region.getName()
						+ ", zone=" + zone.getName()
            + "]";
  }
}
