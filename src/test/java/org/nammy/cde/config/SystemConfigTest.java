package org.nammy.cde.config;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.nammy.cde.strategy.LoadBalancerStrategy;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class SystemConfigTest {
  @Mock
  private LoadBalancerStrategy strategy;

  @Test
  public void test_parse_getNodeConfigs_oneNode() throws Exception {
    String input = "node1 region1 zone1";
    BufferedReader reader = new BufferedReader(new StringReader(input));

    SystemConfig configParser = new SystemConfig(NodeConfigParser.parse(reader), strategy);
    List<NodeConfig> nodeConfigs = configParser.getNodeConfigs();

    assertEquals(1, nodeConfigs.size());
    assertEquals(new NodeConfig("node1", "region1", "zone1"), nodeConfigs.get(0));
  }

  @Test
  public void test_parse_getNodeConfigs_twoNodes() throws Exception {
    String input = "node1 region1 zone1,node2 region2 zone2";
    BufferedReader reader = new BufferedReader(new StringReader(input));

    SystemConfig configParser = new SystemConfig(NodeConfigParser.parse(reader), strategy);
    List<NodeConfig> nodeConfigs = configParser.getNodeConfigs();

    assertEquals(2, nodeConfigs.size());
    assertEquals(new NodeConfig("node1", "region1", "zone1"), nodeConfigs.get(0));
    assertEquals(new NodeConfig("node2", "region2", "zone2"), nodeConfigs.get(1));
  }

  @Test
  public void test_parse_getNodeConfigs_file() throws Exception {
    InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("example.txt");
    Reader reader = new InputStreamReader(is);
    SystemConfig configParser = new SystemConfig(NodeConfigParser.parse(is), strategy);

    List<NodeConfig> nodeConfigs = configParser.getNodeConfigs();

    assertEquals(4, nodeConfigs.size());
    assertEquals(new NodeConfig("node1", "region1", "zone1"), nodeConfigs.get(0));
    assertEquals(new NodeConfig("node2", "region1", "zone1"), nodeConfigs.get(1));
    assertEquals(new NodeConfig("node3", "region2", "zone1"), nodeConfigs.get(2));
    assertEquals(new NodeConfig("node4", "region2", "zone2"), nodeConfigs.get(3));

  }
}