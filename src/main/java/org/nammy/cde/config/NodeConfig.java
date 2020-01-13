package org.nammy.cde.config;

import java.util.Objects;

public class NodeConfig {
  private final String nodeName;
  private final String regionName;
  private final String zoneName;

  public NodeConfig(String nodeName, String regionName, String zoneName) {
    this.nodeName = nodeName;
    this.regionName = regionName;
    this.zoneName = zoneName;
  }

  public String getNodeName() {
    return nodeName;
  }

  public String getRegionName() {
    return regionName;
  }

  public String getZoneName() {
    return zoneName;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    NodeConfig that = (NodeConfig) o;
    return Objects.equals(nodeName, that.nodeName) &&
            Objects.equals(regionName, that.regionName) &&
            Objects.equals(zoneName, that.zoneName);
  }

  @Override
  public int hashCode() {
    return Objects.hash(nodeName, regionName, zoneName);
  }

  @Override
  public String toString() {
    return getClass().getSimpleName()
            + "["
            + "node=" + nodeName
            + ", region=" + regionName
            + ", zone=" + zoneName
            + "]";
  }
}
