package com.mbartecki.storesimulator.events;

import lombok.Getter;

import com.mbartecki.storesimulator.model.PaymentStatus;
import org.springframework.context.ApplicationEvent;

import java.util.UUID;

public class PaymentFinishedEvent extends ApplicationEvent {

  @Getter private final UUID paymentId;
  @Getter private final PaymentStatus status;

  public PaymentFinishedEvent(UUID paymentId, PaymentStatus status) {
    super(paymentId);
    this.paymentId = paymentId;
    this.status = status;
  }
}
