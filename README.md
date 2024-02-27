# Preventative Tests

This repo consist of a code related to Preventative tests which further has lot features. Just give the brief on what preventative tests are and what are the objectives of the same; Preventative Tests are the tests designed to reveal the application defects as soon as possible, in a efficient manner and with lot of time being saved.
## Detailed Documentation
https://github.com/sohambpatel/PreventativeTests/wiki
## Objectives :
- Parallel test execution with following metrics captured during the test execution : We are capturing javascript logs, console logs, performance logs and security logs with the help of ZAP. It also generate the recommendation based on the LLM(currently using chatgpt) and generated logs.
- Finalizing the test cases for execution based on Synthetic app monitoring outcome. Underneath it uses exception captured, parsed them, get class name and method name, code coverage information captured for each test case during its execution and based on the same provide the test cases to be executed to reveal the more issues.


## Installation

You can download the generated jar from mendeley data repository and link for the same is

## Usage

```java
java -jar preventativetestframework.jar
```

## Pre-requisites
- Java : you would need java to install on your machine and setup the path
https://phoenixnap.com/kb/install-java-windows
- Maven : install maven with the help of https://phoenixnap.com/kb/install-maven-windows
- ZAP installation
https://techofide.com/blogs/how-to-install-owasp-zap-on-windows-and-linux/
- Git Installation
https://www.simplilearn.com/tutorials/git-tutorial/git-installation-on-windows

Rest all the necessary software have been added as a part of maven dependency and will get taken care at runtime or if you are using jar, then all the dependencies are part of jar only.
## Running the code on your machine

### Running it using code
- You can clone this repo and setup the project
- Please refer to the pre-requisite section of this read me before you do that and make sure all the necessary software are installed and paths for the same has been set in the environment variable
- After that, please execute the following command to download dependancies
```java
mvn dependency:resolve
```
- Post that execute the following command to run the application
```java
mvn spring-boot:run
```
### Running it using docker
- We have created a docker file and attached in the same repo.
- Please create a docker image with the same, please refer to this article for more information 
https://phoenixnap.com/kb/create-docker-images-with-dockerfile

## Contributing

Pull requests are welcome. For major changes, please open an issue first
to discuss what you would like to change.

Please make sure to update tests as appropriate.

## License

[APACHE](https://www.apache.org/licenses/LICENSE-2.0)
