package com.mbartecki.storesimulator.adapter.mock;

import com.mbartecki.storesimulator.model.Payment;
import com.mbartecki.storesimulator.model.PaymentStatus;
import com.mbartecki.storesimulator.port.EmailSenderPort;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class MockEmailSenderAdapter implements EmailSenderPort {

  @Override
  public void sendEmail(Payment payment) {
    UUID paymentId = payment.getId();
    String recipientEmail = payment.getUserEmail();
    PaymentStatus status = payment.getStatus();
    String subject = "Payment " + status;
    String message = "Payment with ID " + paymentId + " " + status;
    sendEmail(recipientEmail, subject, message);
  }

  private void sendEmail(String to, String subject, String body) {
    System.out.println("Sending email to: " + to);
    System.out.println("Subject: " + subject);
    System.out.println("Body: " + body);
    System.out.println("Email sent successfully!");
  }
}
