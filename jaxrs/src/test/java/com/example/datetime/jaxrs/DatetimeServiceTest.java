package com.example.datetime.jaxrs;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.core.Application;

//TODO change test accordingly

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
        String response = target("/datetime/count")
                .queryParam("y1", "2018")
                .queryParam("m1", "8")
                .queryParam("d1", "23")
                .queryParam("y2", "2018")
                .queryParam("m2", "8")
                .queryParam("d2", "24")
                .request()
                .get(String.class);
        Assert.assertEquals(response, "{\"years\":0.0,\"months\":0.0,\"days\":1.0,\"hours\":24.0,\"minutes\":1440.0,\"seconds\":86400.0}");
    }

    @Test(expected = BadRequestException.class)
    public void shouldThrowBadRequestExceptionForInvalidDateValueWhenCalculatingDaysFromTwoDates() {
        target("datetime/count")
                .queryParam("y1", "2018")
                .queryParam("m1", "089")
                .queryParam("y2", "2018")
                .queryParam("m2", "08")
                .queryParam("d2", "10")
                .request()
                .get(String.class);
    }

    @Test(expected = BadRequestException.class)
    public void shouldThrowBadRequestExceptionForBlankDateValueWhenCalculatingDaysFromTwoDates() {
        target("datetime/count")
                .queryParam("y1", "2018")
                .queryParam("m1", "08")
                .queryParam("d1", "10")
                .request()
                .get(String.class);
    }

    @Test(expected = BadRequestException.class)
    public void shouldThrowBadRequestExceptionForNoQueryParamWhenCalculatingDaysFromTwoDates() {
        target("datetime/count")
                .queryParam("y1", "2018")
                .queryParam("m1", "08")
                .queryParam("d1", "08")
                .request()
                .get(String.class);
    }
}
