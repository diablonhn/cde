package org.nammy.cde.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.TreeSet;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.nammy.cde.config.NodeConfig;
import org.nammy.cde.config.SystemConfig;
import org.nammy.cde.model.Resource;

public class RegionService extends Resource<RegionService> {
  private static final Logger LOG = Logger.getLogger(RegionService.class.getSimpleName());

  private final Map<String, ZoneService> zoneMap;
  private final TreeSet<ZoneService> queue = new TreeSet<>();

  private RegionService(String name, Map<String, ZoneService> zoneMap) {
    super(name, zoneMap.size());
    this.zoneMap = zoneMap;
    this.queue.addAll(zoneMap.values());
  }

  public static RegionService create(String regionName, SystemConfig systemConfig) {
    Map<String, ZoneService> zoneMap = new LinkedHashMap<>();

    for (NodeConfig nodeConfig : systemConfig.getNodeConfigs()) {
      if (nodeConfig.getRegionName().equals(regionName)) {
        String zoneName = nodeConfig.getZoneName();

        zoneMap.computeIfAbsent(zoneName, arg -> ZoneService.create(regionName, zoneName, systemConfig));
      }
    }

    return new RegionService(regionName, zoneMap);
  }

  public ZoneService getZone(String zoneName) {
    return zoneMap.get(zoneName);
  }

  public int getSize() {
    return zoneMap.size();
  }

  public List<ZoneService> getZones() {
    return new ArrayList<>(queue);
  }

  public ZoneService first() {
    return queue.first();
  }

  public void updateLoad(NodeService node, double cost) {
    addLoad(cost);

    ZoneService zone = getZone(node.getZoneName());

    queue.remove(zone);
    zone.updateLoad(node, cost);
    queue.add(zone);
  }

  @Override
  public String toString() {
    return getClass().getSimpleName()
            + "["
            + "name=" + getName()
            + ", weightedLoad=" + getWeightedLoad()
            + ", zones=" + zoneMap.values()
            + "]";
  }
}
