package com.preventative.frameworkui;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

@Component
public class FrameworkUIInitializer implements ApplicationListener<FrameworkUI.StageReadyEvent> {

    private ApplicationContext applicationContext;
    private String applicationTitle;

    @Autowired
    FrameworkUIInitializer(ApplicationContext applicationContext,
                     @Value("${spring.application.ui.title}") String applicationTitle) {
        this.applicationContext = applicationContext;
        this.applicationTitle = applicationTitle;
    }
    @Override
    public void onApplicationEvent(FrameworkUI.StageReadyEvent event) {
        Stage stage = event.getStage();
        stage.setResizable(false);
       // stage.setTitle("Welcome!");
        Parent root = null;
        try {
            root = FXMLLoader.load(new ClassPathResource("/fxml/Framework.fxml").getURL());
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scene scene = new Scene(root);
        stage.setTitle("Preventative Tests");
        stage.setScene(scene);
        stage.show();

    }
}
