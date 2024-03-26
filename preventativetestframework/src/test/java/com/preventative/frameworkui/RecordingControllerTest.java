package com.preventative.frameworkui;

import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.Test;
import org.zaproxy.clientapi.core.ClientApi;

class RecordingControllerTest {
  /**
   * Methods under test:
   * 
   * <ul>
   *   <li>default or parameterless constructor of {@link RecordingController}
   *   <li>{@link RecordingController#getClientApi()}
   *   <li>{@link RecordingController#getMessage()}
   * </ul>
   */
  @Test
  void testGettersAndSetters() {
    // Arrange and Act
    RecordingController actualRecordingController = new RecordingController();
    ClientApi actualClientApi = actualRecordingController.getClientApi();

    // Assert
    assertNull(actualRecordingController.getMessage());
    assertNull(actualClientApi);
  }
}
