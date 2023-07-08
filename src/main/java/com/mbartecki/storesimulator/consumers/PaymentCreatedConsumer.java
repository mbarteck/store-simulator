package com.mbartecki.storesimulator.consumers;

import com.mbartecki.storesimulator.events.PaymentFinishedEvent;
import com.mbartecki.storesimulator.model.Payment;
import com.mbartecki.storesimulator.model.PaymentStatus;
import com.mbartecki.storesimulator.port.PaymentProviderPort;
import com.mbartecki.storesimulator.service.PaymentService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class PaymentCreatedConsumer {

  private final PaymentService paymentService;
  private final PaymentProviderPort paymentProviderPort;
  private final ApplicationEventPublisher eventPublisher;


  public PaymentCreatedConsumer(
      PaymentService paymentService, PaymentProviderPort paymentProviderPort,
      ApplicationEventPublisher eventPublisher) {
    this.paymentService = paymentService;
    this.paymentProviderPort = paymentProviderPort;
    this.eventPublisher = eventPublisher;
  }

  @KafkaListener(topics = "payment-created-topic")
  public void receivePaymentId(String paymentIdString) {
    Optional<Payment> payment = paymentService.getPaymentById(UUID.fromString(paymentIdString));
    if (payment.isPresent()) {
      //TODO implement re-try
      Payment actualPayment = payment.get();
      PaymentStatus status = paymentProviderPort.charge(actualPayment);
      paymentService.updatePaymentStatus(actualPayment.getId(), status);

      //TODO publish event only if update succeeded
      eventPublisher.publishEvent(new PaymentFinishedEvent(actualPayment.getId(), status));
    }
  }
}
