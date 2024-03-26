package com.preventative.frameworkutils;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.io.IOException;
import java.util.ArrayList;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.remote.RemoteLogs;

class MetricsRecorderTest {
  /**
   * Method under test: {@link MetricsRecorder#captureJSLogsMetrics()}
   */
  @Test
  void testCaptureJSLogsMetrics() throws IOException {
    // Arrange
    RemoteLogs remoteLogs = mock(RemoteLogs.class);
    when(remoteLogs.get(Mockito.<String>any())).thenReturn(new LogEntries(new ArrayList<>()));
    WebDriver.Options options = mock(WebDriver.Options.class);
    when(options.logs()).thenReturn(remoteLogs);
    ChromeDriver driver = mock(ChromeDriver.class);
    when(driver.manage()).thenReturn(options);

    // Act
    (new MetricsRecorder(driver)).captureJSLogsMetrics();

    // Assert
    verify(options).logs();
    verify(remoteLogs).get(eq("browser"));
    verify(driver).manage();
  }
}
