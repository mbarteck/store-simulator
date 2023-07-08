package com.mbartecki.storesimulator.model;

public enum PaymentStatus {
  NEW("has been created"),
  SUCCEEDED("succeeded"),
  FAILED("failed");
  private final String description;

  PaymentStatus(String description) {
    this.description = description;
  }

  @Override
  public String toString() {
    return description;
  }
}
