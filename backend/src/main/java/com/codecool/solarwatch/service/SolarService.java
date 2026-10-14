package com.codecool.solarwatch.service;

import com.codecool.solarwatch.model.SolarData;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;
import java.time.LocalDate;

@Service
public class SolarService {

    private final WebClient webClient;

    public SolarService(
            @Qualifier("solarWebClient") WebClient webClient
    ) {
        this.webClient = webClient;
    }

    public SolarData getSolarData(double latitude, double longitude, LocalDate date) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/v2")
                        .queryParam("lat", latitude)
                        .queryParam("lng", longitude)
                        .queryParam("date", date)
                        .build())
                .retrieve()
                .bodyToMono(SolarData.class).block(Duration.ofSeconds(5));
    }
}