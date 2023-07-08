package com.mbartecki.storesimulator.adapter.mock;

import com.mbartecki.storesimulator.model.Payment;
import com.mbartecki.storesimulator.model.PaymentStatus;
import com.mbartecki.storesimulator.port.PaymentProviderPort;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Component
public class MockPaymentProviderAdapter implements PaymentProviderPort {

  public PaymentStatus charge(Payment payment) {
    HttpClient client = HttpClient.newBuilder().build();
    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create("http://example.com/api/payment"))
        .header("Content-Type", "application/json")
        .POST(HttpRequest.BodyPublishers.ofString(payment.toString()))
        .build();
    try {
      HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
      switch (response.statusCode()) {
        case 200:
        case 201:
          // Successful response (2xx status code range)
          return PaymentStatus.SUCCEEDED;
        default:
          return PaymentStatus.FAILED;
      }
    } catch (IOException | InterruptedException e) {
      return PaymentStatus.FAILED;
    }
  }
}
