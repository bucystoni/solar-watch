package com.codecool.solarwatch.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.OffsetDateTime;

public record SolarData(OffsetDateTime sunrise, OffsetDateTime sunset, @JsonProperty("tzid") String timezone) {
}
