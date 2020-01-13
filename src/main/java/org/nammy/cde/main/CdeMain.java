package org.nammy.cde.main;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.nammy.cde.config.SystemConfig;
import org.nammy.cde.service.SystemService;

public class CdeMain {
  private static final int REQUEST_BATCHING_SIZE = 1024 * 16;

  public static void main(String[] args) throws Exception {
    InputStreamReader isReader = new InputStreamReader(System.in);
    BufferedReader reader = new BufferedReader(isReader);

    SystemService systemService = SystemService.create();

    SystemConfig systemConfig = SystemConfig.parse(reader);
    systemConfig.configure(systemService);

    System.out.println("hello");
  }
}
