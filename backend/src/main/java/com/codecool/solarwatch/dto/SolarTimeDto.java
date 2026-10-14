package com.codecool.solarwatch.dto;

import com.codecool.solarwatch.model.SolarTime;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public record SolarTimeDto(LocalDate date, OffsetDateTime sunrise, OffsetDateTime sunset, CityDto city) {

    public static SolarTimeDto from(SolarTime solarTime) {
        String timezone = solarTime.getCity().getTimezone();
        return new SolarTimeDto(solarTime.getDate(), convertTimeToTimezone(solarTime.getSunrise(), timezone), convertTimeToTimezone(solarTime.getSunset(), timezone), CityDto.from(solarTime.getCity()));
    }

    private static OffsetDateTime convertTimeToTimezone(OffsetDateTime time, String timezone) {
        if (timezone == null) return time;
        ZoneId zone = ZoneId.of(timezone);
        return time.atZoneSameInstant(zone).toOffsetDateTime();
    }
}
