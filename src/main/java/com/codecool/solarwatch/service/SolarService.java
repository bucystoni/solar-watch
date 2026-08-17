package com.codecool.solarwatch.service;

import com.codecool.solarwatch.model.SolarData;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class SolarService {

    private final RestClient restClient;

    public SolarService(
            @Qualifier("solarRestClient") RestClient restClient
    ) {
        this.restClient = restClient;
    }

    public SolarData getSolarData(double latitude, double longitude) {
        return restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/v2")
                        .queryParam("lat", latitude)
                        .queryParam("lng", longitude)
                        .build())
                .retrieve()
                .body(SolarData.class);
    }
}