package com.mbartecki.storesimulator.listeners;

import com.mbartecki.storesimulator.events.PaymentFinishedEvent;
import com.mbartecki.storesimulator.model.Payment;
import com.mbartecki.storesimulator.port.EmailSenderPort;
import com.mbartecki.storesimulator.service.PaymentService;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class PaymentFinishedEventListener implements ApplicationListener<PaymentFinishedEvent> {

  private final PaymentService paymentService;
  private final EmailSenderPort emailSenderPort;

  public PaymentFinishedEventListener(
      PaymentService paymentService,
      EmailSenderPort emailSenderPort) {
    this.paymentService = paymentService;
    this.emailSenderPort = emailSenderPort;
  }

  @Override
  public void onApplicationEvent(PaymentFinishedEvent event) {
    Optional<Payment> finishedPayment = paymentService.getPaymentById(event.getPaymentId());
    if (finishedPayment.isPresent()) {
      emailSenderPort.sendEmail(finishedPayment.get());
    }

  }
}
