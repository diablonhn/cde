package org.nammy.cde.model;

public class Request {
  private final long id;
  private final RequestMethod requestMethod;
  private final String regionName;
  private final String zoneName;

  public Request(long id, RequestMethod requestMethod, String regionName, String zoneName) {
    this.id = id;
    this.requestMethod = requestMethod;
    this.regionName = regionName;
    this.zoneName = zoneName;
  }

  public long getId() {
    return id;
  }

  public RequestMethod getRequestMethod() {
    return requestMethod;
  }

  public boolean isImmutable() {
    return requestMethod.isImmutable();
  }

  public String getRegionName() {
    return regionName;
  }

  public String getZoneName() {
    return zoneName;
  }

  @Override
  public String toString() {
    return getClass().getSimpleName()
            + "["
            + "id=" + id
            + ", requestMethod=" + regionName
            + ", regionName=" + regionName
            + ", zoneName=" + zoneName
            + "]";
  }
}
