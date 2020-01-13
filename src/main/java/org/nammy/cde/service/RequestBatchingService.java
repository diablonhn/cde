package org.nammy.cde.service;

import java.util.ArrayList;
import java.util.List;

import org.nammy.cde.model.Request;

public class RequestBatchingService {
  private final List<Request> requests = new ArrayList<>();
  private final int batchSize;

  private final RequestBatchListener listener;

  private RequestBatchingService(int batchSize, RequestBatchListener listener) {
    this.batchSize = batchSize;
    this.listener = listener;
  }

  public void addRequest(Request request) {
    requests.add(request);

    maybeFlush();
  }

  private void maybeFlush() {
  }

  public void flush() {
    listener.onBatch(new ArrayList<>(requests));
    requests.clear();
  }

  public interface RequestBatchListener {
    void onBatch(List<Request> requests);
  }
}
