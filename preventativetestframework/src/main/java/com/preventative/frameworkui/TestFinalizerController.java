package com.preventative.frameworkui;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
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

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestFinalizerController {

    @FXML
    private TextField OUTPUTPATH;

    @FXML
    private TextField TESTREPOPATH;

    @FXML
    private TextField JSONPATH;

    @FXML
    private ImageView backButton;

    @FXML
    private Text message;

    @FXML
    private Button getClassNameMethodNameButton;

    @FXML
    private Button getTestCasesButton;

    JavaExceptionHandler exceptionHandler;
    JSONFilePathHandler jsonFilePathHandler;
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
    @FXML
    void updateJSONPath(KeyEvent event){
        getMessage().setVisible(false);
    }

    private String getSyntheticAppMonitoringOutputFilePath() {
        return OUTPUTPATH.getText();
    }

    private String getTestRepoPATH() {
        return TESTREPOPATH.getText();
    }
    private String getJsonPATH() {
        return JSONPATH.getText();
    }


    @FXML
    void frameworkScene(MouseEvent event) throws IOException {
        Parent root = FXMLLoader.load(new ClassPathResource("/fxml/Framework.fxml").getURL());
        OUTPUTPATH.getScene().setRoot(root);
    }
    //This is being used to read the synthetic app monitoring output
    //Also parse th exception data based on the json path, segregate the method names and class names seperately
    @FXML
    void getClassMethodNames(ActionEvent event) throws ClientApiException {
        String jsonfilepath=getSyntheticAppMonitoringOutputFilePath();
        String jsonpath=getJsonPATH();
        try {
            if(jsonfilepath.isEmpty()){
                throw new RuntimeException("Please enter a valid path");
            }
        } catch (RuntimeException e) {
            Alert alert=new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Please enter a valid \nSynthetic App Monitoring Output File Path");
            alert.show();
            return;
        }
        try {
            if(jsonpath.isEmpty()){
                throw new RuntimeException("Please enter a valid path");
            }
        } catch (RuntimeException e) {
            Alert alert=new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Please enter a valid Json Path");
            alert.show();
            return;
        }
        String[] segregatedjsonpath=jsonpath.split("\\[|\\]");
        String getbasepath=segregatedjsonpath[0];
        String getpathvariable=segregatedjsonpath[2].replace(".","");
        jsonFilePathHandler=new JSONFilePathHandler();
        exceptionHandler=new JavaExceptionHandler();
        String jsoncontent=jsonFilePathHandler.jsonFileReader(jsonfilepath);
        List<Object> exceptions= jsonFilePathHandler.readJsonPathUsingDocument(jsoncontent,getbasepath);
        ArrayList<String> classname=new ArrayList<>();
        ArrayList<String> methodname=new ArrayList<>();
        for(Object exception:exceptions){
            Map<String, String> map = (Map<String, String>) exception;
            classname=exceptionHandler.parseJavaExceptionReturnClassName(map.get(getpathvariable),"class");
            methodname=exceptionHandler.parseJavaExceptionReturnClassName(map.get(getpathvariable),"method");
        }
        System.out.print("Json file processing is done!!");
        String classnamefilepath;
        String methodnamefilepath;
        if(System.getProperty("os.name").contains("Windows")){
            classnamefilepath = System.getProperty("user.home")+"\\Downloads\\classname.txt";
            methodnamefilepath = System.getProperty("user.home")+"\\Downloads\\methodname.txt";
        }else{
            classnamefilepath = System.getProperty("user.home")+"/Downloads/classname.txt";
            methodnamefilepath = System.getProperty("user.home")+"/Downloads/methodname.txt";
        }

        try {
            SingletonFileHandler.getInstance().writeToFile(classnamefilepath, classname.toString());
            SingletonFileHandler.getInstance().writeToFile(methodnamefilepath, methodname.toString());
            System.out.print("Exception processing is done!!");
        } catch (IOException e) {
            System.out.println(classnamefilepath+" doesnt exist or doent have data in it, please check the file and path");
            System.out.println(methodnamefilepath+" doesnt exist or doent have data in it, please check the file and path");
            throw new RuntimeException(e);
        }

    }
    //This is being used to generate the list of test cases which are relevent as per exception data
    //Code coverage information is being used to classify the test cases based on the exception data
    @FXML
    void getTestCases(ActionEvent event) throws IOException, ClientApiException {
        String testrepopath=getTestRepoPATH();
        try {
            if(testrepopath.isEmpty()){
                throw new RuntimeException("Please enter a valid path");
            }
        } catch (RuntimeException e) {
            Alert alert=new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Please enter a valid Test Repo Path");
            alert.show();
            return;
        }
        String classnamefilepath;
        String methodnamefilepath;
        if(System.getProperty("os.name").contains("Windows")){
            classnamefilepath = System.getProperty("user.home")+"\\Downloads\\classname.txt";
            methodnamefilepath = System.getProperty("user.home")+"\\Downloads\\methodname.txt";
        }else{
            classnamefilepath = System.getProperty("user.home")+"/Downloads/classname.txt";
            methodnamefilepath = System.getProperty("user.home")+"/Downloads/methodname.txt";
        }
        System.out.print("Starting the Class file execution");
        String classcontent=SingletonFileHandler.getInstance().readFile(classnamefilepath);
        SingletonFileHandler.getInstance().reset();
        String methodcontent=SingletonFileHandler.getInstance().readFile(methodnamefilepath);

        Pattern ptr=Pattern.compile(",");
        String[] classcontentarray=ptr.split(classcontent.trim());
        String[] methodcontentarray=ptr.split(methodcontent.trim());
        File directory = new File(testrepopath);
        for(String classname:classcontentarray){
            classname=classname.replaceAll("[\\[\\]]","");
            directoryCrawler(directory,classname.trim());
        }
        for(String methodname:methodcontentarray){
            methodname=methodname.replaceAll("[\\[\\]]","");
            directoryCrawler(directory,methodname.trim());
        }

    }

    //This is to crawl the directory and file it contains
    public static void directoryCrawler(File directory, String searchWord) throws IOException {

        File[] filesAndDirs = directory.listFiles();
        try {
            // Iterate the list of files, if it is identified as not a file call
            // directoryCrawler method to list all the Spec files in that directory.
            for (File file : filesAndDirs) {

                if (file.isFile()) {
                    if (file.getName().endsWith(".java")) {
                            fileDifferentiator(file, searchWord);
                    }
                } else {
                    directoryCrawler(file, searchWord);
                }
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    //This is a custom logic to short list and crawl the files selected based on exception data and return the test case if this matches the same information
    public static void fileDifferentiator(File file,String searchWord) throws IOException
    {
        String line="";
        BufferedReader br;
        String finalisedtestcases;
        if(System.getProperty("os.name").contains("Windows")){
            finalisedtestcases = System.getProperty("user.home")+"\\Downloads\\finalisedtestcases.txt";
        }else{
            finalisedtestcases = System.getProperty("user.home")+"/Downloads/finalisedtestcases.txt";
        }
        br = new BufferedReader(new FileReader(file));
        try
        {
            while ((line = br.readLine()) != null) {
                Pattern p = Pattern.compile(("\\b" + searchWord + "\\b"));
                Matcher m = p.matcher(line);
                while (m.find()) {
                    if (line.contains(searchWord)) {
                        System.out.println(file.getAbsolutePath());
                        SingletonFileHandler.getInstance().writeToFile(finalisedtestcases, file.getAbsolutePath());
                        break;
                    }
                }
            }

        }
        catch (Exception e)
        {
            System.out.println(file.getAbsolutePath()+"\n"+line);

            e.printStackTrace();
        }
    }
}