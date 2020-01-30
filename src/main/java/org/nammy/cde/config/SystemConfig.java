package org.nammy.cde.config;

import java.util.List;

import org.nammy.cde.strategy.LoadBalancerStrategy;

public class SystemConfig {
  private final List<NodeConfig> nodeConfigs;
  private final LoadBalancerStrategy loadBalancerStrategy;

  public SystemConfig(List<NodeConfig> nodeConfigs, LoadBalancerStrategy loadBalancerStrategy) {
    this.nodeConfigs = nodeConfigs;
    this.loadBalancerStrategy = loadBalancerStrategy;
  }

  public List<NodeConfig> getNodeConfigs() {
    return nodeConfigs;
  }

  public LoadBalancerStrategy getLoadBalancerStrategy() {
    return loadBalancerStrategy;
  }
}
