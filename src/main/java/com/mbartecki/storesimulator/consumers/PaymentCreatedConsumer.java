package com.mbartecki.storesimulator.consumers;

import lombok.extern.slf4j.Slf4j;

import com.mbartecki.storesimulator.model.Payment;
import com.mbartecki.storesimulator.model.PaymentStatus;
import com.mbartecki.storesimulator.port.PaymentProviderPort;
import com.mbartecki.storesimulator.service.PaymentService;
import org.springframework.kafka.annotation.DltHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.retrytopic.TopicSuffixingStrategy;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.kafka.support.serializer.DeserializationException;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@Slf4j
public class PaymentCreatedConsumer {

  private final PaymentService paymentService;
  private final PaymentProviderPort paymentProviderPort;

  public PaymentCreatedConsumer(
      PaymentService paymentService, PaymentProviderPort paymentProviderPort) {
    this.paymentService = paymentService;
    this.paymentProviderPort = paymentProviderPort;
  }

  @RetryableTopic(
      attempts = "5",
      topicSuffixingStrategy = TopicSuffixingStrategy.SUFFIX_WITH_INDEX_VALUE,
      backoff = @Backoff(delay = 1000, multiplier = 2.0),
      exclude = { DeserializationException.class }
  )
  @KafkaListener(topics = "payment-created-topic")
  public void processNewPayment(String paymentId) {
    Optional<Payment> optionalPayment = paymentService.getById(UUID.fromString(paymentId));
    if (optionalPayment.isPresent()) {
      Payment payment = optionalPayment.get();
      if (PaymentStatus.NEW.equals(payment.getStatus())) {
        PaymentStatus status = paymentProviderPort.charge(payment);
        payment.setStatus(status);
        paymentService.save(payment);
      } else {
        log.warn("Payment with ID " + paymentId + " has already been completed.");
      }
    } else {
      log.warn("Payment with ID " + paymentId + " not found.");
    }
  }

  @DltHandler
  public void handleDlt(String message, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
    log.info("Message: {} handled by dlq topic: {}", message, topic);
  }
}
