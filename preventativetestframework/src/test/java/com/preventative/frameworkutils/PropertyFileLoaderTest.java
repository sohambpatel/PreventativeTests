package com.preventative.frameworkutils;

import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;

class PropertyFileLoaderTest {
  /**
   * Method under test: {@link PropertyFileLoader#readPropertyValues(String)}
   */
  @Test
  void testReadPropertyValues() {
    // Arrange, Act and Assert
    assertThrows(RuntimeException.class, () -> PropertyFileLoader.readPropertyValues("/directory/foo.txt"));
  }
}
