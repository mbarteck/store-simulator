package com.mbartecki.storesimulator.adapter;

import com.mbartecki.storesimulator.dto.CheckoutItem;
import com.mbartecki.storesimulator.dto.OrderRequest;
import com.mbartecki.storesimulator.model.Payment;
import com.mbartecki.storesimulator.port.CheckoutPort;
import com.mbartecki.storesimulator.port.EmailSenderPort;
import com.mbartecki.storesimulator.service.PaymentService;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class CheckoutAdapter implements CheckoutPort {

  private final EmailSenderPort emailSenderPort;

  private final PaymentService paymentService;

  public CheckoutAdapter(EmailSenderPort emailSenderPort, PaymentService paymentService) {
    this.emailSenderPort = emailSenderPort;
    this.paymentService = paymentService;
  }

  public void process(OrderRequest orderRequest) {
    BigDecimal sum = calculateFinalPrice(orderRequest.items());
    Payment shellPayment =
        Payment.builder().amount(sum).userEmail(orderRequest.userEmail()).build();
    Payment createdPayment = paymentService.createPayment(shellPayment);
    //TODO send email only if payment created
    emailSenderPort.sendEmail(createdPayment);


  }


  private BigDecimal calculateFinalPrice(List<CheckoutItem> items) {
    return items.stream()
        .map(item -> item.price().multiply(BigDecimal.valueOf(item.quantity())))
        .reduce(BigDecimal.ZERO, BigDecimal::add);
  }
}
