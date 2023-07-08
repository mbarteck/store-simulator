package com.mbartecki.storesimulator.adapter.mock;

import com.mbartecki.storesimulator.dto.EmailDto;
import com.mbartecki.storesimulator.model.Payment;
import com.mbartecki.storesimulator.model.PaymentStatus;
import com.mbartecki.storesimulator.port.EmailSenderPort;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class MockEmailSenderAdapter implements EmailSenderPort {

  @Override
  public void sendEmail(EmailDto emailDto) {
    System.out.println("Sending emailDto to: " + emailDto.to());
    System.out.println("Subject: " + emailDto.subject());
    System.out.println("Body: " + emailDto.body());
    System.out.println("Email sent successfully!");
  }
}
