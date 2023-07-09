package com.mbartecki.storesimulator.consumer;

import com.mbartecki.storesimulator.consumers.SendEmailConsumer;
import com.mbartecki.storesimulator.dto.EmailDto;
import com.mbartecki.storesimulator.port.EmailSenderPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

class SendEmailConsumerTest {

  @Mock
  private EmailSenderPort emailSenderPort;

  private SendEmailConsumer sendEmailConsumer;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    sendEmailConsumer = new SendEmailConsumer(emailSenderPort);
  }

  @Test
  void receiveEmailDto_ShouldSendEmail_WhenEmailDtoIsReceived() {
    // Arrange
    String jsonString =
        "{\"to\":\"example@example.com\",\"subject\":\"Test Subject\",\"body\":\"Test Body\"}";
    EmailDto emailDto = new EmailDto("example@example.com", "Test Subject", "Test Body");

    // Act
    sendEmailConsumer.receiveEmailDto(jsonString);

    // Assert
    verify(emailSenderPort, times(1)).sendEmail(emailDto);
  }
}

