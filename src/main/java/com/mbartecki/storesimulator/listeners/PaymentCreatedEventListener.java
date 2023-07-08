package com.mbartecki.storesimulator.listeners;

import com.mbartecki.storesimulator.events.PaymentCreatedEvent;
import com.mbartecki.storesimulator.events.PaymentFinishedEvent;
import com.mbartecki.storesimulator.model.Payment;
import com.mbartecki.storesimulator.model.PaymentStatus;
import com.mbartecki.storesimulator.port.PaymentProviderPort;
import com.mbartecki.storesimulator.service.PaymentService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class PaymentCreatedEventListener implements ApplicationListener<PaymentCreatedEvent> {

  private final PaymentService paymentService;
  private final PaymentProviderPort paymentProviderPort;
  private final ApplicationEventPublisher eventPublisher;


  public PaymentCreatedEventListener(PaymentService paymentService, PaymentProviderPort paymentProviderPort,
      ApplicationEventPublisher eventPublisher) {
    this.paymentService = paymentService;
    this.paymentProviderPort = paymentProviderPort;
    this.eventPublisher = eventPublisher;
  }

  @Override
  public void onApplicationEvent(PaymentCreatedEvent event) {
    var paymentId = event.getPaymentId();
    //TODO call the payment Provider only if payment created
    //OR has NEW status
    //OR expired
    Payment payment = paymentService.getPaymentById(paymentId);
    PaymentStatus status = paymentProviderPort.charge(payment);
    paymentService.updatePaymentStatus(payment.getId(), status);
    //TODO publish event only if update succeeded
    eventPublisher.publishEvent(new PaymentFinishedEvent(payment.getId(), status));
  }
}
