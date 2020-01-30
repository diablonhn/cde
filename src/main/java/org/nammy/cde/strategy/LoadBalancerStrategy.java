package org.nammy.cde.strategy;

import org.nammy.cde.model.QueryPlan;
import org.nammy.cde.model.Request;
import org.nammy.cde.service.SystemService;

public interface LoadBalancerStrategy {
  void init(SystemService systemService);

  QueryPlan getQueryPlan(Request request);
}
