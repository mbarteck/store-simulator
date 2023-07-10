package com.mbartecki.storesimulator.dto;

import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record CheckoutItem(Long productId,
                           @Positive(message = "The price must be a positive value") BigDecimal price,
                           @Positive(message = "The quantity must be a positive value") Long quantity) {
}
