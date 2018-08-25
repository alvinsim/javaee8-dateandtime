# javaee8-dateandtime

This is a Java EE 8 sample application to demonstrate the usage of JAX-RS and JAX-WS.

We will use the example of calculating the date and time.

This project is separated into different modules (jaxrs) to demonstrate how different functionality is built and executed.

For simplicity sake, this project is a datetime calculator. You can call the respective services/API to count differences between two dates in days, add or minus datetime, etc.

## Pre-requisites

Besides from the dependencies/libraries defined in the Maven `pom.xml`, there are some libraries/utilites/application which need to be installed first.

1. **Java JDK**. This project uses **JDK version 8**, which you can download from [Oracle](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html).
2. **Apache Maven**. If you currently do not have Maven installed in your local machine, grab the latest version (as of this writing is **3.5.4**) from [here](https://maven.apache.org/download.cgi).
3. **Apache Tomcat**. As of this writing, latest version is **9**. I have tested with versions **7**, **8**, and **9** and they work without any issues.
4. **IDE/Text Editor**. Use your favourite Java IDE or any text editor you fancy. If not sure, you can download [Intellij Community Edition](https://www.jetbrains.com/idea/download/index.html#section=windows), [Netbeans](https://netbeans.org/downloads/) or [Eclipse](https://www.eclipse.org/downloads/).  

## Building the Project

1. Fork and clone this project to your local machine.
2. In the project's root directory, execute `mvn clean install` command. 

## Module: JAXRS

This module is specifically for [JAX-RS](https://en.wikipedia.org/wiki/Java_API_for_RESTful_Web_Services), which is Java's API for RESTful Web Services.

The artifact of this project, which is a `war` file, will be deployed to an instance of **Apache Tomcat**. But, before we start, there are a few changes we need to do to the **Apache Maven**'s and **Apache Tomcat**'s configuration files.

#### Apache Tomcat `tomcat-user.xml`

1. Open the `tomcat-user.xml` file with your choice of IDE/text editor. This file is located at `$TOMCAT_HOME/conf`.
2. Make the following changes to the `<tomcat-users></tomcat-users>` XML tag.
```xml
<tomcat-users>
  <role rolename="manager-gui"/>
  <role rolename="manager-script"/>
  <user username="admin" password="password" roles="manager-gui,manager-scripts"/>
</tomcat-users>
```
If you already have an instance of **Apache Tomcat** installed and the above is already configured but using a different username/password, you can either
1. Do the above changes on a new **Apache Tomcat** instance, or
2. Change the username and password accordingly in the `$PROJECT_HOME/jaxrs/pom.xml`.

#### Apache Maven `settings.xml`

1. Open the `settings.xml` file with your choice of IDE/text editor. This file is located at `$M2_HOME/conf`. 
2. Make the following changes to the `<servers><servers>` XML tag.

```xml
<servers>
  <server>
    <id>TomcatServer</id>
    <username>admin</username>
    <password>password</password>
  </server>
</servers>
```

#### Deploy to Apache Tomcat

1. Open a command prompt or terminal and go to your **Apache Tomcat** instance's `bin` directory (`$TOMCAT_HOME/bin`).
2. Run the command `startup.sh` or `startup.bat` if you are on Windows. 
3. After your instane of **Apache Tomcat** is up, open a different command prompt/terminal and go to the project's root directory.
4. Execute the **Apache Maven** command `mvn clean tomcat7:deploy -pl jaxrs`.
5. Or you can go to the `jaxrs` module (`$PROJECT_HOME/jaxrs`) and run `mvn clean tomcat7:deploy`.
6. After deployment is done, open your web browser and go to the URL [http://localhost:8080/webapp/service/datetime/echo/2018-08-08](http://localhost:8080/webapp/service/datetime/echo/2018-08-08). You will see the response of this example REST Web Service. 