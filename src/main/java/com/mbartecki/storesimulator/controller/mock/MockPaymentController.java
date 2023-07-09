package com.mbartecki.storesimulator.controller.mock;

import com.mbartecki.storesimulator.model.Payment;
import com.mbartecki.storesimulator.model.PaymentStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
class MockPaymentController {

  @PostMapping("/mock/payment")
  public ResponseEntity<PaymentStatus> receiveCart(@RequestBody Payment payment) {
    return ResponseEntity.ok(PaymentStatus.SUCCEEDED);
  }
}
