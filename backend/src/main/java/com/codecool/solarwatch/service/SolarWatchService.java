package com.codecool.solarwatch.service;

import com.codecool.solarwatch.dto.SolarTimeDto;
import com.codecool.solarwatch.model.City;
import com.codecool.solarwatch.model.GeoData;
import com.codecool.solarwatch.model.SolarData;
import com.codecool.solarwatch.model.SolarTime;
import com.codecool.solarwatch.repository.CityRepository;
import com.codecool.solarwatch.repository.SolarTimeRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Locale;

@Service
public class SolarWatchService {
    private final CityRepository cityRepository;
    private final SolarTimeRepository solarTimeRepository;
    private final GeocodingService geocodingService;
    private final SolarService solarService;
    private final Clock clock;

    public SolarWatchService(CityRepository cityRepository, SolarTimeRepository solarTimeRepository, GeocodingService geocodingService, SolarService solarService, Clock clock) {
        this.cityRepository = cityRepository;
        this.solarTimeRepository = solarTimeRepository;
        this.geocodingService = geocodingService;
        this.solarService = solarService;
        this.clock = clock;
    }

    @Transactional
    public SolarTimeDto getSolarData(String cityName, LocalDate date) {
        City city = getOrCreateCity(cityName);
        if (date == null) {
            ZoneId zone = city.getTimezone() != null ? ZoneId.of(city.getTimezone()) : ZoneOffset.UTC;
            date = LocalDate.now(clock.withZone(zone));
        }

        return SolarTimeDto.from(getOrCreateSolarTime(city, date));
    }

    private City getOrCreateCity(String name) {
        String normalizedName = name.trim().toLowerCase(Locale.ROOT);
        return cityRepository.findByNameIgnoreCase(normalizedName)
                .orElseGet(() -> fetchAndStoreCity(normalizedName));
    }

    private SolarTime getOrCreateSolarTime(City city, LocalDate date) {
        return solarTimeRepository.findByCityAndDate(city, date)
                .orElseGet(() -> {
                    SolarData data = solarService.getSolarData(city.getLatitude(), city.getLongitude(), date);
                    city.setTimezone(data.timezone());
                    return solarTimeRepository.save(new SolarTime(date, data.sunrise(), data.sunset(), city));
                });
    }

    private City fetchAndStoreCity(String query) {
        GeoData geoData = geocodingService.getGeoData(query);
        return cityRepository.findByNameIgnoreCase(geoData.name())
                .orElseGet(() -> cityRepository.save(
                        new City(geoData.name(), geoData.lon(), geoData.lat(),
                                geoData.state(), geoData.country())));
    }
}
