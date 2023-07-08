package com.mbartecki.storesimulator.publisher;

import com.mbartecki.storesimulator.model.OutboxEvent;
import com.mbartecki.storesimulator.repository.OutboxEventRepository;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OutboxEventPublisher {
  private final OutboxEventRepository outboxEventRepository;
  private final KafkaTemplate<String, String> kafkaTemplate;

  public OutboxEventPublisher(OutboxEventRepository outboxEventRepository, KafkaTemplate<String, String> kafkaTemplate) {
    this.outboxEventRepository = outboxEventRepository;
    this.kafkaTemplate = kafkaTemplate;
  }

  @Scheduled(fixedDelay = 1000) // Scheduled to run every second
  public void publishPaymentCreatedEventsToKafka() {
    List<OutboxEvent> events = outboxEventRepository.findByEventName("payment-created-topic");
    for (OutboxEvent event : events) {
      publishEventToKafka(event);
      outboxEventRepository.delete(event); // Remove the event from the outbox after publishing
    }
  }

  @Scheduled(fixedDelay = 10000) // Scheduled to run every 10 seconds
  public void publishSendEmailEventsToKafka() {
    List<OutboxEvent> events = outboxEventRepository.findByEventName("send-email-topic");
    for (OutboxEvent event : events) {
      publishEventToKafka(event);
      outboxEventRepository.delete(event); // Remove the event from the outbox after publishing
    }
  }

  private void publishEventToKafka(OutboxEvent event) {
    String eventName = event.getEventName();
    String payload = event.getEventPayload();
    kafkaTemplate.send(eventName, payload);
  }
}
