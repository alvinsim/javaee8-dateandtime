package com.example.datetime.jaxrs;

public class JsonError {
    public enum TYPE { ERROR }

    private String type;
    private String message;

    public JsonError(JsonErrorBuilder builder) {
        this.type = builder.type;
        this.message = builder.message;
    }

    public String getType() {
        return this.type;
    }

    public String getMessage() {
        return this.message;
    }

    public static class JsonErrorBuilder {
        private String type;
        private String message;

        public JsonErrorBuilder() {}

        public JsonErrorBuilder withType(final TYPE type) {
            this.type = type.toString();
            return this;
        }

        public JsonErrorBuilder withMessage(final String message) {
            this.message = message;
            return this;
        }

        public JsonError build() {
            return new JsonError(this);
        }
    }
}
