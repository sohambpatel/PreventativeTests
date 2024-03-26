package com.preventative.frameworkui;

import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.Test;

class TestFinalizerControllerTest {
  /**
   * Methods under test:
   * 
   * <ul>
   *   <li>default or parameterless constructor of {@link TestFinalizerController}
   *   <li>{@link TestFinalizerController#getMessage()}
   * </ul>
   */
  @Test
  void testGettersAndSetters() {
    // Arrange, Act and Assert
    assertNull((new TestFinalizerController()).getMessage());
  }
}
