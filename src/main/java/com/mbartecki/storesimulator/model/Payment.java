package com.mbartecki.storesimulator.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@Builder
@Data
@Entity
@NoArgsConstructor
@Table(name = "payments")
public class Payment {

  @Id
  @Column(name = "id", nullable = false, updatable = false)
  @GeneratedValue(strategy = GenerationType.AUTO)
  private UUID id;
  @Column(name = "amount", precision = 10, scale = 2, nullable = false, updatable = false)
  private BigDecimal amount;
  @Enumerated(EnumType.STRING)
  @Column(name = "status")
  private PaymentStatus status = PaymentStatus.NEW;
  @CreationTimestamp
  @Column(name = "created_at", nullable = false, updatable = false)
  private LocalDateTime createdAt;
  @UpdateTimestamp
  @Column(name = "updated_at")
  private LocalDateTime updatedAt;
  @Column(name = "failed_reason")
  private String failedReason;
  @Column(name = "user_email", nullable = false, updatable = false)
  private String userEmail;
}
