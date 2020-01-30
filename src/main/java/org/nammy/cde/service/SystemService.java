package org.nammy.cde.service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.logging.Logger;

import org.nammy.cde.config.NodeConfig;
import org.nammy.cde.config.SystemConfig;

public class SystemService {
  private static final Logger LOG = Logger.getLogger(SystemService.class.getSimpleName());

  private final Map<String, RegionService> regionMap;
  private final TreeSet<RegionService> queue = new TreeSet<>();

  private SystemService(Map<String, RegionService> regionMap) {
    this.regionMap = regionMap;
    this.queue.addAll(regionMap.values());
  }

  public static SystemService create(SystemConfig systemConfig) {
    Map<String, RegionService> regionMap = new LinkedHashMap<>();

    for (NodeConfig nodeConfig : systemConfig.getNodeConfigs()) {
      String regionName = nodeConfig.getRegionName();

      regionMap.computeIfAbsent(regionName, name -> RegionService.create(name, systemConfig));
    }

    return new SystemService(regionMap);
  }

  public RegionService getRegion(String name) {
    return regionMap.get(name);
  }

  public List<RegionService> getRegions() {
    return new ArrayList<>(queue);
  }

  public void updateLoad(NodeService node, double cost) {
    RegionService region = getRegion(node.getRegionName());

    queue.remove(region);
    region.updateLoad(node, cost);
    queue.add(region);
  }
}
