package com.preventative;

import com.preventative.frameworkui.FrameworkUI;

import javafx.application.Application;

import org.springframework.boot.autoconfigure.SpringBootApplication;

//import javax.annotation.PreDestroy;

@SpringBootApplication
public class PreventativeTestApplication {
	public static void main(String[] args) {
		Application.launch(FrameworkUI.class,args);
	}

//	@PreDestroy
//	public void destroy(){
//	}
}
