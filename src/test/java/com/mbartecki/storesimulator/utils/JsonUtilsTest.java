package com.mbartecki.storesimulator.utils;

import com.mbartecki.storesimulator.dto.EmailDto;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class JsonUtilsTest {

  @Test
  void serialize_ValidObject_ShouldReturnJsonString() {
    // Arrange
    EmailDto emailDto = new EmailDto("example@example.com", "Subject", "Body");
    String expectedJson =
        "{\"to\":\"example@example.com\",\"subject\":\"Subject\",\"body\":\"Body\"}";

    // Act
    String json = JsonUtils.serialize(emailDto);

    // Assert
    assertEquals(expectedJson, json);
  }

  @Test
  void deserialize_ValidJson_ShouldReturnObject() {
    // Arrange
    String json = "{\"to\":\"example@example.com\",\"subject\":\"Subject\",\"body\":\"Body\"}";
    EmailDto expectedEmailDto = new EmailDto("example@example.com", "Subject", "Body");

    // Act
    EmailDto emailDto = JsonUtils.deserialize(json, EmailDto.class);

    // Assert
    assertEquals(expectedEmailDto, emailDto);
  }

  @Test
  void deserialize_InvalidJson_ShouldThrowIllegalArgumentException() {
    // Arrange
    String json = "{invalid}";

    // Act & Assert
    assertThrows(IllegalArgumentException.class, () -> JsonUtils.deserialize(json, EmailDto.class));
  }
}
