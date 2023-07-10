package com.mbartecki.storesimulator.consumers;

import lombok.extern.slf4j.Slf4j;

import com.mbartecki.storesimulator.dto.EmailDto;
import com.mbartecki.storesimulator.port.EmailSenderPort;
import org.springframework.kafka.annotation.DltHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.retrytopic.TopicSuffixingStrategy;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.kafka.support.serializer.DeserializationException;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

import static com.mbartecki.storesimulator.utils.JsonUtils.deserialize;

@Component
@Slf4j
public class SendEmailConsumer {

  private final EmailSenderPort emailSenderPort;

  public SendEmailConsumer(EmailSenderPort emailSenderPort) {
    this.emailSenderPort = emailSenderPort;
  }

  @RetryableTopic(
      attempts = "5",
      topicSuffixingStrategy = TopicSuffixingStrategy.SUFFIX_WITH_INDEX_VALUE,
      backoff = @Backoff(delay = 1000, multiplier = 2.0),
      exclude = { DeserializationException.class}
  )
  @KafkaListener(topics = "send-email-topic")
  public void receiveEmailDto(String jsonString) {
    EmailDto emailDto = deserialize(jsonString, EmailDto.class);
    emailSenderPort.sendEmail(emailDto);
  }
  @DltHandler
  public void handleDlt(String message, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
    log.info("Message: {} handled by dlq topic: {}", message, topic);
  }
}
