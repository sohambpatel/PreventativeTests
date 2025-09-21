package com.preventative.frameworkutils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class CustomGPTGenAIHandler {
    private static String CHATGPTURL="";   // e.g., https://api.openai.com/v1/chat/completions
    private static String API_KEY="";

    public CustomGPTGenAIHandler(String chatgpturl, String chatgptapikey){
        this.CHATGPTURL = chatgpturl;
        this.API_KEY    = chatgptapikey;
    }

    // Calls Chat Completions and returns the assistant's text (choices[0].message.content)
    public String generateRecommendation(String prompt, String content) throws Exception {
        String SYSTEM = "You are Preventative Test Pro GPT, a fun, warm, and motivational guide for observability-driven software quality engineering. You excel at helping users analyze stack traces, logs, metrics, and trace data from observability tools, identifying root causes, and generating clear, actionable test cases. Your expert insights make quality engineering easy to understand, using simple language and friendly encouragement. You are always professional, approachable, and focus on helping users improve software quality through relevant analysis, prioritization of test cases, and practical recommendations. You only address topics related to observability and software quality; if asked about anything else, kindly steer the conversation back on topic. Always ask for more clarification before giving detailed answers, making sure you fully understand the user's data or question first. Your tone is positive, helpful, and supportive, making users feel confident in their journey to better software quality.";

        // Build request body for Chat Completions
        // Model: pick any chat-capable model you have access to (e.g., gpt-4o-mini or gpt-4.1-mini)
        String body = """
        {
          "model": "gpt-4o-mini",
          "messages": [
            {"role": "system", "content": %s},
            {"role": "user",   "content": %s}
          ],
          "temperature": 0.3
        }
        """.formatted(jsonString(SYSTEM), jsonString(prompt + content));

        HttpURLConnection conn = (HttpURLConnection) new URL(CHATGPTURL+ "/chat/completions").openConnection();
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        conn.setRequestProperty("Authorization", "Bearer " + API_KEY);
        conn.setRequestProperty("Content-Type", "application/json");

        try (OutputStream os = conn.getOutputStream();
             OutputStreamWriter w = new OutputStreamWriter(os, StandardCharsets.UTF_8)) {
            w.write(body);
        }

        int status = conn.getResponseCode();
        InputStream is = (status >= 200 && status < 300) ? conn.getInputStream() : conn.getErrorStream();

        String resp;
        try (BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            StringBuilder sb = new StringBuilder();
            for (String line; (line = br.readLine()) != null; ) sb.append(line);
            resp = sb.toString();
        }

        if (status < 200 || status >= 300) {
            throw new RuntimeException("OpenAI API error " + status + ": " + resp);
        }

        // Minimal JSON extraction of choices[0].message.content without external libs
        // (For production, use Jackson/Gson.)
        String contentKey = "\"content\":\"";
        int i = resp.indexOf(contentKey);
        if (i == -1) return resp; // fallback: raw JSON
        i += contentKey.length();
        StringBuilder out = new StringBuilder();
        boolean escape = false;
        for (int j = i; j < resp.length(); j++) {
            char c = resp.charAt(j);
            if (escape) {
                // handle a few common escapes
                switch (c) {
                    case 'n': out.append('\n'); break;
                    case 't': out.append('\t'); break;
                    case 'r': out.append('\r'); break;
                    case '"': out.append('"');  break;
                    case '\\': out.append('\\'); break;
                    default: out.append(c);
                }
                escape = false;
            } else if (c == '\\') {
                escape = true;
            } else if (c == '"') {
                break;
            } else {
                out.append(c);
            }
        }
        return out.toString().trim();
    }

    // Utility: JSON-escape and quote a Java string
    private static String jsonString(String s) {
        StringBuilder b = new StringBuilder("\"");
        for (char c : s.toCharArray()) {
            switch (c) {
                case '\\' -> b.append("\\\\");
                case '"'  -> b.append("\\\"");
                case '\b' -> b.append("\\b");
                case '\f' -> b.append("\\f");
                case '\n' -> b.append("\\n");
                case '\r' -> b.append("\\r");
                case '\t' -> b.append("\\t");
                default -> {
                    if (c < 0x20) b.append(String.format("\\u%04x", (int)c));
                    else b.append(c);
                }
            }
        }
        b.append('"');
        return b.toString();
    }

    public static void main(String[] args) throws Exception {
        //String apiKey = System.getenv("OPENAI_API_KEY"); // safer than hard-coding

        CustomGPTGenAIHandler handler = new CustomGPTGenAIHandler(
                CHATGPTURL,
                API_KEY
        );
        String result = handler.generateRecommendation("This is it", " checkjava");
        System.out.println(result);
    }
}