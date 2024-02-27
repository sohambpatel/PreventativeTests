package com.preventative.frameworkutils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.FileSystemNotFoundException;
import java.util.Properties;

public class PropertyFileLoader {
    private static Properties properties;

    //This is being used to read properties and make them available during the execution
    public static Properties readPropertyValues(String propertyfilepath){
        try{
            File configFile=new File(propertyfilepath);
            FileInputStream configFileReader=new FileInputStream(configFile);
            properties=new Properties();
            properties.load(configFileReader);
            configFileReader.close();
            return properties;
        }catch(FileSystemNotFoundException fileSystemNotFoundException){
            throw new RuntimeException(fileSystemNotFoundException);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
