package com.preventative.frameworkutils;

import com.preventative.frameworkui.RecordingController;
import org.zaproxy.clientapi.core.ClientApi;
import org.zaproxy.clientapi.core.ClientApiException;

public class SecrityTestRecorder {

    RecordingController recordingController;

    public void tearDown(ClientApi api) throws ClientApiException {
        if(api!=null){
            String title="Security Report";
            String template="traditional-html";
            String description="This is auto generated html security scan report";
            String reportfilename="security_zap_report.html";
            String targetfolder;
            if(System.getProperty("os.name").contains("Windows")){
                targetfolder = System.getProperty("user.home")+"\\Downloads\\";
            }else{
                targetfolder = System.getProperty("user.name")+"/Downloads/";
            }
            api.reports.generate(title,template,null,description,null,null,null,null,null,reportfilename,null,targetfolder,null);
        }
    }
}
