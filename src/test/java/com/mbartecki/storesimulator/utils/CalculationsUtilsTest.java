package com.mbartecki.storesimulator.utils;

import com.mbartecki.storesimulator.dto.CheckoutItem;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

public class CalculationsUtilsTest {

  @Test
  public void testCalculateFinalPrice_ValidItemList_ReturnsTotalPrice() {
    // Arrange
    List<CheckoutItem> items = Arrays.asList(
        new CheckoutItem(1L, BigDecimal.valueOf(10.99), 2L),
        new CheckoutItem(2L, BigDecimal.valueOf(5.99), 1L)
    );
    BigDecimal expectedTotalPrice = BigDecimal.valueOf(27.97);

    // Act
    BigDecimal totalPrice = CalculationsUtils.calculateFinalPrice(items);

    // Assert
    Assertions.assertEquals(expectedTotalPrice, totalPrice);
  }

  @Test
  public void testCalculateFinalPrice_EmptyItemList_ReturnsZero() {
    // Arrange
    List<CheckoutItem> items = List.of();
    BigDecimal expectedTotalPrice = BigDecimal.ZERO;

    // Act
    BigDecimal totalPrice = CalculationsUtils.calculateFinalPrice(items);

    // Assert
    Assertions.assertEquals(expectedTotalPrice, totalPrice);
  }

  @Test
  public void testCalculateFinalPrice_NullItemList_ReturnsZero() {
    // Arrange
    List<CheckoutItem> items = null;
    BigDecimal expectedTotalPrice = BigDecimal.ZERO;

    // Act
    BigDecimal totalPrice = CalculationsUtils.calculateFinalPrice(items);

    // Assert
    Assertions.assertEquals(expectedTotalPrice, totalPrice);
  }
}
