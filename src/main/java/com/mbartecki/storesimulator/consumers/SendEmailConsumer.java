package com.mbartecki.storesimulator.consumers;

import com.mbartecki.storesimulator.dto.EmailDto;
import com.mbartecki.storesimulator.port.EmailSenderPort;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

import static com.mbartecki.storesimulator.utils.JsonUtils.deserialize;

@Component
public class SendEmailConsumer {

  private final EmailSenderPort emailSenderPort;

  public SendEmailConsumer(EmailSenderPort emailSenderPort) {
    this.emailSenderPort = emailSenderPort;
  }

  @KafkaListener(topics = "send-email-topic")
  @Retryable(maxAttempts = 3, backoff = @Backoff(delay = 1000, multiplier = 2.0))
  public void receiveEmailDto(String jsonString) {
    EmailDto emailDto = deserialize(jsonString, EmailDto.class);
    emailSenderPort.sendEmail(emailDto);
  }
}
