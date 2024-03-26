package com.preventative.frameworkutils;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;

class JSONFilePathHandlerTest {
  /**
   * Method under test: {@link JSONFilePathHandler#jsonFileReader(String)}
   */
  @Test
  void testJsonFileReader() {
    // Arrange, Act and Assert
    assertThrows(RuntimeException.class, () -> (new JSONFilePathHandler()).jsonFileReader("/directory/foo.txt"));
  }

  /**
   * Method under test: {@link JSONFilePathHandler#readJsonPath(String, String)}
   */
  @Test
  void testReadJsonPath() {
    // Arrange, Act and Assert
    assertNull((new JSONFilePathHandler()).readJsonPath("Not all who wander are lost", "Jsonpath"));
  }

  /**
   * Method under test:
   * {@link JSONFilePathHandler#readJsonPathUsingDocument(String, String)}
   */
  @Test
  void testReadJsonPathUsingDocument() {
    // Arrange, Act and Assert
    assertNull((new JSONFilePathHandler()).readJsonPathUsingDocument("Not all who wander are lost", "Jsonpath"));
  }

  /**
   * Method under test: default or parameterless constructor of
   * {@link JSONFilePathHandler}
   */
  @Test
  void testNewJSONFilePathHandler() {
    // Arrange and Act
    JSONFilePathHandler actualJsonFilePathHandler = new JSONFilePathHandler();

    // Assert
    assertNull(actualJsonFilePathHandler.mapper);
    assertNull(actualJsonFilePathHandler.inputStream);
  }
}
