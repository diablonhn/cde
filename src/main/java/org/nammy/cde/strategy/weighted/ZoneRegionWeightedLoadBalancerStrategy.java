package org.nammy.cde.strategy.weighted;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.nammy.cde.model.QueryPlan;
import org.nammy.cde.model.Request;
import org.nammy.cde.model.Resource;
import org.nammy.cde.service.NodeService;
import org.nammy.cde.service.RegionService;
import org.nammy.cde.service.SystemService;
import org.nammy.cde.service.ZoneService;
import org.nammy.cde.strategy.LoadBalancerStrategy;

public class ZoneRegionWeightedLoadBalancerStrategy implements LoadBalancerStrategy {
  private static final double CROSS_REGION_COST_FACTOR = 4;
  private static final double CROSS_ZONE_COST_FACTOR = 2;

  private SystemService systemService;

  @Override
  public void init(SystemService systemService) {
    this.systemService = systemService;
  }

  @Override
  public QueryPlan getQueryPlan(Request request) {
    return selectRegions(request);
  }

  private QueryPlan selectRegions(Request request) {
    RegionService primaryRegion = systemService.getRegion(request.getRegionName());

    List<RegionService> regions = pick(primaryRegion, systemService.getRegions(), CROSS_REGION_COST_FACTOR);

    return selectZones(request, regions);
  }

  private QueryPlan selectZones(Request request, List<RegionService> regions) {
    RegionService primaryRegion = regions.get(0);
    List<ZoneService> zones = primaryRegion.getZones();
    ZoneService primaryZone = primaryRegion.getZone(request.getZoneName());

    zones = pick(primaryZone, zones, CROSS_ZONE_COST_FACTOR);

    return selectNodes(request, regions, zones);
  }

  private QueryPlan selectNodes(Request request, List<RegionService> regions, List<ZoneService> zones) {
    ZoneService primaryZone = zones.get(0);
    Iterator<NodeService> primaryZoneNodes = primaryZone.getNodes();

    QueryPlan.QueryPlanBuilder builder = QueryPlan.builder(regions.size() * zones.size() * primaryZone.getSize());

    // 1. add two nodes from local zone of local region
    builder.addNode(primaryZoneNodes.next());

    if (!request.isImmutable()) {
      if (primaryZoneNodes.hasNext()) {
        builder.addNode(primaryZoneNodes.next());
      }

      // 2. add one node from remote zone of local region
      if (zones.size() > 1) {
        builder.addNode(zones.get(1).first());
      }

      // 3. add one node from remote region
      if (regions.size() > 1) {
        ZoneService zone = regions.get(1).first();
        builder.addNode(zone.first());
      }

      //
      // 4. add the rest of the nodes
      //

      addNodesFromZones(builder, zones);

      for (RegionService region : regions) {
        addNodesFromZones(builder, region.getZones());
      }
    }

    QueryPlan plan = builder.build(request);

    //
    // 5. update the load stats
    //

    updateLoadStats(request, plan.getNodes());

    return plan;
  }

  private void addNodesFromZones(QueryPlan.QueryPlanBuilder builder, List<ZoneService> zones) {
    for (ZoneService zone : zones) {
      addNodes(builder, zone.getNodes());
    }
  }

  private void addNodes(QueryPlan.QueryPlanBuilder builder, Iterator<NodeService> nodes) {
    while (nodes.hasNext()) {
      builder.addNode(nodes.next());
    }
  }

  private <T extends Resource> List<T> pick(T primary, List<T> list, double crossResourceCostFactor) {
    double primaryCostFactor = 1.0;

    if (primary == null) {
      primary = list.get(0);
      primaryCostFactor = crossResourceCostFactor;
    }

    T secondary = null;

    for (T resource : list) {
      secondary = resource;

      if (secondary != primary) {
        break;
      }
    }

    if (secondary != null) {
      double primaryCost = primary.getWeightedLoad() * primaryCostFactor;
      double secondaryCost = secondary.getWeightedLoad() * crossResourceCostFactor;

      // pick the least loaded resource, taking into account the cost
      if (secondaryCost < primaryCost) {
        T temp = primary;
        primary = secondary;
        secondary = temp;
      }
    }

    List<T> result = new ArrayList<>();

    result.add(primary);
    if (secondary != null) {
      result.add(secondary);
    }

    for (T resource : list) {
      if (resource != primary && resource != secondary) {
        result.add(resource);
      }
    }

    return result;
  }

  private void updateLoadStats(Request request, List<NodeService> nodes) {
    if (nodes.size() > 0) {
      updateLoadStatsForNode(request, nodes.get(0), 1.0);
    }

    if (nodes.size() > 1) {
      updateLoadStatsForNode(request, nodes.get(1), 0.99);
    }

    if (nodes.size() > 2) {
      updateLoadStatsForNode(request, nodes.get(2), 0.01);
    }

    if (nodes.size() > 3) {
      updateLoadStatsForNode(request, nodes.get(3), 0.0099);
    }
  }

  private void updateLoadStatsForNode(Request request, NodeService node, double cost) {
    if (!request.getRegionName().equals(node.getRegionName())) {
      cost *= CROSS_REGION_COST_FACTOR;
    } else if (!request.getZoneName().equals(node.getZoneName())) {
      cost *= CROSS_ZONE_COST_FACTOR;
    }

    systemService.updateLoad(node, cost);
  }
}
