package com.preventative.frameworkutils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.Test;

class PromptHandlerTest {
  /**
   * Method under test: {@link PromptHandler#promptReturner(String)}
   */
  @Test
  void testPromptReturner() {
    // Arrange, Act and Assert
    assertNull(PromptHandler.promptReturner("Promptname"));
    assertEquals(PromptHandler.CONSOLELOGSPROMPT, PromptHandler.promptReturner("CONSOLELOGSPROMPT"));
    assertEquals(PromptHandler.JSLOGSPROMPT, PromptHandler.promptReturner("JSLOGSPROMPT"));
    assertEquals(PromptHandler.PERFORMANCELOGSPROMPT, PromptHandler.promptReturner("PERFORMANCELOGSPROMPT"));
    assertEquals(PromptHandler.SECURITYLOGSPROMPT, PromptHandler.promptReturner("SECURITYLOGSPROMPT"));
  }
}
