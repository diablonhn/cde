package org.nammy.cde.model;

public enum RequestMethod {
  GET,
  POST;

  public boolean isImmutable() {
    return this == POST;
  }
}
