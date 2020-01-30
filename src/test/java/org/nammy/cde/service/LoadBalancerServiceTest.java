package org.nammy.cde.service;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.nammy.cde.config.NodeConfig;
import org.nammy.cde.config.SystemConfig;
import org.nammy.cde.model.QueryPlan;
import org.nammy.cde.model.Request;
import org.nammy.cde.model.RequestMethod;
import org.nammy.cde.strategy.LoadBalancerStrategy;
import org.nammy.cde.strategy.weighted.ZoneRegionWeightedLoadBalancerStrategy;

@RunWith(MockitoJUnitRunner.class)
public class LoadBalancerServiceTest {
  @Test
  public void test_largeInput() {
    List<NodeConfig> nodeConfigs = new ArrayList<>();

    for (int i = 0; i < 3; i++) {
      String regionName = "r" + i;

      for (int j = 0; j < 4; j++) {
        String zoneName = "z" + i;

        for (int k = 0; k < 1000; k++) {
          String nodeName = "n" + i + "_" + j + "_" + k;

          nodeConfigs.add(new NodeConfig(nodeName, regionName, zoneName));
        }
      }
    }

    LoadBalancerStrategy strategy = new ZoneRegionWeightedLoadBalancerStrategy();
    SystemConfig systemConfig = new SystemConfig(nodeConfigs, strategy);

    LoadBalancerService service = LoadBalancerService.create(systemConfig);
    runTest(service);
  }

  private void runTest(LoadBalancerService service) {
    int hashCode = 0;

    for (long i = 0; i < 100000L; i++) {
      Request request = new Request(10000 + i, RequestMethod.GET, "r0", "z0");

      QueryPlan queryPlan = service.getQueryPlan(request);

      StringBuilder sb = new StringBuilder();

      for (NodeService node : queryPlan.getNodes()) {
        sb.append(node.getName());
        sb.append(" ");
      }

      hashCode += System.identityHashCode(sb.toString());
    }

    System.out.println(hashCode);
  }
}
