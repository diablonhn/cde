package org.nammy.cde.model;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.nammy.cde.service.NodeService;

public class QueryPlan {
  private final List<NodeService> nodes;
  private final Request request;

  public QueryPlan(Request request, List<NodeService> nodes) {
    this.request = request;
    this.nodes = nodes;
  }

  public Request getRequest() {
    return request;
  }

  public List<NodeService> getNodes() {
    return nodes;
  }

  @Override
  public String toString() {
    return getClass().getSimpleName()
            + "["
            + "request=" + request
            + ", plan=" + nodes
            + "]";
  }

  public static QueryPlanBuilder builder(int size) {
    return new QueryPlanBuilder(size);
  }

  public static class QueryPlanBuilder {
    private final Set<NodeService> nodes;

    private QueryPlanBuilder(int size) {
      this.nodes = new LinkedHashSet<>(size);
    }

    public QueryPlanBuilder addNode(NodeService nodeService) {
      if (!nodes.contains(nodeService)) {
        nodes.add(nodeService);
      }

      return this;
    }

    public QueryPlan build(Request request) {
      List<NodeService> nodes = new ArrayList<>(this.nodes);

      return new QueryPlan(request, nodes);
    }
  }
}
