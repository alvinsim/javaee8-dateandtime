package com.example.datetime.calc.jaxrs;

import com.example.datetime.calc.jaxrs.exceptions.DatetimeInputException;
import com.example.datetime.calc.jaxrs.models.ChronoUnitData;
import com.example.datetime.calc.jaxrs.models.DatetimeData;
import com.example.datetime.calc.jaxrs.models.PeriodData;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.net.StandardProtocolFamily;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;

@Path("/")
public class DatetimeCalcService {

    // TODO add running service with jetty

    private final Logger logger = LoggerFactory.getLogger(DatetimeCalcService.class);

    private final static String ERROR_MESSAGE_PARSE_DATE = "Problem parsing date with value '%s'";
    private final static String ERROR_MESSAGE_EITHER_TIMEZONE_IS_BLANK = "Invalid timezone input. Either one is blank [fromTz: '%s'; toTz: '%s']";
    private final static String RESPONSE_DATA = "data";
    private final static String RESPONSE_MESSAGE = "message";
    private final static String RESPONSE_RESULT = "result";
    private final static String RESPONSE_STATUS = "status";
    private final static String RESPONSE_STATUS_ERROR = "error";
    private final static String RESPONSE_STATUS_SUCCESS = "success";

    @GET
    @Path("/today")
    @Produces(MediaType.APPLICATION_JSON)
    public Response today() {
        LocalDateTime currentDateTime = LocalDateTime.now();
        JsonObject result = Json.createObjectBuilder()
                .add("date", currentDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                .add("dayOfWeek", String.valueOf(currentDateTime.getDayOfWeek()))
                .add("time", currentDateTime.format(DateTimeFormatter.ofPattern("HH:mm:ss")))
                .build();
        return appendMessageWithStatusOkToResponse(buildSuccessResponse(result));
    }

    // TODO API to count business days/weekdays between two dates

    @GET
    @Path("/count/{fromDatetime}/{toDatetime}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response calculateDatetimeDuration(final @DefaultValue("") @PathParam("fromDatetime") String from,
                                              final @DefaultValue("") @PathParam("toDatetime") String to,
                                              final @DefaultValue("") @QueryParam("fromTz") String fromTimezone,
                                              final @DefaultValue("") @QueryParam("toTz") String toTimezone)
            throws DatetimeInputException {
        LocalDateTime fromDateTime;
        LocalDateTime toDateTime;
        String errorMessage = "";

//        try {
            fromDateTime = parseDateTime(from);
//        } catch (DateTimeParseException e) {
//            errorMessage = String.format(ERROR_MESSAGE_PARSE_DATE, from);
//            logger.error(errorMessage, e);
//            throw new DatetimeInputException(buildErrorResponse(errorMessage));
//        }
//        try {
            toDateTime = parseDateTime(to);
//        } catch (DateTimeException e) {
//            errorMessage = String.format(ERROR_MESSAGE_PARSE_DATE, to);
//            logger.error(errorMessage, e);
//            throw new DatetimeInputException(buildErrorResponse(errorMessage));
//        }

        ChronoUnitData chronoUnitData;
        // consider timezone differences if 'fromTz' and 'toTz' is not blank
        if (StringUtils.isNoneBlank(fromTimezone) && StringUtils.isNoneBlank(toTimezone)) {
            String fromTz = decode(fromTimezone);
            String toTz = decode(toTimezone);
            ZonedDateTime fromZonedDateTime = fromDateTime.atZone(ZoneId.of(fromTz));
            ZonedDateTime toZonedDateTime = toDateTime.atZone(ZoneId.of(toTz));
            chronoUnitData = buildChronoUnitData(fromZonedDateTime, toZonedDateTime);
        }
        // invalid imput if either one of the pair is blank
        else if ((StringUtils.isNoneBlank(fromTimezone) && StringUtils.isBlank(toTimezone)) ||
                (StringUtils.isBlank(fromTimezone) && StringUtils.isNoneBlank(toTimezone))) {
            errorMessage = String.format(ERROR_MESSAGE_EITHER_TIMEZONE_IS_BLANK, fromTimezone, toTimezone);
            logger.error(errorMessage);
            throw new DatetimeInputException(buildErrorResponse(errorMessage));
        } else {
            chronoUnitData = buildChronoUnitData(fromDateTime, toDateTime);
        }
        PeriodData periodData = buildPeriodData(chronoUnitData);

        JsonObject result = Json.createObjectBuilder()
                .add("chronoUnitData", Json.createObjectBuilder()
                        .add("weeks", chronoUnitData.getWeeks())
                        .add("days", chronoUnitData.getDays())
                        .add("hours", chronoUnitData.getHours())
                        .add("minutes", chronoUnitData.getMinutes())
                        .add("seconds", chronoUnitData.getSeconds())
                        .build())
                .add("periodData", Json.createObjectBuilder()
                        .add("days", periodData.getDays())
                        .add("hours", periodData.getHours())
                        .add("minutes", periodData.getMinutes())
                        .add("seconds", periodData.getSeconds())
                        .build())
                .build();

        return appendMessageWithStatusOkToResponse(buildSuccessResponse(result));
    }

    // TODO API to add weekdays to a specified datetime

    @GET
    @Path("/add/{datetime}/")
    @Produces(MediaType.APPLICATION_JSON)
    public Response addDateTime(final @DefaultValue("") @PathParam("datetime") String datetime,
                                final @DefaultValue("0") @QueryParam("addYears") int addYears,
                                final @DefaultValue("0") @QueryParam("addMonths") int addMonths,
                                final @DefaultValue("0") @QueryParam("addWeeks") int addWeeks,
                                final @DefaultValue("0") @QueryParam("addDays") int addDays,
                                final @DefaultValue("0") @QueryParam("addHours") int addHours,
                                final @DefaultValue("0") @QueryParam("addMinutes") int addMinutes,
                                final @DefaultValue("0") @QueryParam("addSeconds") int addSeconds) {
        LocalDateTime inputDatetime;

        try {
            inputDatetime = parseDateTime(datetime);
        } catch (DateTimeParseException e) {
            String errorMessage = String.format(ERROR_MESSAGE_PARSE_DATE, datetime);
            logger.error(errorMessage, e);
            throw new DatetimeInputException(buildErrorResponse(errorMessage));
        }

        String result = inputDatetime
                        .plusYears(addYears)
                        .plusMonths(addMonths)
                        .plusWeeks(addWeeks)
                        .plusDays(addDays)
                        .plusHours(addHours)
                        .plusMinutes(addMinutes)
                        .plusSeconds(addSeconds)
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        return appendMessageWithStatusOkToResponse(buildSuccessResponse(result));
    }

    // TODO API to find weekday from a specified date

    private ChronoUnitData buildChronoUnitData(final Temporal fromDateTime, final Temporal toDateTime) {
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
        try {
            return LocalDateTime.parse(datetime, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));
        } catch (Exception e) {
            String errorMessage;
            errorMessage = String.format(ERROR_MESSAGE_PARSE_DATE, datetime);
            logger.error(errorMessage, e);
            throw new DatetimeInputException(buildErrorResponse(errorMessage));
        }
    }

