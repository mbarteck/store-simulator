package com.mbartecki.storesimulator.controller;

import com.mbartecki.storesimulator.dto.OrderRequest;
import com.mbartecki.storesimulator.port.CheckoutPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
class CheckoutController {

  private final CheckoutPort checkoutPort;

  CheckoutController(CheckoutPort checkoutPort) {this.checkoutPort = checkoutPort;}

  @PostMapping("/checkout")
  public ResponseEntity<String> receiveCart(@RequestBody OrderRequest cartRequest) {
    try {
      checkoutPort.process(cartRequest);
      return ResponseEntity.ok("Successfully processed the order");
    } catch (Exception e) {
      return ResponseEntity
          .status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body("Failed to process the order.");
    }
  }
}
