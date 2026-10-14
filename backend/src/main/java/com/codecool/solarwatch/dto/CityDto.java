package com.codecool.solarwatch.dto;

import com.codecool.solarwatch.model.City;

public record CityDto(Long id, String name, double longitude,
                      double latitude, String state, String country, String timezone) {

    public static CityDto from(City city) {
        return new CityDto(city.getId(), city.getName(), city.getLongitude(),
                city.getLatitude(), city.getState(), city.getCountry(), city.getTimezone());
    }
}