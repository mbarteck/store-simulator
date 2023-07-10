package com.mbartecki.storesimulator.controller;

import com.mbartecki.storesimulator.dto.OrderRequest;
import com.mbartecki.storesimulator.port.CheckoutPort;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
class CheckoutController {

  private final CheckoutPort checkoutPort;

  CheckoutController(CheckoutPort checkoutPort) {this.checkoutPort = checkoutPort;}

  @PostMapping("/checkout")
  public ResponseEntity<String> receiveCart(
      @Valid @RequestBody OrderRequest cartRequest, BindingResult bindingResult) {
    if (bindingResult.hasErrors()) {
      List<String> errors = bindingResult.getFieldErrors().stream()
          .map(FieldError::getDefaultMessage)
          .collect(Collectors.toList());

      return ResponseEntity.badRequest().body(errors.toString());
    }

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
