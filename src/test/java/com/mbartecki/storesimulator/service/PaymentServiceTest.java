package com.mbartecki.storesimulator.service;

import com.mbartecki.storesimulator.model.OutboxEvent;
import com.mbartecki.storesimulator.model.Payment;
import com.mbartecki.storesimulator.model.PaymentStatus;
import com.mbartecki.storesimulator.repository.OutboxEventRepository;
import com.mbartecki.storesimulator.repository.PaymentRepository;
import com.mbartecki.storesimulator.utils.EmailUtils;
import com.mbartecki.storesimulator.utils.JsonUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class PaymentServiceTest {

  @Mock
  private PaymentRepository paymentRepository;

  @Mock
  private OutboxEventRepository outboxEventRepository;

  @InjectMocks
  private PaymentService paymentService;

  @Captor
  private ArgumentCaptor<OutboxEvent> outboxEventCaptor;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void save_NewPayment_ShouldSavePaymentAndOutboxEvents() {
    // Arrange
    UUID paymentId = UUID.randomUUID();
    Payment payment = Payment.builder()
        .id(paymentId)
        .status(PaymentStatus.NEW)
        .build();
    String expectedPaymentId = paymentId.toString();
    String expectedEmailPayload = JsonUtils.serialize(EmailUtils.createEmail(payment));

    // Act
    when(paymentRepository.save(payment)).thenReturn(payment);
    Payment savedPayment = paymentService.save(payment);

    // Assert
    assertEquals(payment, savedPayment);
    verify(paymentRepository, times(1)).save(payment);
    verify(outboxEventRepository, times(2)).save(outboxEventCaptor.capture());

    List<OutboxEvent> capturedEvents = outboxEventCaptor.getAllValues();
    assertEquals(2, capturedEvents.size());

    OutboxEvent paymentCreatedEvent = capturedEvents.get(0);
    assertEquals("payment-created-topic", paymentCreatedEvent.getEventName());
    assertEquals(expectedPaymentId, paymentCreatedEvent.getEventPayload());

    OutboxEvent sendEmailEvent = capturedEvents.get(1);
    assertEquals("send-email-topic", sendEmailEvent.getEventName());
    assertEquals(expectedEmailPayload, sendEmailEvent.getEventPayload());
  }

  @Test
  void getById_ExistingPaymentId_ShouldReturnPayment() {
    // Arrange
    UUID paymentId = UUID.randomUUID();
    Payment payment = Payment.builder()
        .id(paymentId)
        .status(PaymentStatus.NEW)
        .build();

    // Act
    when(paymentRepository.findById(paymentId)).thenReturn(Optional.of(payment));
    Optional<Payment> result = paymentService.getById(paymentId);

    // Assert
    assertTrue(result.isPresent());
    assertEquals(payment, result.get());
    verify(paymentRepository, times(1)).findById(paymentId);
  }

  @Test
  void getById_NonExistingPaymentId_ShouldReturnEmptyOptional() {
    // Arrange
    UUID paymentId = UUID.randomUUID();

    // Act
    when(paymentRepository.findById(paymentId)).thenReturn(Optional.empty());
    Optional<Payment> result = paymentService.getById(paymentId);

    // Assert
    assertTrue(result.isEmpty());
    verify(paymentRepository, times(1)).findById(paymentId);
  }
}

