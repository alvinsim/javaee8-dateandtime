package com.example.datetime.jaxrs;

import javax.ws.rs.core.Application;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Assert;
import org.junit.Test;

public class DatetimeServiceTest extends JerseyTest {

    @Override
    protected Application configure() {
        return new ResourceConfig(DatetimeService.class);
    }

    @Test
    public void shouldReturnSameParameterValue() {
        String response = target("/datetime/echo/2018-08-23").request().get(String.class);
        Assert.assertEquals("datetime: 2018-08-23", response);
    }

    @Test
    public void shouldReturnCalculatedDaysFromTwoDates() {
        String response = target("/datetime/countDays")
                .queryParam("from", "2018-08-23")
                .queryParam("to", "2018-08-24")
                .request()
                .get(String.class);
        Assert.assertEquals(response, "Result: " + 1);
    }

    @Test
    public void shouldReturnWithErrorMessageForInvalidDateWhenCalculatingDaysFromTwoDates() {
        String errorFromDate = "2018-089";
        String responseWithErrorFromDate = target("datetime/countDays")
                .queryParam("from", errorFromDate)
                .queryParam("to", "2018-08-10")
                .request()
                .get(String.class);
        Assert.assertEquals(responseWithErrorFromDate, String.format(DatetimeService.ERROR_MESSAGE_PARSE_DATE, errorFromDate));

        String errorToDate = "abc";
        String responseWithErrorToDate = target("datetime/countDays")
                .queryParam("from", "2018-08-10")
                .queryParam("to", errorToDate)
                .request()
                .get(String.class);
        Assert.assertEquals(responseWithErrorToDate, String.format(DatetimeService.ERROR_MESSAGE_PARSE_DATE, errorToDate));

        String responseWithBlankFromDate = target("datetime/countDays")
                .queryParam("from", "")
                .queryParam("to", "2018-08-08")
                .request()
                .get(String.class);
        Assert.assertEquals(responseWithBlankFromDate, String.format(DatetimeService.ERROR_MESSAGE_PARSE_DATE, ""));

        String responseWithNoFromDate = target("datetime/countDays")
                .queryParam("to", "2018-08-08")
                .request()
                .get(String.class);
        Assert.assertEquals(responseWithNoFromDate, String.format(DatetimeService.ERROR_MESSAGE_PARSE_DATE, ""));

        String responseWithNoDates = target("datetime/countDays").request().get(String.class);
        Assert.assertEquals(responseWithNoDates, String.format(DatetimeService.ERROR_MESSAGE_PARSE_DATE, ""));
    }
}
