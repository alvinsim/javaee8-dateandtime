# java-datetimecalc

This is a sample Java application to demonstrate the usage of JAX-RS.

We will use the example of calculating the date and time.

This project is separated into different modules (jaxrs) to demonstrate how different functionality is built and executed.

For simplicity sake, this project is a datetime calculator. You can call the respective API to count the differences between two dates in days, add or minus datetime, etc.

## Pre-requisites

Besides from the dependencies/libraries defined in the Maven `pom.xml`, there are some libraries/utilites/application which need to be installed first.

1. **Java JDK**. You can download from [Oracle](https://www.oracle.com/technetwork/java/javase/downloads/index.html).
2. **Apache Maven**. If you currently do not have Maven installed in your local machine, grab the latest version (as of this writing is **3.6.2**) from [here](https://maven.apache.org/download.cgi).
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
  <user username="admin" password="password" roles="manager-gui,manager-script"/>
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
3. After your instance of **Apache Tomcat** is up, open a different command prompt/terminal and go to the project's root directory.
4. Execute the **Apache Maven** command `mvn clean tomcat7:deploy -pl jaxrs`, or you can go to the `jaxrs` module directory (`$PROJECT_HOME/jaxrs`) and run `mvn clean tomcat7:deploy`.
6. After deployment is done, open your web browser and go to the URL [http://localhost:8080/datetime-calc/api/today](http://localhost:8080/datetime-calc/api/today). You will see the response of this example REST Web Service. Or you can open a command prompt and use `curl` instead.

## API Reference

### Get Current date and time

Displays the current date and time.

#### Endpoint

```
GET http://localhost:8080/datetime-calc/api/today
```

#### Example Request

```
curl --get http://localhost:8080/datetime-calc/api/today
```

#### Example Response

```json
{
  "status": "success",
  "data": {
    "result": {
      "date": "2019-11-01",
      "dayOfWeek": "FRIDAY",
      "time": "14:23:28"
    }
  }
}
```
### Count difference between two dates and times

Given two dates (_with or without time_), it returns the difference.

#### Endpoint

```
GET http://localhost:8080/datetime-calc/api/count/:fromDateTime/:toDateTime?fromTimeZone=:fromTimeZone&toTimeZone=:toTimeZone
```

#### Parameters

- `fromDateTime` `[yyyy-MM-dd'T'HH:mm:ss]`
- `toDateTime` `[yyyy-MM-dd'T'HH:mm:ss]`
- `fromTimeZone` _(Optional)_
- `toTimeZone` _(Optional)_

#### Example Request

```
curl --get http://localhost:8080/datetime-calc/api/count/2018-08-23T00:00:00/2018-08-24T00:00:00

# with timezones
curl --get http://localhost:8080/datetime-calc/api/count/2017-08-31T00:00:00/2017-09-01T01:00:00 -d fromTz=Asia/Kuala_Lumpur -d toTz=Australia/Melbourne

```

#### Example Response

```json
{
  "status": "success",
  "data": {
    "result": {
      "chronoUnitData": {
        "weeks": 0,
        "days": 1,
        "hours": 24,
        "minutes": 1440,
        "seconds": 86400
      },
      "periodData": {
        "days": 1,
        "hours": 0,
        "minutes": 0,
        "seconds": 0
      }
    }
  }
}
```

### Add years, months, weeks, days, hours, minutes or seconds to a date and time

Adding years, months, weeks, days, hours, minutes or seconds to a date (_with or without_) time.

#### Endpoint

```
GET http://localhost:8080/datetime-calc/api/add/:dateTime?addYears=:addYears&addMonths=:addMonths&addWeeks=:addWeeks&addDays=:addDays&addHours=:addHours&addMinutes=:addMinutes&addSeconds=:addSeconds
```

#### Parameters

- `dateTime` `[yyyy-MM-dd'T'HH:mm:ss]`
- `addYears` _(Optional)_
- `addMonths` _(Optional)_
- `addWeeks` _(Optional)_
- `addDays` _(Optional)_
- `addHours` _(Optional)_
- `addMinutes` _(Optional)_
- `addSeconds` _(Optional)_

#### Example Request

```
curl --get http://localhost:8080/datetime-calc/api/add/2017-08-31T00:00:00 -d addYears=1
```

#### Example Response

```json
{
  "status": "success",
  "data": {
    "result": "2018-08-31 00:00:00"
  }
}
```