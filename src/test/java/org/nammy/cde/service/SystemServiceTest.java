package org.nammy.cde.service;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.nammy.cde.config.NodeConfig;
import org.nammy.cde.config.SystemConfig;
import org.nammy.cde.strategy.LoadBalancerStrategy;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class SystemServiceTest {
  @Mock
  private LoadBalancerStrategy strategy;

  @Test
  public void test_create_empty() {
    List<NodeConfig> nodeConfigs = Arrays.asList();
    SystemConfig systemConfig = new SystemConfig(nodeConfigs, strategy);

    SystemService service = SystemService.create(systemConfig);

    assertTrue(service.getRegions().isEmpty());
  }

  @Test
  public void test_create_one() {
    NodeConfig nodeConfig0 = new NodeConfig("node0", "region0", "zone0");
    List<NodeConfig> nodeConfigs = Arrays.asList(nodeConfig0);

    SystemConfig systemConfig = new SystemConfig(nodeConfigs, strategy);
    SystemService service = SystemService.create(systemConfig);

    assertEquals(1, service.getRegions().size());

    RegionService region = service.getRegion("region0");
    assertEquals("region0", region.getName());
    assertEquals(1, region.getZones().size());

    ZoneService zone = region.getZone("zone0");
    assertEquals("zone0", zone.getName());
    //assertEquals(1, zone.getNodes().size());

    NodeService node = zone.getNode("node0");
    assertEquals("node0", node.getName());
  }

  @Test
  public void test_create_sameRegionZone() {
    NodeConfig nodeConfig0 = new NodeConfig("node0", "region0", "zone0");
    NodeConfig nodeConfig1 = new NodeConfig("node1", "region0", "zone0");

    List<NodeConfig> nodeConfigs = Arrays.asList(nodeConfig0, nodeConfig1);
    SystemConfig systemConfig = new SystemConfig(nodeConfigs, strategy);

    SystemService service = SystemService.create(systemConfig);
    assertEquals(1, service.getRegions().size());

    RegionService region = service.getRegion("region0");
    assertEquals("region0", region.getName());
    assertEquals(1, region.getZones().size());

    ZoneService zone = region.getZone("zone0");
    assertEquals("zone0", zone.getName());
    //assertEquals(2, zone.getNodes().size());

    {
      NodeService node = zone.getNode("node0");
      assertEquals("node0", node.getName());
    }

    {
      NodeService node = zone.getNode("node1");
      assertEquals("node1", node.getName());
    }
  }

  @Test
  public void test_create_sameRegionDifferentZone() {
    NodeConfig nodeConfig0 = new NodeConfig("node0", "region0", "zone0");
    NodeConfig nodeConfig1 = new NodeConfig("node1", "region0", "zone1");

    List<NodeConfig> nodeConfigs = Arrays.asList(nodeConfig0, nodeConfig1);
    SystemConfig systemConfig = new SystemConfig(nodeConfigs, strategy);

    SystemService service = SystemService.create(systemConfig);    assertEquals(1, service.getRegions().size());

    RegionService region = service.getRegion("region0");
    assertEquals("region0", region.getName());
    assertEquals(2, region.getZones().size());
  }

  @Test
  public void test_create_differentRegionSameZone() {
    NodeConfig nodeConfig0 = new NodeConfig("node0", "region0", "zone0");
    NodeConfig nodeConfig1 = new NodeConfig("node1", "region1", "zone0");

    List<NodeConfig> nodeConfigs = Arrays.asList(nodeConfig0, nodeConfig1);
    SystemConfig systemConfig = new SystemConfig(nodeConfigs, strategy);

    SystemService service = SystemService.create(systemConfig);

    assertEquals(2, service.getRegions().size());

    {
      RegionService region = service.getRegion("region0");
      assertEquals("region0", region.getName());
      assertEquals(1, region.getZones().size());

      ZoneService zone = region.getZone("zone0");
      //assertEquals(1, zone.getNodes().size());

      NodeService node = zone.getNode("node0");
      assertNotNull(node);
    }

    {
      RegionService region = service.getRegion("region1");
      assertEquals("region1", region.getName());
      assertEquals(1, region.getZones().size());

      ZoneService zone = region.getZone("zone0");
      //assertEquals(1, zone.getNodes().size());

      NodeService node = zone.getNode("node1");
      assertNotNull(node);
    }
  }
}
