package com.mbartecki.storesimulator.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;

import java.util.List;

public record OrderRequest(@Email(message = "Invalid email address") String userEmail,
                           @Valid List<CheckoutItem> items) {
}
