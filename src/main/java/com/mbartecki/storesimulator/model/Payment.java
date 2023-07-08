package com.mbartecki.storesimulator.model;

import lombok.*;

import jakarta.persistence.*;

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
  @GeneratedValue(strategy = GenerationType.AUTO)
  private UUID id;

  @Column(name = "amount", precision = 10, scale = 2, updatable = false)
  private BigDecimal amount;

  @Enumerated(EnumType.STRING)
  @Column(name = "status")
  private PaymentStatus status = PaymentStatus.NEW;

  @Column(name = "created_at", updatable = false)
  private LocalDateTime createdAt = LocalDateTime.now();;

  @Column(name = "completed_at")
  private LocalDateTime completedAt;

  @Column(name = "failed_reason")
  private String failedReason;

  @Column(name = "user_email")
  private String userEmail;

}
