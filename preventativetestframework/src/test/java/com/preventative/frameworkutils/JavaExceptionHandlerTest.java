package com.preventative.frameworkutils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.ArrayList;
import org.junit.jupiter.api.Test;

class JavaExceptionHandlerTest {
  /**
   * Method under test: default or parameterless constructor of
   * {@link JavaExceptionHandler}
   */
  @Test
  void testNewJavaExceptionHandler() {
    // Arrange and Act
    JavaExceptionHandler actualJavaExceptionHandler = new JavaExceptionHandler();

    // Assert
    assertNull(actualJavaExceptionHandler.classname);
    assertNull(actualJavaExceptionHandler.methodname);
  }

  /**
   * Method under test:
   * {@link JavaExceptionHandler#parseJavaExceptionReturnClassName(String, String)}
   */
  @Test
  void testParseJavaExceptionReturnClassName() {
    // Arrange
    JavaExceptionHandler javaExceptionHandler = new JavaExceptionHandler();

    // Act
    ArrayList<String> actualParseJavaExceptionReturnClassNameResult = javaExceptionHandler
        .parseJavaExceptionReturnClassName("Exceptionstacktrace", "Classormethod");

    // Assert
    assertTrue(actualParseJavaExceptionReturnClassNameResult.isEmpty());
    assertTrue(javaExceptionHandler.classname.isEmpty());
    assertSame(actualParseJavaExceptionReturnClassNameResult, javaExceptionHandler.methodname);
    assertSame(javaExceptionHandler.methodname, actualParseJavaExceptionReturnClassNameResult);
  }

  /**
   * Method under test:
   * {@link JavaExceptionHandler#parseJavaExceptionReturnClassName(String, String)}
   */
  @Test
  void testParseJavaExceptionReturnClassName2() {
    // Arrange
    JavaExceptionHandler javaExceptionHandler = new JavaExceptionHandler();

    // Act
    ArrayList<String> actualParseJavaExceptionReturnClassNameResult = javaExceptionHandler
        .parseJavaExceptionReturnClassName("foo", "class");

    // Assert
    assertTrue(actualParseJavaExceptionReturnClassNameResult.isEmpty());
    assertTrue(javaExceptionHandler.methodname.isEmpty());
    assertSame(actualParseJavaExceptionReturnClassNameResult, javaExceptionHandler.classname);
    assertSame(javaExceptionHandler.classname, actualParseJavaExceptionReturnClassNameResult);
  }

  /**
   * Method under test:
   * {@link JavaExceptionHandler#parseJavaExceptionReturnClassName(String, String)}
   */
  @Test
  void testParseJavaExceptionReturnClassName3() {
    // Arrange
    JavaExceptionHandler javaExceptionHandler = new JavaExceptionHandler();

    // Act
    ArrayList<String> actualParseJavaExceptionReturnClassNameResult = javaExceptionHandler
        .parseJavaExceptionReturnClassName(" at U.U(U:9)", "Classormethod");

    // Assert
    assertEquals(1, actualParseJavaExceptionReturnClassNameResult.size());
    assertEquals("U", actualParseJavaExceptionReturnClassNameResult.get(0));
    ArrayList<String> stringList = javaExceptionHandler.classname;
    assertEquals(1, stringList.size());
    assertEquals("U", stringList.get(0));
    assertSame(actualParseJavaExceptionReturnClassNameResult, javaExceptionHandler.methodname);
    assertSame(javaExceptionHandler.methodname, actualParseJavaExceptionReturnClassNameResult);
  }
}
