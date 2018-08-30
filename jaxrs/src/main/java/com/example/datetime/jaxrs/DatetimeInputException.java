package com.example.datetime.jaxrs;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.Serializable;

public class DatetimeInputException extends WebApplicationException implements Serializable {

    public DatetimeInputException(String message) {
        super(Response.status(Response.Status.BAD_REQUEST).entity(message).type(MediaType.APPLICATION_JSON).build());
    }
}
