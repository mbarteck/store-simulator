package com.mbartecki.storesimulator.consumer;

import com.mbartecki.storesimulator.consumers.PaymentCreatedConsumer;
import com.mbartecki.storesimulator.model.Payment;
import com.mbartecki.storesimulator.model.PaymentStatus;
import com.mbartecki.storesimulator.port.PaymentProviderPort;
import com.mbartecki.storesimulator.service.PaymentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class PaymentCreatedConsumerTest {

  @Mock
  private PaymentService paymentService;

  @Mock
  private PaymentProviderPort paymentProviderPort;

  private PaymentCreatedConsumer paymentCreatedConsumer;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    paymentCreatedConsumer = new PaymentCreatedConsumer(paymentService, paymentProviderPort);
  }

  @Test
  void receivePaymentId_ShouldProcessPayment_WhenPaymentExists() {
    // Arrange
    String paymentIdString = "a61b317f-1695-4be8-bedf-af476558057d";
    UUID paymentId = UUID.fromString(paymentIdString);
    Payment payment = new Payment();
    payment.setId(paymentId);

    when(paymentService.getById(paymentId)).thenReturn(Optional.of(payment));
    when(paymentProviderPort.charge(payment)).thenReturn(PaymentStatus.SUCCEEDED);

    // Act
    paymentCreatedConsumer.receivePaymentId(paymentIdString);

    // Assert
    verify(paymentService, times(1)).getById(paymentId);
    verify(paymentProviderPort, times(1)).charge(payment);
    verify(paymentService, times(1)).save(payment);
    assertEquals(PaymentStatus.SUCCEEDED, payment.getStatus());
    assertEquals(LocalDateTime.now().getDayOfYear(), payment.getUpdatedAt().getDayOfYear());
  }

  @Test
  void receivePaymentId_ShouldNotProcessPayment_WhenPaymentDoesNotExist() {
    // Arrange
    String paymentIdString = "a61b317f-1695-4be8-bedf-af476558057d";
    UUID paymentId = UUID.fromString(paymentIdString);

    when(paymentService.getById(paymentId)).thenReturn(Optional.empty());

    // Act
    paymentCreatedConsumer.receivePaymentId(paymentIdString);

    // Assert
    verify(paymentService, times(1)).getById(paymentId);
    verify(paymentService, times(0)).save(any());
    verifyNoInteractions(paymentProviderPort);
  }
}

