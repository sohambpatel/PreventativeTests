package com.preventative.frameworkutils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

class GenAIHandlerTest {
  /**
   * Method under test: {@link GenAIHandler#GenAIHandler(String, String)}
   */
  @Test
  void testNewGenAIHandler() {
    // Arrange and Act
    GenAIHandler actualGenAIHandler = new GenAIHandler("https://example.org/example", "Chatgptapikey");

    // Assert
    assertEquals("Chatgptapikey", actualGenAIHandler.API_KEY);
    assertEquals("https://example.org/example", actualGenAIHandler.CHATGPTURL);
  }
}
