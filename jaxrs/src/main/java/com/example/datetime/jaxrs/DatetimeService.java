package com.example.datetime.jaxrs;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/datetime")
public class DatetimeService {

    private final Logger logger = LoggerFactory.getLogger(DatetimeService.class);
    final static String ERROR_MESSAGE_PARSE_DATE = "Error: Problem parsing date %s";

    @GET
    @Path("/echo/{datetime}")
    public Response echo(@PathParam("datetime") String datetime) {
        return Response
                .status(Response.Status.OK)
                .entity(String.format("datetime: %s", datetime))
                .build();
    }

    @GET
    @Path("/countDays")
    public Response calculateDays(@QueryParam("from") String from,
                                  @QueryParam("to") String to) {
        LocalDate fromDate = null;
        LocalDate toDate = null;

        try {
            fromDate = parseDate(from);
        } catch (DateTimeException e) {
            logger.error(String.format(ERROR_MESSAGE_PARSE_DATE, from), e);
            return appendMessageToResponseWIthStatusOK(String.format(ERROR_MESSAGE_PARSE_DATE, from));
        }
        try {
            toDate = parseDate(to);
        } catch (DateTimeException e) {
            logger.error(String.format(ERROR_MESSAGE_PARSE_DATE, to), e);
            return appendMessageToResponseWIthStatusOK(String.format(ERROR_MESSAGE_PARSE_DATE, to));
        }

        long days = ChronoUnit.DAYS.between(fromDate, toDate);
        return Response
                .status(Response.Status.OK)
                .entity(String.format("Result: %d", days))
                .build();
    }

    private LocalDate parseDate(String date) throws DateTimeException {
        if (StringUtils.isBlank(date)) {
            throw new DateTimeException("Cannot parse a blank date.");
        }
        return LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    private Response appendMessageToResponseWIthStatusOK(String message) {
        return Response.status(Status.OK).entity(message).build();
    }

    private Response appendMessageWithStatusToResponse(Status status, String message) {
        return Response.status(status).entity(message).build();
    }
}
