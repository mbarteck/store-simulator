package com.mbartecki.storesimulator.utils;

import com.mbartecki.storesimulator.dto.CheckoutItem;

import java.math.BigDecimal;
import java.util.List;

public class CalculationsUtils {

  public static BigDecimal calculateFinalPrice(List<CheckoutItem> items) {
    return items == null ? BigDecimal.ZERO : items.stream()
        .map(item -> item.price().multiply(BigDecimal.valueOf(item.quantity())))
        .reduce(BigDecimal.ZERO, BigDecimal::add);
  }
}
