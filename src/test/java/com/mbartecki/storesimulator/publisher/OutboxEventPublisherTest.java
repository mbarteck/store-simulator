package com.mbartecki.storesimulator.publisher;

import com.mbartecki.storesimulator.model.OutboxEvent;
import com.mbartecki.storesimulator.repository.OutboxEventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;

class OutboxEventPublisherTest {

  @Mock
  private OutboxEventRepository outboxEventRepository;

  @Mock
  private KafkaTemplate<String, String> kafkaTemplate;

  private OutboxEventPublisher outboxEventPublisher;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    outboxEventPublisher = new OutboxEventPublisher(outboxEventRepository, kafkaTemplate);
  }

  @Test
  void testPublishPaymentCreatedEventsToKafka_withEvents() {
    List<OutboxEvent> events = new ArrayList<>();
    events.add(new OutboxEvent(UUID.randomUUID(), "payment-created-topic", "payload1"));
    events.add(new OutboxEvent(UUID.randomUUID(), "payment-created-topic", "payload2"));

    when(outboxEventRepository.findByEventName("payment-created-topic")).thenReturn(events);

    outboxEventPublisher.publishPaymentCreatedEventsToKafka();

    verify(kafkaTemplate, times(2)).send(eq("payment-created-topic"), anyString(), anyString());
    verify(outboxEventRepository, times(2)).delete(any(OutboxEvent.class));
  }

  @Test
  void testPublishPaymentCreatedEventsToKafka_withoutEvents() {
    when(outboxEventRepository.findByEventName("payment-created-topic")).thenReturn(
        new ArrayList<>());

    outboxEventPublisher.publishPaymentCreatedEventsToKafka();

    verify(kafkaTemplate, never()).send(anyString(), anyString());
    verify(outboxEventRepository, never()).delete(any(OutboxEvent.class));
  }

  @Test
  void testPublishSendEmailEventsToKafka_withEvents() {
    List<OutboxEvent> events = new ArrayList<>();
    events.add(new OutboxEvent(UUID.randomUUID(), "send-email-topic", "payload1"));
    events.add(new OutboxEvent(UUID.randomUUID(), "send-email-topic", "payload2"));

    when(outboxEventRepository.findByEventName("send-email-topic")).thenReturn(events);

    outboxEventPublisher.publishSendEmailEventsToKafka();

    verify(kafkaTemplate, times(2)).send(eq("send-email-topic"), anyString(), anyString());
    verify(outboxEventRepository, times(2)).delete(any(OutboxEvent.class));
  }

  @Test
  void testPublishSendEmailEventsToKafka_withoutEvents() {
    when(outboxEventRepository.findByEventName("send-email-topic")).thenReturn(new ArrayList<>());

    outboxEventPublisher.publishPaymentCreatedEventsToKafka();

    verify(kafkaTemplate, never()).send(anyString(), anyString());
    verify(outboxEventRepository, never()).delete(any(OutboxEvent.class));
  }
}
