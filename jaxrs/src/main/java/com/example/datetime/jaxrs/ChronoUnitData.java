package com.example.datetime.jaxrs;

public class ChronoUnitData {

    private long weeks;
    private long days;
    private long hours;
    private long minutes;
    private long seconds;

    public ChronoUnitData(final Builder builder) {
        this.weeks = builder.weeks;
        this.days = builder.days;
        this.hours = builder.hours;
        this.minutes = builder.minutes;
        this.seconds = builder.seconds;
    }

    public long getWeeks() {
        return weeks;
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

    static class Builder {

        private long weeks;
        private long days;
        private long hours;
        private long minutes;
        private long seconds;

        public Builder() {}

        public Builder withWeeks(final long weeks) {
            this.weeks = weeks;
            return this;
        }

        public Builder withDays(final long days) {
            this.days = days;
            return this;
        }

        public Builder withHours(final long hours) {
            this.hours = hours;
            return this;
        }

        public Builder withMinutes(final long minutes) {
            this.minutes = minutes;
            return this;
        }

        public Builder withSeconds(final long seconds) {
            this.seconds = seconds;
            return this;
        }

        public ChronoUnitData build() {
            return new ChronoUnitData(this);
        }
    }
}