    private Response appendMessageWithStatusOkToResponse(final Object object) {
        return Response.status(Status.OK).entity(object).build();
    }

    private Response appendMessageWithErrorToResponse(final Object object) {
        return Response.status(Status.BAD_REQUEST).entity(object).build();
    }

    private String buildErrorResponse(String errorMessage) {
        return Json.createObjectBuilder()
                .add(RESPONSE_STATUS, RESPONSE_STATUS_ERROR)
                .add(RESPONSE_MESSAGE, errorMessage)
                .build()
                .toString();
    }

    private String buildSuccessResponse(JsonObject result) {
        return Json.createObjectBuilder()
                .add(RESPONSE_STATUS, RESPONSE_STATUS_SUCCESS)
                .add(RESPONSE_DATA, Json.createObjectBuilder()
                        .add(RESPONSE_RESULT, result)
                        .build())
                .build()
                .toString();
    }

    private String buildSuccessResponse(String result) {
        return Json.createObjectBuilder()
                .add(RESPONSE_STATUS, RESPONSE_STATUS_SUCCESS)
                .add(RESPONSE_DATA, Json.createObjectBuilder()
                        .add(RESPONSE_RESULT, result)
                        .build())
                .build()
                .toString();
    }

    private String decode(String encoded) {
        if (StringUtils.isNoneBlank(encoded)) {
            try {
                return URLDecoder.decode(encoded, StandardCharsets.UTF_8.toString());
            } catch (Exception e) {
                throw new DatetimeInputException(e.getMessage());
            }
        }
        return encoded;
    }
}
