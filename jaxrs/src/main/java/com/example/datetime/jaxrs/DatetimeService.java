package com.example.datetime.jaxrs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;

@Path("/datetime")
public class DatetimeService {

    // TODO add running service with jetty

    private final Logger logger = LoggerFactory.getLogger(DatetimeService.class);
    private final static String ERROR_MESSAGE_PARSE_DATE = "Error: Problem parsing date with value '%s'";

    @GET
    @Path("/echo/{datetime}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response echo(final @PathParam("datetime") String datetime) {
        return appendMessageWithStatusOkToResponse(new DatetimeData.Builder().withResult(datetime).build());
    }

    @GET
    @Path("/count/{fromDatetime}/{toDatetime}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response calculateDays(final @DefaultValue("") @PathParam("fromDatetime") String from,
                                  final @DefaultValue("") @PathParam("toDatetime") String to)
            throws DatetimeInputException {
        LocalDateTime fromDateTime;
        LocalDateTime toDateTime;

        try {
            fromDateTime = parseDateTime(from);
        } catch (DateTimeParseException e) {
            logger.error(String.format(ERROR_MESSAGE_PARSE_DATE, from), e);
            throw new DatetimeInputException(String.format(ERROR_MESSAGE_PARSE_DATE, from));
        }
        try {
            toDateTime = parseDateTime(to);
        } catch (DateTimeException e) {
            logger.error(String.format(ERROR_MESSAGE_PARSE_DATE, to), e);
            throw new DatetimeInputException(String.format(ERROR_MESSAGE_PARSE_DATE, to));
        }

        ChronoUnitData chronoUnitData = buildChronoUnitData(fromDateTime, toDateTime);
        PeriodData periodData = buildPeriodData(chronoUnitData);
        DatetimeCountData datetimeCountData = new DatetimeCountData.Builder()
                .withChronoUnitData(chronoUnitData)
                .withPeriodData(periodData)
                .build();
        return appendMessageWithStatusOkToResponse(datetimeCountData);
    }

    private ChronoUnitData buildChronoUnitData(final LocalDateTime fromDateTime, final LocalDateTime toDateTime) {
        long weeks = ChronoUnit.WEEKS.between(fromDateTime, toDateTime);
        long days = ChronoUnit.DAYS.between(fromDateTime, toDateTime);
        long hours = ChronoUnit.HOURS.between(fromDateTime, toDateTime);
        long minutes = ChronoUnit.MINUTES.between(fromDateTime, toDateTime);
        long seconds = ChronoUnit.SECONDS.between(fromDateTime, toDateTime);
        return new ChronoUnitData.Builder()
                .withWeeks(weeks)
                .withDays(days)
                .withHours(hours)
                .withMinutes(minutes)
                .withSeconds(seconds)
                .build();
    }

    private PeriodData buildPeriodData(final ChronoUnitData chronoUnitData) {
        long days = chronoUnitData.getDays();
        long hours = chronoUnitData.getHours() % 24;
        long minutes = chronoUnitData.getMinutes() % 60;
        long seconds = chronoUnitData.getSeconds() % 60;
        return new PeriodData.Builder()
                .withDays(days)
                .withHours(hours)
                .withMinutes(minutes)
                .withSeconds(seconds)
                .build();
    }

    private LocalDateTime parseDateTime(final String datetime) throws DateTimeParseException {
        return LocalDateTime.parse(datetime, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));
    }

    private Response appendMessageWithStatusOkToResponse(final Object object) {
        return Response.status(Status.OK).entity(object).build();
    }

    private Response appendMessageWithErrorToResponse(final Object object) {
        return Response.status(Status.BAD_REQUEST).entity(object).build();
    }
}
