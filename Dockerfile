FROM ubuntu:latest

# Install OpenJDK-8
RUN DEBIAN_FRONTEND=noninteractive apt-get update && \
    DEBIAN_FRONTEND=noninteractive apt-get install -y openjdk-17-jdk openjdk-17-jre && \
    DEBIAN_FRONTEND=noninteractive apt-get install -y openjfx && \
    DEBIAN_FRONTEND=noninteractive apt-get install -y ant && \
    DEBIAN_FRONTEND=noninteractive apt-get clean && \
    echo "Installing OpenJDK-8";
    
# Install JavaFX (11 version)
RUN DEBIAN_FRONTEND=noninteractive apt-get update && \
    DEBIAN_FRONTEND=noninteractive apt-get install -y --no-install-recommends openjfx && \
    rm -rf /var/lib/apt/lists/* && \
    echo "Installing JavaFX";


# Install Maven
RUN DEBIAN_FRONTEND=noninteractive apt-get update && \
    DEBIAN_FRONTEND=noninteractive apt-get install -y maven && \
    DEBIAN_FRONTEND=noninteractive apt-get clean && \
    echo "Installing Maven";

# Install cURL
RUN DEBIAN_FRONTEND=noninteractive apt-get update && \
    DEBIAN_FRONTEND=noninteractive apt-get install -y curl && \
    DEBIAN_FRONTEND=noninteractive apt-get clean && \
    echo "Installing cURL";

# Install Git
RUN DEBIAN_FRONTEND=noninteractive apt-get update && \
    DEBIAN_FRONTEND=noninteractive apt-get install -y git-all && \
    DEBIAN_FRONTEND=noninteractive apt-get clean && \
    echo "Installing Git";

# Install NodeJS
# RUN DEBIAN_FRONTEND=noninteractive apt-get update && \
#     DEBIAN_FRONTEND=noninteractive apt-get install -y nodejs && \
#     DEBIAN_FRONTEND=noninteractive apt-get clean && \
#     echo "Installing NodeJS";

# Install NPM
# RUN DEBIAN_FRONTEND=noninteractive apt-get update && \
#     DEBIAN_FRONTEND=noninteractive apt-get install -y npm && \
#     DEBIAN_FRONTEND=noninteractive apt-get clean && \
#     echo "Installing NPM";

# Install Docker
# RUN DEBIAN_FRONTEND=noninteractive apt-get update && \
#     DEBIAN_FRONTEND=noninteractive apt-get install -y apt-utils && \
#     DEBIAN_FRONTEND=noninteractive apt-get install -y snapd && \
#     DEBIAN_FRONTEND=noninteractive apt-get -y install docker.io && \
#     DEBIAN_FRONTEND=noninteractive apt-get clean && \
#     echo "Installing Docker";

# Install Android-SDK
# RUN DEBIAN_FRONTEND=noninteractive apt-get update && \
#     DEBIAN_FRONTEND=noninteractive apt-get install -y android-sdk && \
#     DEBIAN_FRONTEND=noninteractive apt-get clean && \
#     echo "Installing Android-SDK";

# Install Chrome
RUN DEBIAN_FRONTEND=noninteractive apt-get update && \
    DEBIAN_FRONTEND=noninteractive apt-get install -y wget && \
    DEBIAN_FRONTEND=noninteractive apt-get install -y fonts-liberation && \
    DEBIAN_FRONTEND=noninteractive apt-get install -y libgbm1 && \ 
    DEBIAN_FRONTEND=noninteractive apt-get install -y libgtk-3-0 && \
    DEBIAN_FRONTEND=noninteractive apt-get install -y libgtk-4-dev build-essential && \
    DEBIAN_FRONTEND=noninteractive apt-get install -y libxkbcommon0 && \
    DEBIAN_FRONTEND=noninteractive apt-get install -y xdg-utils && \
    wget https://dl.google.com/linux/direct/google-chrome-stable_current_amd64.deb && \
    dpkg --configure -a && \
    dpkg -i google-chrome-stable_current_amd64.deb && \
    DEBIAN_FRONTEND=noninteractive apt-get clean && \
    rm google-chrome-stable_current_amd64.deb  && \
    echo "Installing Chrome";

# Fixes sdkmanager error with java versions higher than java 8
RUN export JAVA_OPTS='-XX:+IgnoreUnrecognizedVMOptions --add-modules java.se.ee' && \
    echo "Fixing SDKManager";

# Fix certificate issues
RUN DEBIAN_FRONTEND=noninteractive apt-get update && \
    DEBIAN_FRONTEND=noninteractive apt-get install ca-certificates-java && \
    DEBIAN_FRONTEND=noninteractive apt-get clean && \
    update-ca-certificates -f && \
    echo "Fixing OpenJDK-8 Certificate Issues";

# Appium
# RUN npm install -g appium && \
#     echo "Installing Appium";


EXPOSE 8080
ADD preventativetestframework/target/preventativetestframework*.jar /preventativetestframework.jar

# Setup ANDROID_HOME
ENV ANDROID_HOME=/usr/lib/android-sdk
RUN export ANDROID_HOME && \
    export PATH=$PATH:${ANDROID_HOME}/tools/bin &&\
    export PATH=$PATH:${ANDROID_HOME}/platform-tools && \
    echo "Setting ANDROID_HOME";

# Setup CHROME_HOME
ENV CHROME_HOME=/usr/bin/google-chrome
RUN export CHROME_HOME && \
    export PATH=$PATH:${CHROME_HOME} && \
    echo "Setting CHROME_HOME";

# Setup GIT_HOME
ENV GIT_HOME=/usr/bin/git
RUN export GIT_HOME && \
    export PATH=$PATH:${GIT_HOME} && \
    echo "Setting GIT_HOME";

# Setup NODE_HOME
ENV NODE_HOME=/usr/bin/nodejs
RUN export NODE_HOME && \
    export PATH=$PATH:${NODE_HOME} && \
    echo "Setting NODE_HOME";

# Setup JAVA_HOME
ENV JAVA_HOME /usr/lib/jvm/java-17-openjdk-amd64
RUN export JAVA_HOME && \
    echo "Setting JAVA_HOME";

# Setup M2_HOME
ENV M2_HOME /usr/share/maven
RUN export M2_HOME && \
    echo "Setting M2_HOME";

# Setup PATH
RUN export PATH=${CHROME_HOME}:${GIT_HOME}:${NODE_HOME}:${ANDROID_HOME}:${M2_HOME}/bin:${JAVA_HOME}/bin/java:${PATH} && \
    echo "Setting PATH";

#Fix JAVAFX
RUN DEBIAN_FRONTEND=noninteractive apt-get update && \
	DEBIAN_FRONTEND=noninteractive apt-get install --no-install-recommends -y xorg -y openbox -y libgl1-mesa-glx && \
	rm -rf /var/lib/apt/lists/* 

ENTRYPOINT [ "java","-cp", "/usr/share/openjfx/lib/javafx.base.jar", "-cp", "/usr/share/openjfx/lib/javafx.fxml.jar", "-cp", "/usr/share/openjfx/lib/javafx.media.jar", "-cp", "/usr/share/openjfx/lib/javafx.swing.jar", "-cp", "/usr/share/openjfx/lib/javafx.controls.jar", "-cp", "/usr/share/openjfx/lib/javafx.graphics.jar", "-cp","/usr/share/openjfx/lib/javafx.web.jar","-jar","/preventativetestframework.jar" ]