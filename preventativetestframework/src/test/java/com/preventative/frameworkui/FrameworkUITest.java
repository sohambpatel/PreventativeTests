package com.preventative.frameworkui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import javafx.application.HostServices;
import org.junit.jupiter.api.Test;

class FrameworkUITest {
  /**
   * Method under test: default or parameterless constructor of
   * {@link FrameworkUI}
   */
  @Test
  void testNewFrameworkUI() {
    // Arrange and Act
    FrameworkUI actualFrameworkUI = new FrameworkUI();

    // Assert
    HostServices hostServices = actualFrameworkUI.getHostServices();
    assertEquals("", hostServices.getCodeBase());
    assertNull(actualFrameworkUI.getParameters());
    String expectedDocumentBase = String.join("", "file:/C:/Users/", System.getProperty("user.name"),
        "/Pictures/PreventativeTests/preventativetestframework/");
    assertEquals(expectedDocumentBase, hostServices.getDocumentBase());
  }
}
