package com.codecool.solarwatch.service;

import com.codecool.solarwatch.exception.CityNotFoundException;
import com.codecool.solarwatch.model.GeoData;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;
import java.util.List;

@Service
public class GeocodingService {
    private final WebClient webClient;

    @Value("${openweather.api-key}")
    private String apiKey;

    public GeocodingService(
            @Qualifier("openWeatherWebClient") WebClient webClient
    ) {
        this.webClient = webClient;
    }

    public GeoData getGeoData(String city) {
        List<GeoData> geoDataList = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/geo/1.0/direct")
                        .queryParam("q", city)
                        .queryParam("limit", 1)
                        .queryParam("appid", apiKey)
                        .build())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<GeoData>>() {}).block(Duration.ofSeconds(5));

        if (geoDataList == null || geoDataList.isEmpty()) {
            throw new CityNotFoundException("City not found: " + city);
        }

        return geoDataList.getFirst();
    }
}
