package org.nammy.cde.config;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import org.nammy.cde.service.NodeService;
import org.nammy.cde.service.RegionService;
import org.nammy.cde.service.SystemService;
import org.nammy.cde.service.ZoneService;

public class SystemConfig {
  private final List<NodeConfig> nodeConfigs;

  public SystemConfig(List<NodeConfig> nodeConfigs) {
    this.nodeConfigs = nodeConfigs;
  }

  public static SystemConfig parse(InputStream is) throws IOException {
    Reader reader = new InputStreamReader(is);

    return parse(new BufferedReader(reader));
  }

  public static SystemConfig parse(BufferedReader reader) throws IOException {
    List<NodeConfig> nodeConfigs = parseNodeConfig(reader);

    return new SystemConfig(nodeConfigs);
  }

  private static List<NodeConfig> parseNodeConfig(BufferedReader reader) throws IOException {
    List<NodeConfig> nodeConfigs = new ArrayList<>();

    String line = reader.readLine();

    for (String nodeToken : line.split(",")) {
      String[] values = nodeToken.split(" ");

      String nodeName = values[0];
      String regionName = values[1];
      String zoneName = values[2];

      NodeConfig nodeConfig = new NodeConfig(nodeName, regionName, zoneName);

      nodeConfigs.add(nodeConfig);
    }

    return nodeConfigs;
  }

  public List<NodeConfig> getNodeConfigs() {
    return nodeConfigs;
  }

  public SystemService configure(SystemService systemService) {
    for (NodeConfig nodeConfig : getNodeConfigs()) {
      String nodeName = nodeConfig.getNodeName();
      String regionName = nodeConfig.getRegionName();
      String zoneName = nodeConfig.getZoneName();

      RegionService region = systemService.getOrCreateRegion(regionName);
      ZoneService zone = region.getOrCreateZone(zoneName);
      NodeService node = zone.getOrCreateNode(nodeName);
    }

    return systemService;
  }
}
