package org.nammy.model;

import java.util.HashMap;
import java.util.Map;

public class Zone {
  private final Map<String, Node> nodeMap = new HashMap<>();

  private final String name;
  private final Region region;

  public Zone(String name, Region region) {
    this.name = name;
    this.region = region;
  }

  public String getName() {
    return name;
  }

  public Node addNode(String nodeName) {
    Node node = new Node(nodeName, region, this);

    nodeMap.put(nodeName, node);

    return node;
  }

  @Override
  public String toString() {
    return getClass().getSimpleName()
            + "["
            + "name=" + name
            + ", region=" + region.getName()
            + "]";
  }
}
