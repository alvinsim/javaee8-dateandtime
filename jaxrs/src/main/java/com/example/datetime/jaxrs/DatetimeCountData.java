package com.example.datetime.jaxrs;

//TODO add new entity 'DateTimeCount'
//TODO add new entity 'DateTimeAdd'

public class DatetimeCountData {

    private ChronoUnitData chronoUnitData;
    private PeriodData periodData;

    private DatetimeCountData() {}

    public DatetimeCountData(final Builder builder) {
        this.chronoUnitData = builder.chronoUnitData;
        this.periodData = builder.periodData;
    }

    public ChronoUnitData getChronoUnitData() {
        return chronoUnitData;
    }

    public PeriodData getPeriodData() {
        return periodData;
    }

    static class Builder {

        private ChronoUnitData chronoUnitData;
        private PeriodData periodData;

        public Builder() {}

        public Builder withChronoUnitData(final ChronoUnitData chronoUnitData) {
            this.chronoUnitData = chronoUnitData;
            return this;
        }

        public Builder withPeriodData(final PeriodData periodData) {
            this.periodData = periodData;
            return this;
        }

        public DatetimeCountData build() {
            return new DatetimeCountData(this);
        }
    }
}
