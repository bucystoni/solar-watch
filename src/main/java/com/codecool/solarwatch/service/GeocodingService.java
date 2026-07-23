package com.codecool.solarwatch.service;

import com.codecool.solarwatch.model.GeoData;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

@Service
public class GeocodingService {
    private final RestClient restClient;

    @Value("${openweather.api-key}")
    private String apiKey;

    public GeocodingService(
            @Qualifier("openWeatherRestClient") RestClient restClient
    ) {
        this.restClient = restClient;
    }

    public GeoData getGeoData(String city) {
        List<GeoData> geoDataList = restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/geo/1.0/direct")
                        .queryParam("q", city)
                        .queryParam("limit", 1)
                        .queryParam("appid", apiKey)
                        .build())
                .retrieve()
                .body(new ParameterizedTypeReference<List<GeoData>>() {});

        if (geoDataList.isEmpty()) {
            throw new RuntimeException("City not found: " + city);
        }

        return geoDataList.getFirst();
    }
}
