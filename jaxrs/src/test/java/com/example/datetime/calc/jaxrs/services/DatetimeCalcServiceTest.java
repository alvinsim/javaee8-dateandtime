package com.example.datetime.calc.jaxrs.services;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Assert;
import org.junit.Test;

import javax.json.Json;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Application;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DatetimeCalcServiceTest extends JerseyTest {

    private final static String RESPONSE_DATA = "data";
    private final static String RESPONSE_RESULT = "result";
    private final static String RESPONSE_STATUS = "status";
    private final static String RESPONSE_STATUS_SUCCESS = "success";

    @Override
    protected Application configure() {
        return new ResourceConfig(DatetimeCalcService.class);
    }

    @Test
    public void shouldReturnTodaysDateValues() {
        String response = target("/today").request().get(String.class);
        LocalDateTime currentDateTime = LocalDateTime.now();
        String expectedResponse = Json.createObjectBuilder()
                .add(RESPONSE_STATUS, RESPONSE_STATUS_SUCCESS)
                .add(RESPONSE_DATA, Json.createObjectBuilder()
                        .add(RESPONSE_RESULT, Json.createObjectBuilder()
                                .add("date", currentDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                                .add("dayOfWeek", String.valueOf(currentDateTime.getDayOfWeek()))
                                .add("time", currentDateTime.format(DateTimeFormatter.ofPattern("HH:mm:ss")))
                                .build())
                        .build())
                .build()
                .toString();
        Assert.assertEquals(expectedResponse, response);
    }

    @Test
    public void shouldReturnCalculatedDaysFromTwoDates() {
        String response = target("/count/2018-08-23T00:00:00/2018-08-24T00:00:00")
                .request()
                .get(String.class);
        String expectedResponse = Json.createObjectBuilder()
                .add(RESPONSE_STATUS, RESPONSE_STATUS_SUCCESS)
                .add(RESPONSE_DATA, Json.createObjectBuilder()
                        .add(RESPONSE_RESULT, Json.createObjectBuilder()
                                .add("chronoUnitData", Json.createObjectBuilder()
                                        .add("weeks", 0)
                                        .add("days", 1)
                                        .add("hours", 24)
                                        .add("minutes", 1440)
                                        .add("seconds", 86400)
                                        .build())
                                .add("periodData", Json.createObjectBuilder()
                                        .add("days", 1)
                                        .add("hours", 0)
                                        .add("minutes", 0)
                                        .add("seconds", 0)
                                        .build())
                                .build())
                        .build())
                .build()
                .toString();
        Assert.assertEquals(expectedResponse, response);
    }

    @Test
    public void shouldReturnCalculatedDaysFromTwoDatesWithTime() {
        String response = target("/count/2017-08-31T10:05:00/2018-09-02T01:03:30")
                .request()
                .get(String.class);
        String expectedResponse = Json.createObjectBuilder()
                .add(RESPONSE_STATUS, RESPONSE_STATUS_SUCCESS)
                .add(RESPONSE_DATA, Json.createObjectBuilder()
                        .add(RESPONSE_RESULT, Json.createObjectBuilder()
                                .add("chronoUnitData", Json.createObjectBuilder()
                                        .add("weeks", 52)
                                        .add("days", 366)
                                        .add("hours", 8798)
                                        .add("minutes", 527938)
                                        .add("seconds", 31676310)
                                        .build())
                                .add("periodData", Json.createObjectBuilder()
                                        .add("days", 366)
                                        .add("hours", 14)
                                        .add("minutes", 58)
                                        .add("seconds", 30)
                                        .build())
                                .build())
                        .build())
                .build()
                .toString();
        Assert.assertEquals(expectedResponse, response);
    }

    @Test
    public void shouldReturnCalculatedDaysFromTwoDatesWithTimeAndTimezone() {
        String response = target("/count/2017-08-31T00:00:00/2017-09-01T01:00:00")
                .queryParam("fromTz", "Asia/Kuala_Lumpur")
                .queryParam("toTz", "Australia/Melbourne")
                .request()
                .get(String.class);
        String expectedResponse = Json.createObjectBuilder()
                .add(RESPONSE_STATUS, RESPONSE_STATUS_SUCCESS)
                .add(RESPONSE_DATA, Json.createObjectBuilder()
                        .add(RESPONSE_RESULT, Json.createObjectBuilder()
                                .add("chronoUnitData", Json.createObjectBuilder()
                                        .add("weeks", 0)
                                        .add("days", 0)
                                        .add("hours", 23)
                                        .add("minutes", 1380)
                                        .add("seconds", 82800)
                                        .build())
                                .add("periodData", Json.createObjectBuilder()
                                        .add("days", 0)
                                        .add("hours", 23)
                                        .add("minutes", 0)
                                        .add("seconds", 0)
                                        .build())
                                .build())
                        .build())
                .build()
                .toString();
        Assert.assertEquals(expectedResponse, response);
    }

    @Test(expected = BadRequestException.class)
    public void shouldThrowExceptionWhenCalculatingDaysFromTwoDatesWithTimeAndBlankFromTimezone() {
        target("/count/2017-08-31T00:00:00/2017-09-01T01:00:00")
                .queryParam("toTz", "Asia/Kuala_Lumpur")
                .request()
                .get(String.class);
    }

    @Test(expected = BadRequestException.class)
    public void shouldThrowExceptionWhenCalculatingDaysFromTwoDatesWithTimeAndBlankToTimezone() {
        target("/count/2017-08-31T00:00:00/2017-09-01T01:00:00")
                .queryParam("fromTz", "Asia/Kuala_Lumpur")
                .request()
                .get(String.class);
    }

    @Test(expected = InternalServerErrorException.class)
    public void shouldThrowExceptionWhenCalculatingDaysFromTwoDatesWithTimeAndInvalidTimezone() {
        target("/count/2017-08-31T00:00:00/2017-09-01T01:00:00")
                .queryParam("fromTz", "Asia/Kuala_Lumpur")
                .queryParam("toTz", "abc")
                .request()
                .get(String.class);
    }

    @Test(expected = BadRequestException.class)
    public void shouldThrowBadRequestExceptionForInvalidDateValueWhenCalculatingDaysFromTwoDates() {
        target("/count/2018-089-00T00:00:00/2018-08-10T00:00:00")
                .request()
                .get(String.class);
    }

    @Test(expected = NotFoundException.class)
    public void shouldThrowBadRequestExceptionForBlankDateValueWhenCalculatingDaysFromTwoDates() {
        target("/count/2018-08-10T00:00:00")
                .request()
                .get(String.class);
    }

    @Test(expected = NotFoundException.class)
    public void shouldThrowBadRequestExceptionForNoQueryParamWhenCalculatingDaysFromTwoDates() {
        target("/count/2018-08-08T00:00:00")
                .request()
                .get(String.class);
    }

    @Test
    public void shouldAddDaysToDatetime() {
        String response = target("/add/2018-09-16T00:00:00")
                .queryParam("addDays", 1)
                .request()
                .get(String.class);
        String expectedResponse = Json.createObjectBuilder()
                .add(RESPONSE_STATUS, RESPONSE_STATUS_SUCCESS)
                .add(RESPONSE_DATA, Json.createObjectBuilder()
                        .add(RESPONSE_RESULT, "2018-09-17 00:00:00")
                        .build())
                .build()
                .toString();
        Assert.assertEquals(expectedResponse, response);
    }

    @Test
    public void shouldAddYearsMonthsDaysHoursMinutesSecondsToDatetime() {
        String response = target("/add/2018-09-16T00:00:00")
                .queryParam("addYears", 1)
                .queryParam("addMonths", 2)
                .queryParam("addDays", 3)
                .queryParam("addHours", 4)
                .queryParam("addMinutes", 5)
                .queryParam("addSeconds", 6)
                .request()
                .get(String.class);
        String expectedResponse = Json.createObjectBuilder()
                .add(RESPONSE_STATUS, RESPONSE_STATUS_SUCCESS)
                .add(RESPONSE_DATA, Json.createObjectBuilder()
                        .add(RESPONSE_RESULT, "2019-11-19 04:05:06")
                        .build())
                .build()
                .toString();
        Assert.assertEquals(expectedResponse, response);
    }

    @Test
    public void shouldAddWeeksToDatetime() {
        String response = target("/add/2018-09-16T00:00:00")
                .queryParam("addWeeks", 1)
                .request()
                .get(String.class);
        String expectedResponse = Json.createObjectBuilder()
                .add(RESPONSE_STATUS, RESPONSE_STATUS_SUCCESS)
                .add(RESPONSE_DATA, Json.createObjectBuilder()
                        .add(RESPONSE_RESULT, "2018-09-23 00:00:00")
                        .build())
                .build()
                .toString();
        Assert.assertEquals(expectedResponse, response);
    }
}
