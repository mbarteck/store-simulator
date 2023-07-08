package com.mbartecki.storesimulator.dto;

import java.util.List;

public record OrderRequest(String userEmail, List<CheckoutItem> items) {
}
