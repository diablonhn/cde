package org.nammy.cde.service;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Logger;

public class RegionService {
  private static final Logger LOG = Logger.getLogger(RegionService.class.getSimpleName());

  private final Map<String, ZoneService> zoneMap = new LinkedHashMap<>();

  private final SystemService systemService;
  private final String name;

  private RegionService(SystemService systemService, String name) {
    this.systemService = systemService;
    this.name = name;
  }

  public static RegionService create(SystemService systemService, String name) {
    return new RegionService(systemService, name);
  }

  public String getName() {
    return name;
  }

  public ZoneService getZone(String zoneName) {
    return zoneMap.get(zoneName);
  }

  public ZoneService getOrCreateZone(String zoneName) {
    return zoneMap.computeIfAbsent(zoneName, this::createZone);
  }

  public ZoneService createZone(String name) {
    ZoneService zone = ZoneService.create(this, name);

    LOG.info("created zone=" + zone);

    return zone;
  }

  public Collection<ZoneService> getZones() {
    return zoneMap.values();
  }

  @Override
  public String toString() {
    return getClass().getSimpleName()
            + "["
            + "name=" + name
            + "zones=" + zoneMap.values()
            + "]";
  }
}
