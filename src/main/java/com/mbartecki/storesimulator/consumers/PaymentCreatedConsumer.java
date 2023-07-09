package com.mbartecki.storesimulator.consumers;

import com.mbartecki.storesimulator.model.Payment;
import com.mbartecki.storesimulator.model.PaymentStatus;
import com.mbartecki.storesimulator.port.PaymentProviderPort;
import com.mbartecki.storesimulator.service.PaymentService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Component
public class PaymentCreatedConsumer {

  private final PaymentService paymentService;
  private final PaymentProviderPort paymentProviderPort;

  public PaymentCreatedConsumer(
      PaymentService paymentService, PaymentProviderPort paymentProviderPort) {
    this.paymentService = paymentService;
    this.paymentProviderPort = paymentProviderPort;
  }

  @KafkaListener(topics = "payment-created-topic")
  public void receivePaymentId(String paymentIdString) {
    Optional<Payment> optionalPayment = paymentService.getById(UUID.fromString(paymentIdString));
    if (optionalPayment.isPresent()) {
      //TODO implement re-try
      Payment payment = optionalPayment.get();
      PaymentStatus status = paymentProviderPort.charge(payment);
      payment.setStatus(status);
      payment.setCompletedAt(LocalDateTime.now());
      paymentService.save(payment);
    }
  }
}
