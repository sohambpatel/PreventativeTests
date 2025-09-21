package com.preventative.frameworkui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.*;

@Component
public class FrameworkController {

    @FXML
    private ComboBox jenkinsFileList;

    @FXML
    protected TextField projectLocation;

    @FXML
    TextField projectName;

    @FXML
    Button browseProjectLoc;

    @FXML
    protected TextField objectRepoLoc;

    @FXML
    Button browseObjectRepo;

    @FXML
    protected Button startcodegenerationButton;

    @FXML
    RadioButton openexistingprojectButton;

    @FXML
    RadioButton startRecordingButton;

    @FXML
    RadioButton startPreventativeTests;

    @FXML
    RadioButton startManualTestCaseGeneration;

    @FXML
    RadioButton startAutomatedTestCaseGeneration;

    @FXML
    RadioButton startRCA;

    @FXML
    protected Button jiraxrayintegration;

    String selectedJenkinsFile = "";

    private java.io.File selectedDirectory;

    private java.io.File selectedFile;

    public static String replaceFileSeparator(String filePath) {
        if (filePath.contains("\\")) {
            return filePath.replace("\\", "/");
        }
        return filePath;
    }

    @FXML
    void startrecordingScene(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(new ClassPathResource("/fxml/WebRecording.fxml").getURL());
        startRecordingButton.getScene().setRoot(root);
    }

    @FXML
    void startpreventativeScene(ActionEvent event) throws IOException{
        Parent root = FXMLLoader.load(new ClassPathResource("/fxml/PreventativeTest.fxml").getURL());
        startRecordingButton.getScene().setRoot(root);
    }
    @FXML
    void starttestcasegenerationScene(ActionEvent event) throws IOException{
        Parent root = FXMLLoader.load(new ClassPathResource("/fxml/ManualTestCaseGenerator.fxml").getURL());
        startManualTestCaseGeneration.getScene().setRoot(root);
    }
    @FXML
    void startautomatedtestgenerationScene(ActionEvent event) throws IOException{
        Parent root = FXMLLoader.load(new ClassPathResource("/fxml/AutomatedTestCaseGenerator.fxml").getURL());
        startAutomatedTestCaseGeneration.getScene().setRoot(root);
    }
    @FXML
    void startrcaScene(ActionEvent event) throws IOException{
        Parent root = FXMLLoader.load(new ClassPathResource("/fxml/RCA.fxml").getURL());
        startRCA.getScene().setRoot(root);
    }
}
