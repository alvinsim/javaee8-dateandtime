package com.example.datetime.jaxrs;

public class DatetimeData {
    private String result;

    public DatetimeData(Builder builder) {
        this.result = builder.result;
    }

    public String getResult() {
        return result;
    }

    public static class Builder {
        private String result;

        public Builder() {}

        public Builder withResult(String result) {
            this.result = result;
            return this;
        }

        public DatetimeData build() {
            return new DatetimeData(this);
        }
    }
}
