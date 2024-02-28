package com.preventative.frameworkutils;

public class PromptHandler {
    //Following are the prompts which are best to retrieve the information from chatgpt for js/console/performance/security logs
    public static final String JSLOGSPROMPT="Imagine you are tasked with analyzing JavaScript logs from a web application. These logs contain information about errors, warnings, and other events occurring within the application. Your goal is to examine these logs and provide recommendations for improving the application's stability and performance.To begin your analysis, familiarize yourself with the structure of JavaScript logs and identify common patterns or trends. Look for recurring error messages, warnings, or performance bottlenecks.Once you have a clear understanding of the logs, consider the following questions: What are the most frequent types of errors encountered in the logs?Are there any specific functions or modules consistently associated with errors or warnings?Do you notice any performance issues, such as slow-loading resources or high CPU usage?Are there any recurring issues that could be indicative of underlying bugs or design flaws?Based on your analysis, what recommendations would you make to improve the application's stability and performance?Keep in mind that your recommendations should be clear and actionable, especially for someone who is new to JavaScript and web development. Focus on practical steps that can be taken to address the identified issues and enhance the overall quality of the application.";
    public static final String CONSOLELOGSPROMPT="You've been given access to the console logs of a web application, which contain valuable information about its performance and any potential errors or warnings.Your task is to carefully examine these console logs and provide recommendations to improve the functionality and user experience of the web application";
    public static final String PERFORMANCELOGSPROMPT="I've noticed that my website is running a bit slow, especially when loading certain pages or performing specific actions. I think it might have something to do with the performance. Can you help me figure out what's going on? Here are the logs";
    public static final String SECURITYLOGSPROMPT="I've been using ZAP Security testing tool to check the security of my web application, but I'm not sure how to interpret the logs it generates. Can you help me understand them and suggest some improvements? Here are the logs";
    public static String promptReturner(String promptname){
        if(promptname=="CONSOLELOGSPROMPT"){
            return CONSOLELOGSPROMPT;
        }else if(promptname=="JSLOGSPROMPT"){
            return JSLOGSPROMPT;
        }else if(promptname=="PERFORMANCELOGSPROMPT"){
            return PERFORMANCELOGSPROMPT;
        } else if (promptname=="SECURITYLOGSPROMPT") {
            return SECURITYLOGSPROMPT;
        }
        return null;
    }
}
