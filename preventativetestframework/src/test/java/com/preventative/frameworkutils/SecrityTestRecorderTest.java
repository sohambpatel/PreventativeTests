package com.preventative.frameworkutils;

import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.Test;
import org.zaproxy.clientapi.core.ClientApi;
import org.zaproxy.clientapi.core.ClientApiException;

class SecrityTestRecorderTest {
  /**
   * Method under test: default or parameterless constructor of
   * {@link SecrityTestRecorder}
   */
  @Test
  void testNewSecrityTestRecorder() {
    // Arrange, Act and Assert
    assertNull((new SecrityTestRecorder()).recordingController);
  }

  /**
   * Method under test: {@link SecrityTestRecorder#tearDown(ClientApi)}
   */
  @Test
  void testTearDown() throws ClientApiException {
    // Arrange
    SecrityTestRecorder secrityTestRecorder = new SecrityTestRecorder();

    // Act
    secrityTestRecorder.tearDown(null);

    // Assert that nothing has changed
    assertNull(secrityTestRecorder.recordingController);
  }
}
