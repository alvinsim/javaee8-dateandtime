package com.example.datetime.jaxrs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

@Path("/datetime")
public class DatetimeService {

    private final Logger logger = LoggerFactory.getLogger(DatetimeService.class);
    private final static String ERROR_MESSAGE_PARSE_DATE = "Error: Problem parsing date [year: '%s'; month: '%s'; day: '%s'; hour: '%s'; minute: '%s'; second: '%s']";

    @GET
    @Path("/echo/{datetime}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response echo(final @PathParam("datetime") String datetime) {
        return appendMessageWithStatusOkToResponse(new DatetimeData.Builder().withResult(datetime).build());
    }

    @GET
    @Path("/count")
    @Produces(MediaType.APPLICATION_JSON)
    public Response calculateDays(final @DefaultValue("0") @QueryParam("d1") String fromDay,
                                  final @DefaultValue("0") @QueryParam("m1") String fromMonth,
                                  final @DefaultValue("0") @QueryParam("y1") String fromYear,
                                  final @DefaultValue("0") @QueryParam("h1") String fromHour,
                                  final @DefaultValue("0") @QueryParam("i1") String fromMinute,
                                  final @DefaultValue("0") @QueryParam("s1") String fromSecond,
                                  final @DefaultValue("0") @QueryParam("d2") String toDay,
                                  final @DefaultValue("0") @QueryParam("m2") String toMonth,
                                  final @DefaultValue("0") @QueryParam("y2") String toYear,
                                  final @DefaultValue("0") @QueryParam("h2") String toHour,
                                  final @DefaultValue("0") @QueryParam("i2") String toMinute,
                                  final @DefaultValue("0") @QueryParam("s2") String toSecond)
            throws DatetimeInputException {
        LocalDateTime fromDateTime;
        LocalDateTime toDateTime;

        try {
            fromDateTime = parseDateTime(fromYear, fromMonth, fromDay, fromHour, fromMinute, fromSecond);
        } catch (DateTimeException e) {
            logger.error(String.format(ERROR_MESSAGE_PARSE_DATE, fromYear, fromMonth, fromDay, fromHour, fromMinute, fromSecond), e);
            throw new DatetimeInputException(String.format(ERROR_MESSAGE_PARSE_DATE, fromYear, fromMonth, fromDay, fromHour, fromMinute, fromSecond));
        }
        try {
            toDateTime = parseDateTime(toYear, toMonth, toDay, toHour, toMinute, toSecond);
        } catch (DateTimeException e) {
            logger.error(String.format(ERROR_MESSAGE_PARSE_DATE, toYear, toMonth, toDay, toHour, toMinute, toSecond), e);
            throw new DatetimeInputException(String.format(ERROR_MESSAGE_PARSE_DATE, toYear, toMonth, toDay, toHour, toMinute, toSecond));
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

    private LocalDateTime parseDateTime(final String year, final String month, final String day, final String hour, final String minute, final String second)
            throws DateTimeException {
        LocalDate localDate = LocalDate.of(Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(day));
        LocalTime localTime = LocalTime.of(Integer.parseInt(hour), Integer.parseInt(minute), Integer.parseInt(second));

        return LocalDateTime.of(localDate, localTime);
    }

    private Response appendMessageWithStatusOkToResponse(final Object object) {
        return Response.status(Status.OK).entity(object).build();
    }

    private Response appendMessageWithErrorToResponse(final Object object) {
        return Response.status(Status.BAD_REQUEST).entity(object).build();
    }
}
