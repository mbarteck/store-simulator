package com.mbartecki.storesimulator.utils;

import lombok.extern.slf4j.Slf4j;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mbartecki.storesimulator.dto.EmailDto;
import com.mbartecki.storesimulator.model.Payment;
import com.mbartecki.storesimulator.model.PaymentStatus;

import java.util.UUID;

@Slf4j
public class JsonUtils {

  private static final ObjectMapper objectMapper = new ObjectMapper();

  public static String serialize(Object object) {
    try {
      return objectMapper.writeValueAsString(object);
    } catch (JsonProcessingException e) {
      String errorMessage = "Error occurred while serializing object to JSON: " + e.getMessage();
      log.warn(errorMessage);
      throw new IllegalArgumentException(e);
    }
  }

  public static <T> T deserialize(String json, Class<T> clazz) {
    try {
      return objectMapper.readValue(json, clazz);
    } catch (JsonProcessingException e) {
      String errorMessage = "Error occurred while deserializing JSON to object: " + e.getMessage();
      log.warn(errorMessage);
      throw new IllegalArgumentException(json, e);
    }
  }
}
