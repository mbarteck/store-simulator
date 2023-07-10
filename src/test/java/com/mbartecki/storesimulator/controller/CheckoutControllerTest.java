package com.mbartecki.storesimulator.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.mbartecki.storesimulator.dto.CheckoutItem;
import com.mbartecki.storesimulator.dto.OrderRequest;
import com.mbartecki.storesimulator.port.CheckoutPort;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CheckoutController.class)
class CheckoutControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private CheckoutPort checkoutPort;

  @Test
  void testReceiveCart_InvalidEmailFormat_ValidationFailure() throws Exception {
    // Arrange
    CheckoutItem item = new CheckoutItem(1L, new BigDecimal(1), 1L);
    OrderRequest cartRequest = new OrderRequest("invalid_email", Collections.singletonList(item));

    // Act & Assert
    mockMvc.perform(post("/checkout")
            .contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(cartRequest)))
        .andExpect(status().isBadRequest())
        .andExpect(content().string("[Invalid email address]"));

    verify(checkoutPort, never()).process(any());
  }

  @Test
  void testReceiveCart_EmptyItemList_ValidationFailure() throws Exception {
    // Arrange
    OrderRequest cartRequest = new OrderRequest("valid@example.com", Collections.emptyList());

    // Act & Assert
    mockMvc.perform(post("/checkout")
            .contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(cartRequest)))
        .andExpect(status().isBadRequest())
        .andExpect(content().string("[List of items cannot be empty]"));

    verify(checkoutPort, never()).process(any());
  }

  @Test
  void testReceiveCart_NegativePrice_ValidationFailure() throws Exception {
    // Arrange
    CheckoutItem item = new CheckoutItem(1L, new BigDecimal(-10), 1L);
    OrderRequest cartRequest =
        new OrderRequest("valid@example.com", Collections.singletonList(item));

    // Act & Assert
    mockMvc.perform(post("/checkout")
            .contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(cartRequest)))
        .andExpect(status().isBadRequest())
        .andExpect(content().string("[The price must be a positive value]"));

    verify(checkoutPort, never()).process(any());
  }

  @Test
  void testReceiveCart_NegativeQuantity_ValidationFailure() throws Exception {
    // Arrange
    CheckoutItem item = new CheckoutItem(1L, BigDecimal.valueOf(10), -1L);
    OrderRequest cartRequest =
        new OrderRequest("valid@example.com", Collections.singletonList(item));

    // Act & Assert
    mockMvc.perform(post("/checkout")
            .contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(cartRequest)))
        .andExpect(status().isBadRequest())
        .andExpect(content().string("[The quantity must be a positive value]"));

    verify(checkoutPort, never()).process(any());
  }

  @Test
  void testReceiveCart_ValidRequest_SuccessfulProcessing() throws Exception {
    // Arrange
    CheckoutItem item = new CheckoutItem(1L, BigDecimal.valueOf(10), 1L);
    OrderRequest cartRequest =
        new OrderRequest("valid@example.com", Collections.singletonList(item));

    // Act & Assert
    mockMvc.perform(post("/checkout")
            .contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(cartRequest)))
        .andExpect(status().isOk())
        .andExpect(content().string("Successfully processed the order"));

    verify(checkoutPort, times(1)).process(cartRequest);
  }

}
