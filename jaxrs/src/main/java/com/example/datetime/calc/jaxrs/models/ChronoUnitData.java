package com.example.datetime.calc.jaxrs.models;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ChronoUnitData {

    private final long weeks;
    private final long days;
    private final long hours;
    private final long minutes;
    private final long seconds;
}
