package com.example.datetime.jaxrs;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Assert;
import org.junit.Ignore;
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
        String response = target("/datetime/count")
                .queryParam("y1", "2018")
                .queryParam("m1", "8")
                .queryParam("d1", "23")
                .queryParam("y2", "2018")
                .queryParam("m2", "8")
                .queryParam("d2", "24")
                .request()
                .get(String.class);
        Assert.assertEquals(
                "{\"chronoUnitData\":{\"weeks\":0,\"days\":1,\"hours\":24,\"minutes\":1440,\"seconds\":86400}," +
                        "\"periodData\":{\"days\":1,\"hours\":0,\"minutes\":0,\"seconds\":0}}",
                response);
    }

    @Test
    public void shouldReturnCalculatedDaysFromTwoDatesWithTime() {
        String response = target("/datetime/count")
                .queryParam("y1", "2017")
                .queryParam("m1", "08")
                .queryParam("d1", "31")
                .queryParam("h1", "10")
                .queryParam("i1", "05")
                .queryParam("s1", "00")
                .queryParam("y2", "2018")
                .queryParam("m2", "09")
                .queryParam("d2", "02")
                .queryParam("h2", "01")
                .queryParam("i2", "03")
                .queryParam("s2", "30")
                .request()
                .get(String.class);
        Assert.assertEquals(
                "{\"chronoUnitData\":{\"weeks\":52,\"days\":366,\"hours\":8798,\"minutes\":527938,\"seconds\":31676310}," +
                        "\"periodData\":{\"days\":366,\"hours\":14,\"minutes\":58,\"seconds\":30}}",
                response);
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
