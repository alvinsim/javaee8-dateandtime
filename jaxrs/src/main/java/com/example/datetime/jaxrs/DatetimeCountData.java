package com.example.datetime.jaxrs;

//TODO add new entity 'DateTimeCount'
//TODO add new entity 'DateTimeAdd'

public class DatetimeCountData {

    private long years;
    private long months;
    private long days;
    private long hours;
    private long minutes;
    private long seconds;

    private DatetimeCountData() {}

    private DatetimeCountData(final DatetimeCountDataBuilder builder) {
        this.years = builder.years;
        this.months = builder.months;
        this.days = builder.days;
        this.hours = builder.hours;
        this.minutes = builder.minutes;
        this.seconds = builder.seconds;
    }

    public double getYears() {
        return years;
    }

    public double getMonths() {
        return months;
    }

    public double getDays() {
        return days;
    }

    public double getHours() {
        return hours;
    }

    public double getMinutes() {
        return minutes;
    }

    public double getSeconds() {
        return seconds;
    }

    public static class DatetimeCountDataBuilder {
        private long years;
        private long months;
        private long days;
        private long hours;
        private long minutes;
        private long seconds;

        public DatetimeCountDataBuilder() {}

        public DatetimeCountDataBuilder withYears(final long year) {
            this.years = year;
            return this;
        }

        public DatetimeCountDataBuilder withMonths(final long month) {
            this.months = month;
            return this;
        }

        public DatetimeCountDataBuilder withDays(final long day) {
            this.days = day;
            return this;
        }

        public DatetimeCountDataBuilder withHours(final long hour) {
            this.hours = hour;
            return this;
        }

        public DatetimeCountDataBuilder withMinutes(final long minute) {
            this.minutes = minute;
            return this;
        }

        public DatetimeCountDataBuilder withSeconds(final long second) {
            this.seconds = second;
            return this;
        }

        public DatetimeCountData build() {
            return new DatetimeCountData(this);
        }
    }
}
