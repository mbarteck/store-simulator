package com.mbartecki.storesimulator.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mbartecki.storesimulator.model.OutboxEvent;
import com.mbartecki.storesimulator.model.Payment;
import com.mbartecki.storesimulator.model.PaymentStatus;
import com.mbartecki.storesimulator.repository.OutboxEventRepository;
import com.mbartecki.storesimulator.repository.PaymentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;


@Service
public class PaymentService {

  private final PaymentRepository paymentRepository;
  private final OutboxEventRepository outboxEventRepository;

  public PaymentService(
      PaymentRepository paymentRepository, OutboxEventRepository outboxEventRepository) {
    this.paymentRepository = paymentRepository;
    this.outboxEventRepository = outboxEventRepository;
  }

  @Transactional
  public Payment createPayment(Payment payment) {
    Payment savedPayment = paymentRepository.save(payment);
    var paymentId = savedPayment.getId().toString();

    OutboxEvent paymentCreatedEvent = OutboxEvent.builder()
            .eventName("payment-created-topic")
            .eventPayload(paymentId)
            .build();
    outboxEventRepository.save(paymentCreatedEvent);

    OutboxEvent sendEmailEvent = OutboxEvent.builder()
        .eventName("send-email-topic")
        .eventPayload(paymentId)
        .build();
    outboxEventRepository.save(sendEmailEvent);
    return savedPayment;
  }

  @Transactional
  public void updatePaymentStatus(UUID paymentId, PaymentStatus status) {
    paymentRepository.updatePaymentStatus(paymentId, status);

    OutboxEvent sendEmailEvent = OutboxEvent.builder()
        .eventName("send-email-topic")
        .eventPayload(paymentId.toString())
        .build();
    outboxEventRepository.save(sendEmailEvent);
  }

  @Transactional(readOnly = true)
  public Optional<Payment> getPaymentById(UUID paymentId) {
    return paymentRepository.findById(paymentId);
  }
}
