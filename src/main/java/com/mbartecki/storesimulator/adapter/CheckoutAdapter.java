package com.mbartecki.storesimulator.adapter;

import com.mbartecki.storesimulator.dto.CheckoutItem;
import com.mbartecki.storesimulator.dto.OrderRequest;
import com.mbartecki.storesimulator.model.Payment;
import com.mbartecki.storesimulator.model.PaymentStatus;
import com.mbartecki.storesimulator.port.CheckoutPort;
import com.mbartecki.storesimulator.port.EmailSenderPort;
import com.mbartecki.storesimulator.service.PaymentService;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class CheckoutAdapter implements CheckoutPort {

  private final PaymentService paymentService;
  private final KafkaTemplate<String, String> kafkaTemplate;
  private final EmailSenderPort emailSenderPort;

  public CheckoutAdapter(
      PaymentService paymentService, KafkaTemplate<String, String> kafkaTemplate,
      EmailSenderPort emailSenderPort) {
    this.paymentService = paymentService;
    this.kafkaTemplate = kafkaTemplate;
    this.emailSenderPort = emailSenderPort;
  }

  public void process(OrderRequest orderRequest) {
    BigDecimal sum = calculateFinalPrice(orderRequest.items());
    Payment shellPayment =
        Payment
            .builder()
            .amount(sum)
            .userEmail(orderRequest.userEmail())
            .status(PaymentStatus.NEW)
            .build();
    Payment createdPayment = paymentService.createPayment(shellPayment);
    kafkaTemplate.send("payment-created-topic", createdPayment.getId().toString());
    emailSenderPort.sendEmail(createdPayment);
  }


  private BigDecimal calculateFinalPrice(List<CheckoutItem> items) {
    return items.stream()
        .map(item -> item.price().multiply(BigDecimal.valueOf(item.quantity())))
        .reduce(BigDecimal.ZERO, BigDecimal::add);
  }
}
