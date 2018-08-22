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
    public void echoDatetimeTest() {
        String response = target("datetime/2018-08-23").request().get(String.class);
        Assert.assertTrue("datetime: 2018-08-23".equals(response));
    }

}
