package org.nammy.cde.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

import org.nammy.cde.config.NodeConfig;
import org.nammy.cde.config.NodeConfigParser;
import org.nammy.cde.config.SystemConfig;
import org.nammy.cde.model.QueryPlan;
import org.nammy.cde.model.Request;
import org.nammy.cde.model.RequestMethod;
import org.nammy.cde.service.LoadBalancerService;
import org.nammy.cde.service.NodeService;
import org.nammy.cde.strategy.LoadBalancerStrategy;
import org.nammy.cde.strategy.weighted.ZoneRegionWeightedLoadBalancerStrategy;

public class CdeApplication {
  private final LoadBalancerService loadBalancerService;
  private final SystemConfig systemConfig;

  public static void main(String[] args) throws Exception {
    boolean isDebug = false;

    for (String arg : args) {
      if (arg.equals("--debug")) {
        isDebug = true;
      }
    }

    InputStreamReader isReader = new InputStreamReader(System.in);
    BufferedReader reader = new BufferedReader(isReader);

    List<NodeConfig> nodeConfigs = NodeConfigParser.parse(reader);
    CdeApplication application = new CdeApplication(nodeConfigs);

    boolean isPrintDebug = isDebug;
    application.processRequests(new RequestIterator(reader), qp -> printQueryPlan(qp, isPrintDebug));
  }

  public CdeApplication(List<NodeConfig> nodeConfigs) {
    LoadBalancerStrategy loadBalancerStrategy = new ZoneRegionWeightedLoadBalancerStrategy();

    systemConfig = new SystemConfig(nodeConfigs, loadBalancerStrategy);
    loadBalancerService = LoadBalancerService.create(systemConfig);
  }

  public void processRequests(Iterator<Request> requestIterator, Consumer<QueryPlan> queryPlanConsumer) {
    while (requestIterator.hasNext()) {
      Request request = requestIterator.next();

      QueryPlan queryPlan = loadBalancerService.getQueryPlan(request);
      queryPlanConsumer.accept(queryPlan);
    }
  }

  private static void printQueryPlan(QueryPlan queryPlan, boolean isDebug) {
    StringBuilder sb = new StringBuilder();

    sb.append(queryPlan.getRequest().getId());

    for (NodeService nodeService : queryPlan.getNodes()) {
      sb.append(' ');
      sb.append(nodeService.getName());
    }

    if (isDebug) {
      while (sb.length() < 40) {
        sb.append(' ');
      }

      sb.append('-');

      for (NodeService node : queryPlan.getNodes()) {
        sb.append(' ');
        sb.append(node.getWeightedLoad());
      }
    }

    System.out.println(sb);
  }

  private static class RequestIterator implements Iterator<Request> {
    private final BufferedReader reader;
    private String line;

    public RequestIterator(BufferedReader reader) {
      this.reader = reader;
      this.line = readLine();
    }

    @Override
    public boolean hasNext() {
      return line != null;
    }

    @Override
    public Request next() {
      String[] tokens = line.split(" ");

      long id = Long.parseLong(tokens[0]);
      RequestMethod requestMethod = RequestMethod.valueOf(tokens[1]);
      String regionName = tokens[2];
      String zoneName = tokens[3];

      line = readLine();

      return new Request(id, requestMethod, regionName, zoneName);
    }

    private String readLine() {
      String line;

      try {
        while ((line = reader.readLine()) != null) {
          line = line.trim();

          if (line.length() > 0) {
            break;
          }
        }
      } catch (IOException e) {
        throw new RuntimeException(e);
      }

      return line;
    }
  }
}
