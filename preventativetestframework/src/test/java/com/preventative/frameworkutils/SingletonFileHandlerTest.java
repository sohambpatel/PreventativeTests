package com.preventative.frameworkutils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import java.nio.file.Paths;
import org.junit.jupiter.api.Test;

class SingletonFileHandlerTest {
  /**
   * Method under test: {@link SingletonFileHandler#readFile(String)}
   */
  @Test
  void testReadFile() {
    // Arrange
    SingletonFileHandler instance = SingletonFileHandler.getInstance();

    // Act and Assert
    assertNull(instance.readFile("foo.txt"));
    String expectedAbsolutePath = Paths.get(System.getProperty("user.dir"), "foo.txt").toString();
    assertEquals(expectedAbsolutePath,
        instance.f1.getAbsoluteFile().getAbsoluteFile().getAbsoluteFile().getAbsolutePath());
  }
}
