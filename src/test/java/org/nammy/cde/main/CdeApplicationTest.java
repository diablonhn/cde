package org.nammy.cde.main;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;
import org.nammy.cde.config.NodeConfig;
import org.nammy.cde.model.QueryPlan;
import org.nammy.cde.model.Request;
import org.nammy.cde.model.RequestMethod;
import org.nammy.cde.service.NodeService;

import static org.junit.Assert.assertEquals;

public class CdeApplicationTest {
  @Test
  public void test_main() throws Exception {
    InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("example.txt");

    System.setIn(is);

    CdeApplication.main(new String[] { "--debug" });
  }

  @Test
  public void test_largeInputImmutable() throws Exception {
    List<NodeConfig> nodeConfigs = new ArrayList<>();

    for (int i = 0; i < 1000; i++) {
      nodeConfigs.add(new NodeConfig("" + (1000 + i), "r0", "z0"));
    }

    List<Request> requests = new ArrayList<>();
    for (int i = 0; i < 1000; i++) {
      requests.add(new Request(1000 + i, RequestMethod.POST, "r0", "z0"));
    }


    List<QueryPlan> results = new ArrayList<>();

    CdeApplication application = new CdeApplication(nodeConfigs);
    application.processRequests(requests.iterator(), results::add);

    List<NodeService> list = new ArrayList<>();

    for (QueryPlan queryPlan : results) {
      list.add(queryPlan.getNodes().get(0));
    }

    assertEquals(requests.size(), list.size());
  }

  @Test
  public void test_largeInput() throws Exception {
    List<NodeConfig> nodeConfigs = new ArrayList<>();

    for (int i = 0; i < 1000; i++) {
      nodeConfigs.add(new NodeConfig("" + (10000 + i), "r0", "z0"));
    }

    List<Request> requests = new ArrayList<>();
    for (long i = 0; i < 1000L; i++) {
      requests.add(new Request(10000 + i, RequestMethod.GET, "r0", "z0"));
    }

    List<NodeService> list = new ArrayList<>();

    CdeApplication application = new CdeApplication(nodeConfigs);
    application.processRequests(requests.iterator(), queryPlan -> list.add(queryPlan.getNodes().get(0)));

    assertEquals(requests.size(), list.size());
  }
}
