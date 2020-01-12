package org.nammy.model;

import java.util.HashMap;
import java.util.Map;

public class Region {
  private final Map<String, Zone> zoneMap = new HashMap<>();

  private final String name;

  public Region(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public Zone addZone(String zoneName) {
    Zone zone = new Zone(zoneName, this);

    zoneMap.put(zoneName, zone);

    return zone;
  }

  @Override
  public String toString() {
    return getClass().getSimpleName()
            + "["
            + "name=" + name
            + ", zones=" + zoneMap.values()
            + "]";
  }
}
