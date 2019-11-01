package com.example.datetime.calc.jaxrs.models;

public class PeriodData {

    private long days;
    private long hours;
    private long minutes;
    private long seconds;

    private PeriodData(Builder builder) {
        this.days = builder.days;
        this.hours = builder.hours;
        this.minutes = builder.minutes;
        this.seconds = builder.seconds;
    }

    public long getDays() {
        return days;
    }

    public long getHours() {
        return hours;
    }

    public long getMinutes() {
        return minutes;
    }

    public long getSeconds() {
        return seconds;
    }

    public static class Builder {

        private long days;
        private long hours;
        private long minutes;
        private long seconds;

        public Builder() {}

        public Builder withDays(final long day) {
            this.days = day;
            return this;
        }

        public Builder withHours(final long hour) {
            this.hours = hour;
            return this;
        }

        public Builder withMinutes(final long minute) {
            this.minutes = minute;
            return this;
        }

        public Builder withSeconds(final long second) {
            this.seconds = second;
            return this;
        }

        public PeriodData build() {
            return new PeriodData(this);
        }
    }
}
