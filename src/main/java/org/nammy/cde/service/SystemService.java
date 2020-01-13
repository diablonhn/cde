package org.nammy.cde.service;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.nammy.cde.config.NodeConfig;
import org.nammy.cde.config.SystemConfig;

public class SystemService {
  private static final Logger LOG = Logger.getLogger(SystemService.class.getSimpleName());

  private final Map<String, RegionService> regionMap = new LinkedHashMap<>();

  private SystemService() {
  }

  public static SystemService create() {
    return new SystemService();
  }

  public RegionService getRegion(String name) {
    return regionMap.get(name);
  }

  public RegionService getOrCreateRegion(String name) {
    return regionMap.computeIfAbsent(name, this::createRegion);
  }

  private RegionService createRegion(String name) {
    RegionService region = RegionService.create(this, name);

    LOG.config("created region=" + region);
    return region;
  }

  public Collection<RegionService> getRegions() {
    return regionMap.values();
  }
}
