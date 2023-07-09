package com.mbartecki.storesimulator.utils;

import com.mbartecki.storesimulator.dto.EmailDto;
import com.mbartecki.storesimulator.model.Payment;
import com.mbartecki.storesimulator.model.PaymentStatus;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class EmailUtilsTest {

  @Test
  void createEmail_ValidPayment_ShouldCreateEmailDto() {
    // Arrange
    UUID paymentId = UUID.randomUUID();
    String userEmail = "example@example.com";
    PaymentStatus status = PaymentStatus.SUCCEEDED;

    Payment payment = Payment.builder().id(paymentId).userEmail(userEmail).status(status).build();

    // Act
    EmailDto emailDto = EmailUtils.createEmail(payment);

    // Assert
    assertEquals(userEmail, emailDto.to());
    assertEquals("Payment succeeded", emailDto.subject());
    assertEquals("Payment with ID " + paymentId + " succeeded", emailDto.body());
  }

  @Test
  void createEmail_NullPayment_ShouldThrowNullPointerException() {
    // Arrange
    Payment payment = null;

    // Act & Assert
    assertThrows(NullPointerException.class, () -> EmailUtils.createEmail(payment));
  }
}
