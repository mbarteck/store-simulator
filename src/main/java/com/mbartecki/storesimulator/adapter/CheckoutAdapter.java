package com.mbartecki.storesimulator.adapter;

import com.mbartecki.storesimulator.dto.OrderRequest;
import com.mbartecki.storesimulator.model.Payment;
import com.mbartecki.storesimulator.model.PaymentStatus;
import com.mbartecki.storesimulator.port.CheckoutPort;
import com.mbartecki.storesimulator.service.PaymentService;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

import static com.mbartecki.storesimulator.utils.CalculationsUtils.calculateFinalPrice;

@Component
public class CheckoutAdapter implements CheckoutPort {

  private final PaymentService paymentService;

  public CheckoutAdapter(PaymentService paymentService) {
    this.paymentService = paymentService;
  }

  public void process(OrderRequest orderRequest) {
    BigDecimal sum = calculateFinalPrice(orderRequest.items());
    Payment payment =
        Payment
            .builder()
            .amount(sum)
            .userEmail(orderRequest.userEmail())
            .status(PaymentStatus.NEW)
            .build();
    paymentService.save(payment);
  }
}
