package com.mbartecki.storesimulator.repository;

import com.mbartecki.storesimulator.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PaymentRepository extends JpaRepository<Payment, UUID> {

}
