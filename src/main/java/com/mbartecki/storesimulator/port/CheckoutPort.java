package com.mbartecki.storesimulator.port;

import com.mbartecki.storesimulator.dto.OrderRequest;

public interface CheckoutPort {

  void process(OrderRequest orderRequest);
}
