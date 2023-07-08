package com.mbartecki.storesimulator.dto;

import java.math.BigDecimal;

public record EmailDto(String to, String subject, String body) {
}
