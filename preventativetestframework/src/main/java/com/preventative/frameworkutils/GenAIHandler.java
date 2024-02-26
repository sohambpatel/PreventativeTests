package com.preventative.frameworkutils;

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

    public String generateRecommendation(String prompt, String content) throws Exception {
        try {
            String id = "";
            URL url = new URL(this.CHATGPTURL + "/chat/completions");
            HttpURLConnection httpConn = (HttpURLConnection)url.openConnection();
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
            if (responseCode != 204 || responseCode!=200) {
                System.out.print("Response is: "+httpConn.getResponseMessage());
                throw new HttpResponseException(responseCode, "We are getting other than 200 or 204 for api call");
            }

            System.out.println("We are getting correct response code " + responseCode);
            System.out.println("Request which were hitting: "+httpConn.getRequestProperties());
            System.out.println("Response Message is: "+httpConn.getResponseMessage());
            return httpConn.getResponseMessage();
        } catch (HttpResponseException httpResponseException) {
            httpResponseException.printStackTrace();
            System.out.println("Recommendation not generated");
            return null;
        }

    }
}
