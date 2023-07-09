package com.mbartecki.storesimulator.adapter;

import com.mbartecki.storesimulator.dto.CheckoutItem;
import com.mbartecki.storesimulator.dto.OrderRequest;
import com.mbartecki.storesimulator.model.Payment;
import com.mbartecki.storesimulator.model.PaymentStatus;
import com.mbartecki.storesimulator.port.CheckoutPort;
import com.mbartecki.storesimulator.service.PaymentService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

public class CheckoutAdapterTest {

  @Mock
  private PaymentService paymentService;

  private CheckoutPort checkoutPort;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
    checkoutPort = new CheckoutAdapter(paymentService);
  }

  @Test
  public void testProcess_SavesPayment() {
    // Arrange
    String userEmail = "example@example.com";
    List<CheckoutItem> items = Arrays.asList(
        new CheckoutItem(1L, BigDecimal.valueOf(10.99), 2L),
        new CheckoutItem(2L, BigDecimal.valueOf(5.99), 1L)
    );
    OrderRequest orderRequest = new OrderRequest(userEmail, items);
    BigDecimal expectedAmount = BigDecimal.valueOf(27.97);

    // Act
    checkoutPort.process(orderRequest);

    // Assert
    ArgumentCaptor<Payment> paymentCaptor = ArgumentCaptor.forClass(Payment.class);
    Mockito.verify(paymentService).save(paymentCaptor.capture());
    Payment savedPayment = paymentCaptor.getValue();

    Assertions.assertEquals(expectedAmount, savedPayment.getAmount());
    Assertions.assertEquals(userEmail, savedPayment.getUserEmail());
    Assertions.assertEquals(PaymentStatus.NEW, savedPayment.getStatus());
  }
}

