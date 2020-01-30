package org.nammy.cde.strategy.weighted;

import java.util.Arrays;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.nammy.cde.config.NodeConfig;
import org.nammy.cde.config.SystemConfig;
import org.nammy.cde.model.QueryPlan;
import org.nammy.cde.model.Request;
import org.nammy.cde.model.RequestMethod;
import org.nammy.cde.service.LoadBalancerService;
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
}
