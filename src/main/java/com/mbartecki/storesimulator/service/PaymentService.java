package com.mbartecki.storesimulator.service;

import com.mbartecki.storesimulator.model.Payment;
import com.mbartecki.storesimulator.model.PaymentStatus;
import com.mbartecki.storesimulator.repository.PaymentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
public class PaymentService {

  private final PaymentRepository paymentRepository;


  public PaymentService(PaymentRepository paymentRepository) {
    this.paymentRepository = paymentRepository;
  }

  @Transactional
  public Payment createPayment(Payment payment) {
    return paymentRepository.save(payment);
  }

  @Transactional
  public void updatePaymentStatus(UUID paymentId, PaymentStatus status) {
    paymentRepository.updatePaymentStatus(paymentId, status);
  }

  @Transactional(readOnly = true)
  public Optional<Payment> getPaymentById(UUID paymentId) {
    return paymentRepository.findById(paymentId);
  }
}
