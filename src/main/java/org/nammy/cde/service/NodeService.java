package org.nammy.cde.service;

public class NodeService {
  private final ZoneService zone;
  private final String name;

  private NodeService(ZoneService zone, String name) {
    this.zone = zone;
    this.name = name;
  }

  public static NodeService create(ZoneService zone, String name) {
    return new NodeService(zone, name);
  }

  public String getName() {
    return name;
  }

  public ZoneService getZone() {
    return zone;
  }

  @Override
  public String toString() {
    return getClass().getSimpleName()
            + "["
            + "name=" + name
						+ ", zone=" + zone.getName()
            + "]";
  }
}
