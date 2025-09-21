package com.preventative.frameworkui;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.*;

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

public class RecordingController {

    @FXML
    private TextField URL;

    @FXML
    private TextField PATH;

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

    private String getURL() {
        return URL.getText();
    }

    private String getPATH() {
        return PATH.getText();
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
        URL.getScene().setRoot(root);
    }

    //This method is being used to run browser session and record 4 metrics, security, performance, js logs and console logs
    //Here we are using selenium 4 capabilities for performace, js and console logs and ZAP for security logs
    @FXML
    void start(ActionEvent event) throws ClientApiException {
        URL url;
        String urlString = getURL();
        try {
            String propertyfilepath = getPATH();
            configProperties = new Properties();
            configProperties = PropertyFileLoader.readPropertyValues(propertyfilepath);
        }catch(Exception e){
            Alert alert=new Alert(Alert.AlertType.ERROR);
            alert.setContentText("ERROR OCCURED, \nPlease check console logs");
            alert.show();
            e.printStackTrace();
            throw new RuntimeException("This is due to property file not found");
        }
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
        //options.addExtensions(new File(path));
        options.setAcceptInsecureCerts(true);
        options.setProxy(proxy);
        driver = new ChromeDriver();
        driver.manage().deleteAllCookies();
        driver.manage().window().maximize();
        driver.get(url.toString());
        recordMetrics=new MetricsRecorder(driver);
        recordMetrics.captureConsoleLogs();
        recordMetrics.capturePerformanceMetrics();
    }
    //This is tear down method which is used to stop the listners which has been started as a part of start session
    //This is also being used to kill the driver created during the start session
    @FXML
    void stop(ActionEvent event) throws IOException, ClientApiException {
        recordMetrics=new MetricsRecorder(driver);
        recordMetrics.captureJSLogsMetrics();
        secrityTestRecorder=new SecrityTestRecorder();
        secrityTestRecorder.tearDown(clientApi);
        driver.quit();
    }
    //This is being used to generate the recommendation based on the logs files generated
    @FXML
    void getRecommendation(ActionEvent event) throws Exception {
        if (System.getProperty("os.name").contains("Windows")) {
            consolelogscontent= SingletonFileHandler.getInstance().readFile(System.getProperty("user.home")+"/Downloads/consolelogs.log").replaceAll("\\s","").replaceAll("\"", "'");
            SingletonFileHandler.getInstance().reset();
            jslogscontent=SingletonFileHandler.getInstance().readFile(System.getProperty("user.home")+"/Downloads/js.log").replaceAll("\\s","").replaceAll("\"", "'");
            SingletonFileHandler.getInstance().reset();
            performancelogscontent=SingletonFileHandler.getInstance().readFile(System.getProperty("user.home")+"/Downloads/performancelogs.log").replaceAll("\\s","").replaceAll("\"", "'");
            SingletonFileHandler.getInstance().reset();
            securitylogscontent=SingletonFileHandler.getInstance().readFile(System.getProperty("user.home")+"/Downloads/security_zap_report.html").replaceAll("\\s","").replaceAll("\"", "'");
            SingletonFileHandler.getInstance().reset();
        } else {
            consolelogscontent=SingletonFileHandler.getInstance().readFile(System.getProperty("user.name")+"/Downloads/consolelogs.log").replaceAll("\\s","").replaceAll("\"", "'");
            SingletonFileHandler.getInstance().reset();
            jslogscontent = SingletonFileHandler.getInstance().readFile(System.getProperty("user.name")+"/Downloads/js.log").replaceAll("\\s","").replaceAll("\"", "'");
            SingletonFileHandler.getInstance().reset();
            performancelogscontent=SingletonFileHandler.getInstance().readFile(System.getProperty("user.name")+"/Downloads/performancelogs.log").replaceAll("\\s","").replaceAll("\"", "'");
            SingletonFileHandler.getInstance().reset();
            securitylogscontent=SingletonFileHandler.getInstance().readFile(System.getProperty("user.name")+"/Downloads/security_zap_report.html").replaceAll("\\s","").replaceAll("\"", "'");
            SingletonFileHandler.getInstance().reset();
        }

        if(System.getProperty("os.name").contains("Windows")){
            consolelogrecommendationfilepath = System.getProperty("user.home")+"/Downloads/consolelogs.recommendation";
            jslogrecommendationfilepath=System.getProperty("user.home")+"/Downloads/jslogs.recommendation";
            performancelogrecommendationfilepath=System.getProperty("user.home")+"/Downloads/performancelogs.recommendation";
            securitylogrecommendationfilepath=System.getProperty("user.home")+"/Downloads/securitylogs.recommendation";
        }else{
            consolelogrecommendationfilepath = System.getProperty("user.name")+"/Downloads/consolelogs.recommendation";
            jslogrecommendationfilepath=System.getProperty("user.name")+"/Downloads/jslogs.recommendation";
            performancelogrecommendationfilepath=System.getProperty("user.name")+"/Downloads/performancelogs.recommendation";
            securitylogrecommendationfilepath=System.getProperty("user.name")+"/Downloads/securitylogs.recommendation";
        }
        try {
            String propertyfilepath = getPATH();
            configProperties = new Properties();
            configProperties = PropertyFileLoader.readPropertyValues(propertyfilepath);
        }catch(Exception e){
            Alert alert=new Alert(Alert.AlertType.ERROR);
            alert.setContentText("ERROR OCCURED, \nPlease check console logs");
            alert.show();
            e.printStackTrace();
            throw new RuntimeException("This is due to property file not found");
        }
        genAIHandler=new GenAIHandler(configProperties.getProperty("CHATGPTURL"),configProperties.getProperty("CHATGPTAPI_KEY"));

        if(consolelogscontent!=null){
            System.out.println("Prompt used for this request is: "+PromptHandler.promptReturner("CONSOLELOGSPROMPT"));
            SingletonFileHandler.getInstance().writeToFile(consolelogrecommendationfilepath,genAIHandler.generateRecommendation(PromptHandler.promptReturner("CONSOLELOGSPROMPT"),consolelogscontent));
        }else{
            System.out.println("Cannot run the Console logs recommendation");
        }

        if (jslogscontent!=null){
            System.out.println("Prompt used for this request is: "+PromptHandler.JSLOGSPROMPT);
            SingletonFileHandler.getInstance().writeToFile(jslogrecommendationfilepath,genAIHandler.generateRecommendation(PromptHandler.JSLOGSPROMPT,jslogscontent));
        }else{
            System.out.println("Cannot run the Javascript logs recommendation");
        }

        if(performancelogscontent!=null){
            System.out.println("Prompt used for this request is: "+PromptHandler.PERFORMANCELOGSPROMPT);
           SingletonFileHandler.getInstance().writeToFile(performancelogrecommendationfilepath,genAIHandler.generateRecommendation(PromptHandler.PERFORMANCELOGSPROMPT,performancelogscontent));
        }else{
            System.out.println("Cannot run the Performance logs recommendation");
        }

        if(securitylogscontent!=null){
            System.out.println("Prompt used for this request is: "+PromptHandler.SECURITYLOGSPROMPT);
            SingletonFileHandler.getInstance().writeToFile(securitylogrecommendationfilepath,genAIHandler.generateRecommendation(PromptHandler.SECURITYLOGSPROMPT,securitylogscontent));
        }else{
            System.out.println("Cannot run the Security logs recommendation");
        }

        Alert alert=new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText("Recommendation task completed, \nCheck the logs for more details");
        alert.show();
    }
}