package com.codecool.solarwatch.model;

import java.time.OffsetDateTime;

public record SolarData(OffsetDateTime sunrise, OffsetDateTime sunset) {

    @Override
    public String toString() {
        return sunrise.toString() + sunset.toString();
    }
}
