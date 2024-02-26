package com.preventative.frameworkui;

import com.preventative.frameworkutils.*;
import io.github.bonigarcia.wdm.WebDriverManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.core.io.ClassPathResource;
import org.zaproxy.clientapi.core.ClientApi;
import org.zaproxy.clientapi.core.ClientApiException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class TestFinalizerController {

    @FXML
    private TextField OUTPUTPATH;

    @FXML
    private TextField TESTREPOPATH;

    @FXML
    private ImageView backButton;

    @FXML
    private Text message;

    @FXML
    private Button startrecordingButton;

    @FXML
    private Button getrecommendation;

    @FXML
    private Button stoprecording;
    MetricsRecorder recordMetrics;
    SecrityTestRecorder secrityTestRecorder;

    GenAIHandler genAIHandler;
    public static String consolelogscontent;
    public static String jslogscontent;
    public static String performancelogscontent;
    public static String securitylogscontent;

    public static String consolelogrecommendationfilepath;
    public static String jslogrecommendationfilepath;
    public static String performancelogrecommendationfilepath;
    public static String securitylogrecommendationfilepath;

    WebDriver driver;
    FileWriter fw1;
    PrintWriter pw1;
    File f1;
    Properties configProperties;
    static final String ZAP_PROXY_ADDRESS="";
    static final int ZAP_PROXY_PORT = 0;
    static final String ZAP_API_KEY="";

    public ClientApi clientApi;
    public Text getMessage() {
        return message;
    }

    @FXML
    void updatedURL(KeyEvent event) {
        getMessage().setVisible(false);
    }

    @FXML
    void updatePath(KeyEvent event){
        getMessage().setVisible(false);
    }

    private String getSyntheticAppMonitoringOutputFilePath() {
        return OUTPUTPATH.getText();
    }

    private String getTestRepoPATH() {
        return TESTREPOPATH.getText();
    }
    public void setClientApi(String zapproxyaddress,int zapport,String zapapikey){
        clientApi=new ClientApi(zapproxyaddress,zapport,zapapikey);
    }

    public ClientApi getClientApi(){
        return clientApi;
    }

    @FXML
    void frameworkScene(MouseEvent event) throws IOException {
        Parent root = FXMLLoader.load(new ClassPathResource("/fxml/Framework.fxml").getURL());
        OUTPUTPATH.getScene().setRoot(root);
    }

    @FXML
    void getClassMethodNames(ActionEvent event) throws ClientApiException {
        URL url;
        String urlString = getSyntheticAppMonitoringOutputFilePath();
        String propertyfilepath=getTestRepoPATH();
        configProperties=new Properties();
        configProperties=PropertyFileLoader.readPropertyValues(propertyfilepath);
        System.out.println(urlString);
        try {
            url = new URL(urlString);
        } catch (MalformedURLException e) {
            getMessage().setText("Please, enter a valid URL");
            getMessage().setVisible(true);
            return;
        }
        String path;
        String proxyServerURL=configProperties.getProperty("ZAP_PROXY_ADDRESS")+":"+Integer.parseInt(configProperties.getProperty("ZAP_PROXY_PORT"));
        Proxy proxy=new Proxy();
        proxy.setHttpProxy(proxyServerURL);
        proxy.setSslProxy(proxyServerURL);
        setClientApi(configProperties.getProperty("ZAP_PROXY_ADDRESS"), Integer.parseInt(configProperties.getProperty("ZAP_PROXY_PORT")),configProperties.getProperty("ZAP_API_KEY"));
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        if(System.getProperty("os.name").contains("Windows")){
            path = System.getProperty("user.dir")+"\\preventativetestframework\\src\\main\\resources\\TestCaseStudio.crx";
        }else{
            path = System.getProperty("user.dir")+"/preventativetestframework/src/main/resources/TestCaseStudio.crx";
        }
        options.addExtensions(new File(path));
        options.setAcceptInsecureCerts(true);
        options.setProxy(proxy);
        driver = new ChromeDriver(options);
        driver.manage().deleteAllCookies();
        driver.manage().window().maximize();
        driver.get(url.toString());
        recordMetrics=new MetricsRecorder(driver);
        recordMetrics.captureConsoleLogs();
        recordMetrics.capturePerformanceMetrics();
    }
    @FXML
    void getTestCases(ActionEvent event) throws IOException, ClientApiException {
        recordMetrics=new MetricsRecorder(driver);
        recordMetrics.captureJSLogsMetrics();
        secrityTestRecorder=new SecrityTestRecorder();
        secrityTestRecorder.tearDown(clientApi);
        driver.quit();
    }
}