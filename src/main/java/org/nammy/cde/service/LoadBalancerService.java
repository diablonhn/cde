package org.nammy.cde.service;

import java.util.logging.Logger;

import org.nammy.cde.config.SystemConfig;
import org.nammy.cde.model.QueryPlan;
import org.nammy.cde.model.Request;
import org.nammy.cde.strategy.LoadBalancerStrategy;

public class LoadBalancerService {
  private static final Logger LOG = Logger.getLogger(SystemService.class.getSimpleName());

  private final LoadBalancerStrategy strategy;

  private LoadBalancerService(LoadBalancerStrategy strategy) {
    this.strategy = strategy;
  }

  public static LoadBalancerService create(SystemConfig systemConfig) {
    SystemService systemService = SystemService.create(systemConfig);
    LoadBalancerStrategy loadBalancerStrategy = systemConfig.getLoadBalancerStrategy();

    loadBalancerStrategy.init(systemService);

    return new LoadBalancerService(loadBalancerStrategy);
  }

  public QueryPlan getQueryPlan(Request request) {
    return strategy.getQueryPlan(request);
  }

}
