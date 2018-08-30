package com.example.datetime.jaxrs;

public class DatetimeData {
    private String result;

    public DatetimeData(DatetimeDataBuilder builder) {
        this.result = builder.result;
    }

    private DatetimeData(String result) {
        DatetimeData datetimeData = new DatetimeData.DatetimeDataBuilder()
                .withResult(result)
                .build();
        this.result = datetimeData.getResult();
    }

    public String getResult() {
        return result;
    }

    public static class DatetimeDataBuilder {
        private String result;

        public DatetimeDataBuilder() {}

        public DatetimeDataBuilder withResult(String result) {
            this.result = result;
            return this;
        }

        public DatetimeData build() {
            return new DatetimeData(this);
        }
    }
}
