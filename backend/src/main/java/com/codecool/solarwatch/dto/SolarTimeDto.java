package com.codecool.solarwatch.dto;

import com.codecool.solarwatch.model.SolarTime;

import java.time.LocalDate;
import java.time.OffsetDateTime;

public record SolarTimeDto(LocalDate date, OffsetDateTime sunrise, OffsetDateTime sunset, CityDto city) {

    public static SolarTimeDto from(SolarTime solarTime) {
        return new SolarTimeDto(solarTime.getDate(), solarTime.getSunrise(), solarTime.getSunset(), CityDto.from(solarTime.getCity()));
    }
}
