package com.mbartecki.storesimulator.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record OrderRequest(@Email(message = "Invalid email address") String userEmail,
                           @NotEmpty(message = "List of items cannot be empty") @Valid List<CheckoutItem> items) {
}
