package com.mbartecki.storesimulator.adapter.mock;

import com.mbartecki.storesimulator.dto.EmailDto;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MockEmailSenderAdapterTest {

  @Test
  void sendEmail_ShouldPrintEmailDetails() {
    // Arrange
    MockEmailSenderAdapter emailSender = new MockEmailSenderAdapter();
    EmailDto emailDto = new EmailDto("example@example.com", "Test Subject", "Test Body");
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    PrintStream printStream = new PrintStream(outputStream);
    System.setOut(printStream);

    // Act
    emailSender.sendEmail(emailDto);
    String consoleOutput = outputStream.toString();

    // Assert
    String expectedOutput = "### MAIL SERVICE ###\n" +
        "Sending email to: example@example.com\n" +
        "Subject: Test Subject\n" +
        "Body: Test Body\n" +
        "Email sent successfully!\n\n";
    assertEquals(expectedOutput, consoleOutput);

    // Clean up
    System.setOut(System.out);
  }
}
