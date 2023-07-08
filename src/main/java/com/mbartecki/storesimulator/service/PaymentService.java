package com.mbartecki.storesimulator.service;

import com.mbartecki.storesimulator.model.OutboxEvent;
import com.mbartecki.storesimulator.model.Payment;
import com.mbartecki.storesimulator.model.PaymentStatus;
import com.mbartecki.storesimulator.repository.OutboxEventRepository;
import com.mbartecki.storesimulator.repository.PaymentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

import static com.mbartecki.storesimulator.utils.EmailUtils.createEmail;
import static com.mbartecki.storesimulator.utils.JsonUtils.serialize;


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
  public Payment save(Payment payment) {
    Payment savedPayment = paymentRepository.save(payment);
    var paymentId = savedPayment.getId().toString();

    if (PaymentStatus.NEW.equals(savedPayment.getStatus())) {
      OutboxEvent paymentCreatedEvent = OutboxEvent.builder()
          .eventName("payment-created-topic")
          .eventPayload(paymentId)
          .build();
      outboxEventRepository.save(paymentCreatedEvent);
    }

    OutboxEvent sendEmailEvent = OutboxEvent.builder()
        .eventName("send-email-topic")
        .eventPayload(serialize(createEmail(savedPayment)))
        .build();
    outboxEventRepository.save(sendEmailEvent);
    return savedPayment;
  }

  @Transactional(readOnly = true)
  public Optional<Payment> getById(UUID paymentId) {
    return paymentRepository.findById(paymentId);
  }
}
