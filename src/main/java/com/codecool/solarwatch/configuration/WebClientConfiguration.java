package com.codecool.solarwatch.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfiguration {

    @Bean
    public WebClient openWeatherWebClient() {
        return WebClient.builder()
                .baseUrl("https://api.openweathermap.org")
                .build();
    }

    @Bean
    public WebClient solarWebClient() {
        return WebClient.builder()
                .baseUrl("https://api.sunrise-sunset.org")
                .build();
    }
}