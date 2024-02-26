package com.preventative.frameworkui;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
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
    void stop(ActionEvent event) throws IOException, ClientApiException {
        recordMetrics=new MetricsRecorder(driver);
        recordMetrics.captureJSLogsMetrics();
        secrityTestRecorder=new SecrityTestRecorder();
        secrityTestRecorder.tearDown(clientApi);
        driver.quit();
    }
    @FXML
    void getRecommendation(ActionEvent event) throws Exception {
        if (System.getProperty("os.name").contains("Windows")) {
            consolelogscontent= SingletonFileHandler.getInstance().readFile(System.getProperty("user.home")+"/Downloads/consolelogs.log").replaceAll("\\s","").replaceAll("\"", "'");
            jslogscontent=SingletonFileHandler.getInstance().readFile(System.getProperty("user.home")+"/Downloads/js.log").replaceAll("\\s","").replaceAll("\"", "'");
            performancelogscontent=SingletonFileHandler.getInstance().readFile(System.getProperty("user.home")+"/Downloads/performancelogs.log").replaceAll("\\s","").replaceAll("\"", "'");
            securitylogscontent=SingletonFileHandler.getInstance().readFile(System.getProperty("user.home")+"/Downloads/security_zap_report.html").replaceAll("\\s","").replaceAll("\"", "'");
        } else {
            consolelogscontent=SingletonFileHandler.getInstance().readFile(System.getProperty("user.name")+"/Downloads/consolelogs.log").replaceAll("\\s","").replaceAll("\"", "'");
            jslogscontent = SingletonFileHandler.getInstance().readFile(System.getProperty("user.name")+"/Downloads/js.log").replaceAll("\\s","").replaceAll("\"", "'");
            performancelogscontent=SingletonFileHandler.getInstance().readFile(System.getProperty("user.name")+"/Downloads/performancelogs.log").replaceAll("\\s","").replaceAll("\"", "'");
            securitylogscontent=SingletonFileHandler.getInstance().readFile(System.getProperty("user.name")+"/Downloads/security_zap_report.html").replaceAll("\\s","").replaceAll("\"", "'");
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
            SingletonFileHandler.getInstance().writeToFile(consolelogrecommendationfilepath,genAIHandler.generateRecommendation(PromptHandler.CONSOLELOGSPROMPT,consolelogscontent));
        }else{
            System.out.println("Cannot run the Console logs recommendation");
        }

        if (jslogscontent!=null){
            SingletonFileHandler.getInstance().writeToFile(jslogrecommendationfilepath,genAIHandler.generateRecommendation(PromptHandler.JSLOGSPROMPT,jslogscontent));
        }else{
            System.out.println("Cannot run the Javascript logs recommendation");
        }

        if(performancelogscontent!=null){
           SingletonFileHandler.getInstance().writeToFile(performancelogrecommendationfilepath,genAIHandler.generateRecommendation(PromptHandler.PERFORMANCELOGSPROMPT,performancelogscontent));
        }else{
            System.out.println("Cannot run the Performance logs recommendation");
        }

        if(securitylogscontent!=null){
            SingletonFileHandler.getInstance().writeToFile(securitylogrecommendationfilepath,genAIHandler.generateRecommendation(PromptHandler.SECURITYLOGSPROMPT,securitylogscontent));
        }else{
            System.out.println("Cannot run the Security logs recommendation");
        }

        Alert alert=new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText("Recommendation task completed, \nCheck the logs for more details");
        alert.show();
    }


    private Map<String, String> processWindowHandles(WebDriver driver) {
        Map<String, String> allwindows = new LinkedHashMap<>();
        Set<String> allWindowHandles = driver.getWindowHandles();
        String mainWindowHandle = driver.getWindowHandle();
        for (String win : allWindowHandles) {

            allwindows.put(win, driver.switchTo().window(win).getTitle());
        }
        driver.switchTo().window(mainWindowHandle);
        return allwindows;
    }

    private void execute(String fileContents, JavascriptExecutor js) {

        js.executeScript(String.format("return window.onbeforeunload = function(){ window.localStorage.setItem('refresh', 'yes');}"));

        String ss = (String) js.executeScript(String.format("return window.localStorage.getItem('%s');", "refresh"));

        if (ss != null && !ss.equalsIgnoreCase("no")) {
            System.out.println(ss);
            js.executeScript(String.format("return window.localStorage.setItem('refresh', 'no');"));
            js.executeScript(fileContents);
        }

    }

    public boolean waitForPageLoad(WebDriver driver) {
        if (new WebDriverWait(driver, Duration.ofSeconds(100)).until((ExpectedCondition<Boolean>) wd ->
                ((JavascriptExecutor) wd).executeScript("return document.readyState").equals("complete")) != null) {

            return true;

        }
        return false;
    }

    public boolean isBrowserClosed(WebDriver driver) {
        boolean isClosed = false;
        try {
            driver.getTitle();
        } catch (Exception ubex) {
            if (waitForPageLoad(driver)) {
                JavascriptExecutor js = ((JavascriptExecutor) driver);
                // js.executeScript(String.format("return window.localStorage.setItem('mainurl', ('%s'));", url));
                System.out.println((String) js.executeScript(String.format("return window.localStorage.getItem('%s');", "objectRepo")));
            }
            isClosed = true;
        }

        return isClosed;
    }


}