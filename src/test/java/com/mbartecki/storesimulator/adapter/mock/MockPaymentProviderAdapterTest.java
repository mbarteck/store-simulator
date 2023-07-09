package com.mbartecki.storesimulator.adapter.mock;

import com.mbartecki.storesimulator.model.Payment;
import com.mbartecki.storesimulator.model.PaymentStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class MockPaymentProviderAdapterTest {

  @Mock
  private RestTemplate restTemplate;

  private MockPaymentProviderAdapter paymentProviderAdapter;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    paymentProviderAdapter = new MockPaymentProviderAdapter(restTemplate);
  }

  @Test
  void charge_ShouldReturnSucceeded_WhenHttpStatusIs2xx() {
    // Arrange
    Payment payment = new Payment();
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    HttpEntity<Payment> requestEntity = new HttpEntity<>(payment, headers);
    ResponseEntity<Void> responseEntity = new ResponseEntity<>(HttpStatus.OK);

    when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), eq(requestEntity), eq(Void.class)))
        .thenReturn(responseEntity);

    // Act
    PaymentStatus result = paymentProviderAdapter.charge(payment);

    // Assert
    assertEquals(PaymentStatus.SUCCEEDED, result);
    verify(restTemplate, times(1)).exchange(
        anyString(), eq(HttpMethod.POST), eq(requestEntity), eq(Void.class));
  }

  @Test
  void charge_ShouldReturnFailed_WhenHttpStatusIsNot2xx() {
    // Arrange
    Payment payment = new Payment();
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    HttpEntity<Payment> requestEntity = new HttpEntity<>(payment, headers);
    ResponseEntity<Void> responseEntity = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

    when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), eq(requestEntity), eq(Void.class)))
        .thenReturn(responseEntity);

    // Act
    PaymentStatus result = paymentProviderAdapter.charge(payment);

    // Assert
    assertEquals(PaymentStatus.FAILED, result);
    verify(restTemplate, times(1)).exchange(
        anyString(), eq(HttpMethod.POST), eq(requestEntity), eq(Void.class));
  }

  @Test
  void charge_ShouldReturnFailed_WhenRestClientExceptionIsThrown() {
    // Arrange
    Payment payment = new Payment();

    when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), any(HttpEntity.class),
        eq(Void.class)))
        .thenThrow(new RestClientException("RestClientException"));

    // Act
    PaymentStatus result = paymentProviderAdapter.charge(payment);

    // Assert
    assertEquals(PaymentStatus.FAILED, result);
    verify(restTemplate, times(1)).exchange(
        anyString(), eq(HttpMethod.POST), any(HttpEntity.class), eq(Void.class));
  }
}
