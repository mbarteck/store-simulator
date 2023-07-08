package com.mbartecki.storesimulator.port;

import com.mbartecki.storesimulator.model.Payment;
import com.mbartecki.storesimulator.model.PaymentStatus;

public interface PaymentProviderPort {

  PaymentStatus charge(Payment payment);
}
