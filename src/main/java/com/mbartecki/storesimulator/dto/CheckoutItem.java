package com.mbartecki.storesimulator.dto;

import java.math.BigDecimal;

public record CheckoutItem(Long productId, BigDecimal price, Long quantity) {
}
