package com.mbartecki.storesimulator.listeners;

import com.mbartecki.storesimulator.events.PaymentFinishedEvent;
import com.mbartecki.storesimulator.model.Payment;
import com.mbartecki.storesimulator.port.EmailSenderPort;
import com.mbartecki.storesimulator.service.PaymentService;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class PaymentFinishedEventListener implements ApplicationListener<PaymentFinishedEvent> {

  private final PaymentService paymentService;

  private final EmailSenderPort emailSenderPort;



  public PaymentFinishedEventListener(PaymentService paymentService,
      EmailSenderPort emailSenderPort) {
    this.paymentService = paymentService;
    this.emailSenderPort = emailSenderPort;
  }


  @Override
  public void onApplicationEvent(PaymentFinishedEvent event) {
    Payment finishedPayment = paymentService.getPaymentById(event.getPaymentId());
    emailSenderPort.sendEmail(finishedPayment);

  }
}
