package com.mbartecki.storesimulator.service;

import com.mbartecki.storesimulator.events.PaymentCreatedEvent;
import com.mbartecki.storesimulator.model.Payment;
import com.mbartecki.storesimulator.model.PaymentStatus;
import com.mbartecki.storesimulator.repository.PaymentRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class PaymentService {

  private final PaymentRepository paymentRepository;
  private final ApplicationEventPublisher eventPublisher;

  public PaymentService(
      PaymentRepository paymentRepository, ApplicationEventPublisher eventPublisher) {
    this.paymentRepository = paymentRepository;
    this.eventPublisher = eventPublisher;
  }

  @Transactional
  public Payment createPayment(Payment payment) {
    // Step 1: Initiate a database transaction

    // Step 2: Create a NEW payment record in the database
    Payment createdPayment = paymentRepository.save(payment);

    // Step 3: Publish an event notification to the Pub-Sub
    eventPublisher.publishEvent(new PaymentCreatedEvent(createdPayment.getId()));

    return createdPayment;
    // Step 4: Commit the transaction to the DB
    // (This will be done automatically by Spring upon method completion)
  }

  @Transactional
  public void updatePaymentStatus(UUID paymentId, PaymentStatus status) {
    paymentRepository.updatePaymentStatus(paymentId, status);
  }

  @Transactional(readOnly = true)
  public Payment getPaymentById(UUID paymentId) {
    return paymentRepository.findById(paymentId)
        .orElseThrow(() -> new NoSuchElementException("Payment not found for ID: " + paymentId));
  }
}
