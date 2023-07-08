package com.mbartecki.storesimulator.repository;

import com.mbartecki.storesimulator.model.Payment;
import com.mbartecki.storesimulator.model.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface PaymentRepository extends JpaRepository<Payment, UUID> {

  @Modifying
  @Query("UPDATE Payment p SET p.status = :status WHERE p.id = :paymentId")
  void updatePaymentStatus(
      @Param("paymentId") UUID paymentId, @Param("status") PaymentStatus status);
}
