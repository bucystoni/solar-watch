package com.codecool.solarwatch.service;

import com.codecool.solarwatch.model.City;
import com.codecool.solarwatch.model.GeoData;
import com.codecool.solarwatch.model.SolarData;
import com.codecool.solarwatch.model.SolarTime;
import com.codecool.solarwatch.repository.CityRepository;
import com.codecool.solarwatch.repository.SolarTimeRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class SolarWatchService {
    private final CityRepository cityRepository;
    private final SolarTimeRepository solarTimeRepository;
    private final GeocodingService geocodingService;
    private final SolarService solarService;

    public SolarWatchService(CityRepository cityRepository, SolarTimeRepository solarTimeRepository, GeocodingService geocodingService, SolarService solarService) {
        this.cityRepository = cityRepository;
        this.solarTimeRepository = solarTimeRepository;
        this.geocodingService = geocodingService;
        this.solarService = solarService;
    }

    @Transactional
    public SolarTime getSolarData(String cityName, LocalDate date) {
        if (date == null) date = LocalDate.now();

        City city = getOrCreateCity(cityName);
        return getOrCreateSolarTime(city, date);

    }

    private City getOrCreateCity(String name) {
        return cityRepository.findByName(name)
                .orElseGet(() -> {
                    GeoData geoData = geocodingService.getGeoData(name);
                    return cityRepository.save(new City(geoData.name(), geoData.lon(), geoData.lat(), geoData.zip(), geoData.country()));
                });
    }

    private SolarTime getOrCreateSolarTime(City city, LocalDate date) {
        return solarTimeRepository.findByCityAndDate(city, date)
                .orElseGet(() -> {
                    SolarData data = solarService.getSolarData(city.getLatitude(), city.getLongitude());
                    return solarTimeRepository.save(new SolarTime(date, data.sunrise(), data.sunset(), city));
                });
    }
}
