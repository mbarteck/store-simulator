package com.mbartecki.storesimulator.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.mbartecki.storesimulator.dto.OrderRequest;
import com.mbartecki.storesimulator.port.CheckoutPort;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = CheckoutController.class)
public class CheckoutControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private CheckoutPort checkoutPort;

  @Test
  public void testReceiveCart_SuccessfulProcessing() throws Exception {
    // Arrange
    OrderRequest cartRequest = new OrderRequest("example@example.com", Collections.emptyList());

    // Act & Assert
    mockMvc.perform(post("/checkout")
            .contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(cartRequest)))
        .andExpect(status().isOk())
        .andExpect(content().string("Successfully processed the order"));

    verify(checkoutPort, times(1)).process(cartRequest);
  }

  @Test
  public void testReceiveCart_FailedProcessing() throws Exception {
    // Arrange
    OrderRequest cartRequest = new OrderRequest("example@example.com", Collections.emptyList());
    doThrow(new RuntimeException("Failed to process the order"))
        .when(checkoutPort)
        .process(cartRequest);

    // Act & Assert
    mockMvc.perform(post("/checkout")
            .contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(cartRequest)))
        .andExpect(status().isInternalServerError())
        .andExpect(content().string("Failed to process the order."));

    verify(checkoutPort, times(1)).process(cartRequest);
  }
}
