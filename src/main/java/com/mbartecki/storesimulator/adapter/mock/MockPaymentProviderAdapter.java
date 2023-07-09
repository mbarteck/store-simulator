package com.mbartecki.storesimulator.adapter.mock;

import com.mbartecki.storesimulator.model.Payment;
import com.mbartecki.storesimulator.model.PaymentStatus;
import com.mbartecki.storesimulator.port.PaymentProviderPort;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Component
public class MockPaymentProviderAdapter implements PaymentProviderPort {

  private final RestTemplate restTemplate;

  public MockPaymentProviderAdapter(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

  public PaymentStatus charge(Payment payment) {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    HttpEntity<Payment> requestEntity = new HttpEntity<>(payment, headers);

    try {
      ResponseEntity<Void> responseEntity = restTemplate.exchange(
          "http://example.com/api/payment",
          HttpMethod.POST,
          requestEntity,
          Void.class);

      var statusCode = responseEntity.getStatusCode();
      if (statusCode.is2xxSuccessful()) {
        return PaymentStatus.SUCCEEDED;
      } else {
        return PaymentStatus.FAILED;
      }
    } catch (RestClientException e) {
      return PaymentStatus.FAILED;
    }
  }
}
