package com.mbartecki.storesimulator.consumers;

import com.mbartecki.storesimulator.model.Payment;
import com.mbartecki.storesimulator.port.EmailSenderPort;
import com.mbartecki.storesimulator.service.PaymentService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class SendEmailConsumer {

  private final PaymentService paymentService;

  private final EmailSenderPort emailSenderPort;

  public SendEmailConsumer(PaymentService paymentService, EmailSenderPort emailSenderPort) {
    this.paymentService = paymentService;
    this.emailSenderPort = emailSenderPort;
  }


  @KafkaListener(topics = "send-email-topic")
  @Retryable(maxAttempts = 3, backoff = @Backoff(delay = 1000, multiplier = 2.0))
  public void receivePaymentId(String paymentIdString) {
    Optional<Payment> payment = paymentService.getPaymentById(UUID.fromString(paymentIdString));
    if (payment.isPresent()) {
      emailSenderPort.sendEmail(payment.get());
    }
  }
}
