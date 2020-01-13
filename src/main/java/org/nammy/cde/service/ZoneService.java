package org.nammy.cde.service;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Logger;

public class ZoneService {
  private static final Logger LOG = Logger.getLogger(ZoneService.class.getSimpleName());

  private final Map<String, NodeService> nodeMap = new LinkedHashMap<>();

  private final RegionService region;
  private final String name;

  public ZoneService(RegionService region, String name) {
    this.region = region;
    this.name = name;
  }

  public static ZoneService create(RegionService region, String name) {
    return new ZoneService(region, name);
  }

  public String getName() {
    return name;
  }

  public NodeService getNode(String name) {
    return nodeMap.get(name);
  }

  public NodeService getOrCreateNode(String name) {
    return nodeMap.computeIfAbsent(name, this::createNode);
  }

  private NodeService createNode(String name) {
    NodeService node = NodeService.create(this, name);

    LOG.info("created node=" + node);

    return node;
  }

  public RegionService getRegion() {
    return region;
  }

  public Collection<NodeService> getNodes() {
    return nodeMap.values();
  }

  @Override
  public String toString() {
    return getClass().getSimpleName()
            + "["
            + "name=" + name
            + ", region=" + region.getName()
            + ", nodes=" + nodeMap.values()
            + "]";
  }
}
