package com.example.datetime.jaxrs;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Assert;
import org.junit.Test;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.core.Application;

public class DatetimeServiceTest extends JerseyTest {

    @Override
    protected Application configure() {
        return new ResourceConfig(DatetimeService.class);
    }

    @Test
    public void shouldReturnSameParameterValue() {
        String response = target("/datetime/echo/2018-08-23").request().get(String.class);
        Assert.assertEquals("{\"result\":\"2018-08-23\"}", response);
    }

    @Test
    public void shouldReturnCalculatedDaysFromTwoDates() {
        String response = target("/datetime/countDays")
                .queryParam("from", "2018-08-23")
                .queryParam("to", "2018-08-24")
                .request()
                .get(String.class);
        Assert.assertEquals(response, "{\"result\":\"1\"}");
    }

    @Test(expected = BadRequestException.class)
    public void shouldThrowBadRequestExceptionForInvalidDateValueWhenCalculatingDaysFromTwoDates() {
        target("datetime/countDays")
                .queryParam("from", "2018-089")
                .queryParam("to", "2018-08-10")
                .request()
                .get(String.class);
    }

    @Test(expected = BadRequestException.class)
    public void shouldThrowBadRequestExceptionForBlankDateValueWhenCalculatingDaysFromTwoDates() {
        target("datetime/countDays")
                .queryParam("from", "2018-08-10")
                .queryParam("to", "")
                .request()
                .get(String.class);
    }

    @Test(expected = BadRequestException.class)
    public void shouldThrowBadRequestExceptionForNoQueryParamWhenCalculatingDaysFromTwoDates() {
        target("datetime/countDays")
                .queryParam("to", "2018-08-08")
                .request()
                .get(String.class);
    }
}
