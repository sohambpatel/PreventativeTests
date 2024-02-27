package com.preventative.frameworkutils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JavaExceptionHandler {

public ArrayList<String> classname;
public ArrayList<String> methodname;


    //Parses the java exceptions and returns the methods and classes
    public ArrayList<String> parseJavaExceptionReturnClassName(String exceptionstacktrace, String classormethod) {
        classname=new ArrayList<String>();
        methodname=new ArrayList<String>();
        Pattern pattern = Pattern.compile("\\s*at\\s+([\\w\\.$_]+)\\.([\\w$_]+)\\(([^:]+):(\\d+)\\)");
        Matcher matcher = pattern.matcher(exceptionstacktrace);

        while (matcher.find()) {
            String className = matcher.group(1);
            String methodName = matcher.group(2);
            classname.add(className);
            methodname.add(methodName);
        }
        if(classormethod.equalsIgnoreCase("class")){
            return classname;
        }else{
            return methodname;
        }
    }
}
