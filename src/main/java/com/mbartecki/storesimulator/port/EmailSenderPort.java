package com.mbartecki.storesimulator.port;

import com.mbartecki.storesimulator.model.Payment;

public interface EmailSenderPort {


  void sendEmail(Payment payment);
}
