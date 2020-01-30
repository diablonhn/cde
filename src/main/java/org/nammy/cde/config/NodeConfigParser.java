package org.nammy.cde.config;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

public class NodeConfigParser {
  public static List<NodeConfig> parse(InputStream is) throws IOException {
    return parse(new InputStreamReader(is));
  }

  public static List<NodeConfig> parse(Reader reader) throws IOException {
    return parse(new BufferedReader(reader));
  }

  public static List<NodeConfig> parse(BufferedReader reader) throws IOException {
    return parseNodeConfig(reader);
  }

  private static List<NodeConfig> parseNodeConfig(BufferedReader reader) throws IOException {
    List<NodeConfig> nodeConfigs = new ArrayList<>();

    String line = reader.readLine();

    if (line != null) {
      for (String nodeToken : line.split(",")) {
        nodeToken = nodeToken.trim();
        String[] values = nodeToken.split(" ");

        String nodeName = values[0];
        String regionName = values[1];
        String zoneName = values[2];

        NodeConfig nodeConfig = new NodeConfig(nodeName, regionName, zoneName);
        nodeConfigs.add(nodeConfig);
      }
    }

    return nodeConfigs;
  }
}
