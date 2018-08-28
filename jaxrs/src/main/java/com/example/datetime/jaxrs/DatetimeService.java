package com.example.datetime.jaxrs;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

@Path("/datetime")
public class DatetimeService {

    private final Logger logger = LoggerFactory.getLogger(DatetimeService.class);
    private final static String ERROR_MESSAGE_PARSE_DATE = "Error: Problem parsing date '%s'";

    @GET
    @Path("/echo/{datetime}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response echo(@PathParam("datetime") String datetime) {
        return appendMessageWithStatusOkToResponse(new DatetimeData(datetime));
    }

    @GET
    @Path("/countDays")
    @Produces(MediaType.APPLICATION_JSON)
    public Response calculateDays(@DefaultValue("") @QueryParam("from") String from,
                                  @DefaultValue("") @QueryParam("to") String to) {
        LocalDate fromDate;
        LocalDate toDate;

        try {
            fromDate = parseDate(from);
        } catch (DateTimeException e) {
            logger.error(String.format(ERROR_MESSAGE_PARSE_DATE, from), e);
            return appendMessageWithErrorToResponse(new JsonError(JsonError.TYPE.ERROR, String.format(ERROR_MESSAGE_PARSE_DATE, from)));
        }
        try {
            toDate = parseDate(to);
        } catch (DateTimeException e) {
            logger.error(String.format(ERROR_MESSAGE_PARSE_DATE, to), e);
            return appendMessageWithErrorToResponse(new JsonError(JsonError.TYPE.ERROR, String.format(ERROR_MESSAGE_PARSE_DATE, to)));
        }

        long days = ChronoUnit.DAYS.between(fromDate, toDate);
        DatetimeData datetimeData = new DatetimeData(String.valueOf(days));
        return appendMessageWithStatusOkToResponse(datetimeData);
    }

    private LocalDate parseDate(String date) throws DateTimeException {
        if (StringUtils.isBlank(date)) {
            throw new DateTimeException("Cannot parse a blank date.");
        }
        return LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    private Response appendMessageWithStatusOkToResponse(Object object) {
        return Response.status(Status.OK).entity(object).build();
    }

    private Response appendMessageWithErrorToResponse(Object object) {
        return Response.status(Status.BAD_REQUEST).entity(object).build();
    }
}
