package com.codecool.solarwatch.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfiguration {

    @Bean
    public RestClient openWeatherRestClient() {
        return RestClient.builder()
                .baseUrl("http://api.openweathermap.org")
                .build();
    }

    @Bean
    public RestClient solarRestClient() {
        return RestClient.builder()
                .baseUrl("https://api.sunrise-sunset.org")
                .build();
    }
}