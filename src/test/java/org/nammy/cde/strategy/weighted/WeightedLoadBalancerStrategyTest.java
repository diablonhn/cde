package org.nammy.cde.strategy.weighted;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.nammy.cde.config.NodeConfig;
import org.nammy.cde.config.NodeConfigParser;
import org.nammy.cde.config.SystemConfig;
import org.nammy.cde.model.QueryPlan;
import org.nammy.cde.model.Request;
import org.nammy.cde.model.RequestMethod;
import org.nammy.cde.service.LoadBalancerService;
import org.nammy.cde.service.NodeService;
import org.nammy.cde.strategy.LoadBalancerStrategy;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class WeightedLoadBalancerStrategyTest {
  @Test
  public void test_get_balanced() {
    NodeConfig nodeConfig0 = new NodeConfig("n0", "r0", "z0");
    NodeConfig nodeConfig1 = new NodeConfig("n1", "r0", "z0");

    LoadBalancerStrategy strategy = new ZoneRegionWeightedLoadBalancerStrategy();
    SystemConfig systemConfig = new SystemConfig(Arrays.asList(nodeConfig0, nodeConfig1), strategy);

    LoadBalancerService service = LoadBalancerService.create(systemConfig);

    int count0 = 0;
    int count1 = 0;

    for (int i = 0; i < 1000 * 10; i++) {
      Request request = new Request(i, RequestMethod.GET, "r0", "z0");
      QueryPlan plan = service.getQueryPlan(request);

      if (plan.getNodes().get(0).getName().equals(nodeConfig0.getNodeName())) {
        count0++;
      } else {
        count1++;
      }
    }

    assertEquals(count0, count1);
  }

  @Test
  public void test_post_balanced() {
    NodeConfig nodeConfig0 = new NodeConfig("n0", "r0", "z0");
    NodeConfig nodeConfig1 = new NodeConfig("n1", "r0", "z0");

    LoadBalancerStrategy strategy = new ZoneRegionWeightedLoadBalancerStrategy();
    SystemConfig systemConfig = new SystemConfig(Arrays.asList(nodeConfig0, nodeConfig1), strategy);

    LoadBalancerService service = LoadBalancerService.create(systemConfig);

    int count0 = 0;
    int count1 = 0;

    for (int i = 0; i < 1000 * 10; i++) {
      Request request = new Request(i, RequestMethod.POST, "r0", "z0");
      QueryPlan plan = service.getQueryPlan(request);

      if (plan.getNodes().get(0).getName().equals(nodeConfig0.getNodeName())) {
        count0++;
      } else {
        count1++;
      }
    }

    assertEquals(count0, count1);
  }

  @Test
  public void test_getPost_balanced() {
    NodeConfig nodeConfig0 = new NodeConfig("n0", "r0", "z0");
    NodeConfig nodeConfig1 = new NodeConfig("n1", "r0", "z0");

    LoadBalancerStrategy strategy = new ZoneRegionWeightedLoadBalancerStrategy();
    SystemConfig systemConfig = new SystemConfig(Arrays.asList(nodeConfig0, nodeConfig1), strategy);

    LoadBalancerService service = LoadBalancerService.create(systemConfig);

    int count0 = 0;
    int count1 = 0;

    for (int i = 0; i < 1000 * 10; i++) {
      RequestMethod method = i % 5 == 0 ? RequestMethod.POST : RequestMethod.GET;

      Request request = new Request(i,method, "r0", "z0");
      QueryPlan plan = service.getQueryPlan(request);

      if (plan.getNodes().get(0).getName().equals(nodeConfig0.getNodeName())) {
        count0++;
      } else {
        count1++;
      }
    }

    assertEquals(count0, count1, 10);
  }

  @Test
  public void test_example() throws Exception {
    InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("example.txt");
    List<NodeConfig> nodeConfigs = NodeConfigParser.parse(is);

    LoadBalancerStrategy strategy = new ZoneRegionWeightedLoadBalancerStrategy();
    SystemConfig systemConfig = new SystemConfig(nodeConfigs, strategy);

    LoadBalancerService service = LoadBalancerService.create(systemConfig);

    {
      Request request = new Request(1, RequestMethod.GET, "region1", "zone1");
      QueryPlan plan = service.getQueryPlan(request);
      assertNodesEquals(plan, "node1 node2 node3 node4");
    }

    {
      Request request = new Request(2, RequestMethod.POST, "region1", "zone1");
      QueryPlan plan = service.getQueryPlan(request);
      assertNodesEquals(plan, "node2");
    }

    {
      Request request = new Request(3, RequestMethod.GET, "region2", "zone3");
      QueryPlan plan = service.getQueryPlan(request);
      assertNodesEquals(plan, "node4 node3 node1 node2");
    }

    {
      Request request = new Request(4, RequestMethod.GET, "region1", "zone1");
      QueryPlan plan = service.getQueryPlan(request);
      assertNodesEquals(plan, "node1 node2 node3 node4");
    }

    {
      Request request = new Request(5, RequestMethod.GET, "region2", "zone1");
      QueryPlan plan = service.getQueryPlan(request);
      assertNodesEquals(plan, "node3 node4 node1 node2");
    }

    {
      Request request = new Request(6, RequestMethod.POST, "region1", "zone1");
      QueryPlan plan = service.getQueryPlan(request);
      assertNodesEquals(plan, "node1");
    }
  }

  private void assertNodesEquals(QueryPlan plan, String nodeNames) {
    String actualNodeNames = plan.getNodes().stream()
            .map(NodeService::getName)
            .collect(Collectors.joining(" "));

    assertEquals(nodeNames, actualNodeNames);
  }
}
