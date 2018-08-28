package com.example.datetime.jaxrs;

public class JsonError {
    public enum TYPE { ERROR }

    private String type;
    private String message;

    public JsonError(JsonErrorBuilder builder) {
        this.type = builder.type;
        this.message = builder.message;
    }

    public JsonError(TYPE type, String message) {
        JsonError jsonError = new JsonError.JsonErrorBuilder()
                .withType(type)
                .withMessage(message)
                .build();
        this.type = jsonError.type;
        this.message = jsonError.message;
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

        public JsonErrorBuilder withType(TYPE type) {
            this.type = type.toString();
            return this;
        }

        public JsonErrorBuilder withMessage(String message) {
            this.message = message;
            return this;
        }

        public JsonError build() {
            return new JsonError(this);
        }
    }
}
