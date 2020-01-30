package org.nammy.cde.service;

import org.nammy.cde.model.Resource;

public class NodeService extends Resource<NodeService> {
  private final String regionName;
  private final String zoneName;

  private NodeService(String name, String regionName, String zoneName) {
    super(name, 1);

    this.regionName = regionName;
    this.zoneName = zoneName;
  }

  public static NodeService create(String name, String regionName, String zoneName) {
    return new NodeService(name, regionName, zoneName);
  }

  public String getRegionName() {
    return regionName;
  }

  public String getZoneName() {
    return zoneName;
  }

  public void updateLoad(double cost) {
    addLoad(cost);
  }

  @Override
  public String toString() {
    return getClass().getSimpleName()
            + "["
            + "name=" + getName()
            + ", weightedLoad=" + getWeightedLoad()
            + "]";
  }
}
