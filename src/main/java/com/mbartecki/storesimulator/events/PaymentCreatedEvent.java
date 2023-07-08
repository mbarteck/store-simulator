package com.mbartecki.storesimulator.events;

import lombok.Getter;

import com.mbartecki.storesimulator.model.Payment;
import org.springframework.context.ApplicationEvent;

import java.util.UUID;

public class PaymentCreatedEvent extends ApplicationEvent {

  @Getter private final UUID paymentId;

  public PaymentCreatedEvent(UUID paymentId) {
    super(paymentId);
    this.paymentId = paymentId;
  }
}
