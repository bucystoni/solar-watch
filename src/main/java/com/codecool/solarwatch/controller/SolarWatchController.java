package com.codecool.solarwatch.controller;

import com.codecool.solarwatch.model.GeoData;
import com.codecool.solarwatch.model.SolarData;
import com.codecool.solarwatch.service.GeocodingService;
import com.codecool.solarwatch.service.SolarService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
public class SolarWatchController {
    private final SolarService solarService;
    private final GeocodingService geocodingService;


    public SolarWatchController(SolarService solarService, GeocodingService geocodingService) {
        this.solarService = solarService;
        this.geocodingService = geocodingService;
    }


    @GetMapping("/api/solardata")
    public ResponseEntity<?> getSolarData (@RequestParam(required = false) LocalDate date,
                                           @RequestParam String city) {

        GeoData geoData = geocodingService.getGeoData(city);
        double latitude = geoData.lat();
        double longitude = geoData.lon();
        SolarData solarData = solarService.getSolarData(latitude, longitude);

        return ResponseEntity.ok(solarData);
    }

}
