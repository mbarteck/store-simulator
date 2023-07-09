package com.mbartecki.storesimulator.adapter.mock;

import com.mbartecki.storesimulator.dto.EmailDto;
import com.mbartecki.storesimulator.port.EmailSenderPort;
import org.springframework.stereotype.Component;

@Component
public class MockEmailSenderAdapter implements EmailSenderPort {

  @Override
  public void sendEmail(EmailDto emailDto) {
    System.out.println("### MAIL SERVICE ###");
    System.out.println("Sending email to: " + emailDto.to());
    System.out.println("Subject: " + emailDto.subject());
    System.out.println("Body: " + emailDto.body());
    System.out.println("Email sent successfully!");
    System.out.println("");
  }
}
