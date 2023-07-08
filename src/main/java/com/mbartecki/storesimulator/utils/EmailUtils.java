package com.mbartecki.storesimulator.utils;

import com.mbartecki.storesimulator.dto.EmailDto;
import com.mbartecki.storesimulator.model.Payment;
import com.mbartecki.storesimulator.model.PaymentStatus;

import java.util.UUID;

public class EmailUtils {

  public static EmailDto createEmail(Payment payment) {
    UUID paymentId = payment.getId();
    String recipientEmail = payment.getUserEmail();
    PaymentStatus status = payment.getStatus();
    String subject = "Payment " + status;
    String message = "Payment with ID " + paymentId + " " + status;
    return new EmailDto(recipientEmail, subject, message);
  }
}
