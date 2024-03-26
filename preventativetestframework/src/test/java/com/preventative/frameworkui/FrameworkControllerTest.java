package com.preventative.frameworkui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

class FrameworkControllerTest {
  /**
   * Method under test: {@link FrameworkController#replaceFileSeparator(String)}
   */
  @Test
  void testReplaceFileSeparator() {
    // Arrange, Act and Assert
    assertEquals("/directory/foo.txt", FrameworkController.replaceFileSeparator("/directory/foo.txt"));
    assertEquals("/", FrameworkController.replaceFileSeparator("\\"));
  }
}
