package com.preventative.frameworkutils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.HasDevTools;
import org.openqa.selenium.devtools.v121.console.Console;
import org.openqa.selenium.devtools.v121.network.Network;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class MetricsRecorder {
    WebDriver driver;
    public MetricsRecorder(WebDriver driver){
        this.driver=driver;
    }
    //Create the listner and start capturing the console logs
    public void captureConsoleLogs(){
        DevTools devTools = ((HasDevTools) driver).getDevTools();
        devTools.createSession();
        devTools.send(Console.enable());
        devTools.addListener(
                Console.messageAdded(),
                event -> {
                    System.out.println(event.getText());
                    String consolelogfilepath;
                    if(System.getProperty("os.name").contains("Windows")){
                        consolelogfilepath = System.getProperty("user.home")+"\\Downloads\\consolelogs.log";
                    }else{
                        consolelogfilepath = System.getProperty("user.name")+"/Downloads/consolelogs.log";
                    }

                    try {
                        SingletonFileHandler.getInstance().writeToFile(consolelogfilepath, event.getText());
                    } catch (IOException e) {
                        System.out.println(consolelogfilepath+" doesnt exist, please check the file and path");
                        throw new RuntimeException(e);
                    }
                });
    }

    //Create the listner and start capturing the performance logs
    public void capturePerformanceMetrics() {
        DevTools devTools = ((HasDevTools) driver).getDevTools();
        devTools.createSession();
        devTools.send(Network.enable(Optional.empty(), Optional.empty(), Optional.empty()));
        String performancelogfilelocation;
        if (System.getProperty("os.name").contains("Windows")) {
            performancelogfilelocation = System.getProperty("user.home")+"\\Downloads\\performancelogs.log";
        } else {
            performancelogfilelocation = System.getProperty("user.name")+"/Downloads/performancelogs.log";
        }
        devTools.addListener(Network.requestWillBeSent(), entry -> {
            try {
                SingletonFileHandler.getInstance().writeToFile(performancelogfilelocation, "Request (id): " + entry.getRequestId()
                        + "URL is: " + entry.getRequest().getUrl()
                        + "and Request Type is: " + entry.getRequest().getMethod());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            System.out.println("Request (id): " + entry.getRequestId()
                    + "URL is: " + entry.getRequest().getUrl()
                    + "and Request Type is: " + entry.getRequest().getMethod());
        });

        devTools.addListener(Network.responseReceived(), entry -> {

            try {
                SingletonFileHandler.getInstance().writeToFile(performancelogfilelocation, "Response (Req id): " + entry.getRequestId()
                        + "URL is: " + entry.getResponse().getUrl()
                        + "Response body is: " + entry.getResponse().getStatusText()
                        + "Response time is: " + entry.getResponse().getResponseTime()
                        + "and Response code is:" + entry.getResponse().getStatus());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            System.out.println("Response (Req id): " + entry.getRequestId()
                    + "URL is: " + entry.getResponse().getUrl()
                    + "Response body is: " + entry.getResponse().getStatusText()
                    + "Response time is: " + entry.getResponse().getResponseTime()
                    + "and Response code is:" + entry.getResponse().getStatus());
        });
    }
    //This is custom logic to capture the js logs from selenium logs
    public void captureJSLogsMetrics() throws IOException {
        String jslogfilelocation;
        if (System.getProperty("os.name").contains("Windows")) {
            jslogfilelocation = System.getProperty("user.home")+"/Downloads/js.log";
        } else {
            jslogfilelocation = System.getProperty("user.name")+"/Downloads/js.log";
        }
        LogEntries logEntries = driver.manage().logs().get(LogType.BROWSER);
        List<LogEntry> lg = logEntries.getAll();
        for(LogEntry logEntry : lg) {
            System.out.println(logEntry);
            SingletonFileHandler.getInstance().writeToFile(jslogfilelocation, String.valueOf(logEntry));
        }
    }
}
