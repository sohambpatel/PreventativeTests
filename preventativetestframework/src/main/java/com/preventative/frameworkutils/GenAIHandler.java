package com.preventative.frameworkutils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import org.apache.hc.client5.http.HttpResponseException;

public class GenAIHandler {
    String CHATGPTURL="";
    String API_KEY="";

    public GenAIHandler(String chatgpturl,String chatgptapikey){
        this.CHATGPTURL=chatgpturl;
        this.API_KEY=chatgptapikey;
    }

    //This is the java coded chat gpt completion api which further reports if the chat gpt is not returning 200
    public String generateRecommendation(String prompt, String content) throws Exception {
        String id = "";
        URL url = new URL(this.CHATGPTURL + "/chat/completions");
        HttpURLConnection httpConn = (HttpURLConnection)url.openConnection();
        InputStream inputStream;
        try {
            httpConn.setRequestMethod("POST");
            httpConn.setRequestProperty("Authorization", "Bearer " + this.API_KEY);
            httpConn.setRequestProperty("Content-Type", "application/json");
            httpConn.setDoOutput(true);
            OutputStreamWriter writer = new OutputStreamWriter(httpConn.getOutputStream());
            writer.write("{\n" +
                    "    \"model\": \"gpt-3.5-turbo\",\n" +
                    "    \"messages\": [\n" +
                    "        {\n" +
                    "            \"role\": \"system\",\n" +
                    "            \"content\": \""+prompt+"\"\n" +
                    "        },\n" +
                    "        {\n" +
                    "            \"role\": \"user\",\n" +
                    "            \"content\": \""+content+"\"\n" +
                    "        }\n" +
                    "    ]\n" +
                    "}");
            writer.flush();
            writer.close();
            String soham=httpConn.getOutputStream().toString();
            httpConn.getOutputStream().close();
            int responseCode = httpConn.getResponseCode();
            if (200 <= responseCode && responseCode <= 299) {
                inputStream = httpConn.getInputStream();
            } else {
                inputStream = httpConn.getErrorStream();
            }
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(
                            inputStream));

            StringBuilder response = new StringBuilder();
            String currentLine;

            while ((currentLine = in.readLine()) != null)
                response.append(currentLine);
            System.out.println("We are getting response code " + responseCode);
            //System.out.println("Request which were hitting: "+httpConn.getRequestProperties());
            System.out.println("Response Message is: "+response.toString());
            in.close();
            return response.toString();
        } catch (HttpResponseException httpResponseException) {
            httpResponseException.printStackTrace();
            System.out.println("Recommendation not generated");
            return httpConn.getResponseMessage();
        }

    }
}
