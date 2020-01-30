package org.nammy.cde.service;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeSet;
import java.util.logging.Logger;

import org.nammy.cde.config.NodeConfig;
import org.nammy.cde.config.SystemConfig;
import org.nammy.cde.model.Resource;

public class ZoneService extends Resource<ZoneService> {
  private static final Logger LOG = Logger.getLogger(ZoneService.class.getSimpleName());

  private final Map<String, NodeService> nodeMap;

  private final TreeSet<NodeService> queue = new TreeSet<>();

  private ZoneService(String name, Map<String, NodeService> nodeMap) {
    super(name, nodeMap.size());
    this.nodeMap = nodeMap;
    this.queue.addAll(nodeMap.values());
  }

  public static ZoneService create(String regionName, String zoneName, SystemConfig systemConfig) {
    Map<String, NodeService> nodeMap = new LinkedHashMap<>();

    for (NodeConfig nodeConfig : systemConfig.getNodeConfigs()) {
      if (nodeConfig.getRegionName().equals(regionName) && nodeConfig.getZoneName().equals(zoneName)) {
        String nodeName = nodeConfig.getNodeName();

        nodeMap.computeIfAbsent(nodeName, arg -> NodeService.create(nodeName, regionName, zoneName));
      }
    }

    return new ZoneService(zoneName, nodeMap);
  }

  public NodeService getNode(String name) {
    return nodeMap.get(name);
  }

  public int getSize() {
    return nodeMap.size();
  }

  public Iterator<NodeService> getNodes() {
    return queue.iterator();
  }

  public NodeService first() {
    return queue.first();
  }

  public void updateLoad(NodeService node, double cost) {
    addLoad(cost);

    queue.remove(node);
    node.updateLoad(cost);
    queue.add(node);
  }

  @Override
  public String toString() {
    return getClass().getSimpleName()
            + "["
            + "name=" + getName()
            + ", weightedLoad=" + getWeightedLoad()
            + ", nodes=" + nodeMap.values()
            + "]";
  }
}
