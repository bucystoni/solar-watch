package com.codecool.solarwatch.controller;

import com.codecool.solarwatch.model.GeoData;
import com.codecool.solarwatch.model.SolarData;
import com.codecool.solarwatch.model.SolarTime;
import com.codecool.solarwatch.service.GeocodingService;
import com.codecool.solarwatch.service.SolarService;
import com.codecool.solarwatch.service.SolarWatchService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
public class SolarWatchController {
    private final SolarWatchService solarWatchService;


    public SolarWatchController(SolarWatchService solarWatchService) {
        this.solarWatchService = solarWatchService;
    }


    @GetMapping("/api/solardata")
    public ResponseEntity<?> getSolarData (@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                                           @RequestParam String city) {

        SolarTime solarTime = solarWatchService.getSolarData(city, date);

        return ResponseEntity.ok(solarTime);
    }
}
