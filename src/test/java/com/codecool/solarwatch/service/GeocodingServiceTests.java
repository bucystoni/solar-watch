package com.codecool.solarwatch.service;

import com.codecool.solarwatch.model.GeoData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.client.RestClient;

import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GeocodingServiceTests {

    @Mock
    private RestClient restClient;

    @InjectMocks
    private GeocodingService geocodingService;

    @Test
    void getGeoData_returnsData_whenCityFound() {
        RestClient.RequestHeadersUriSpec requestHeadersUriSpec = mock(RestClient.RequestHeadersUriSpec.class);
        when(restClient.get()).thenReturn(requestHeadersUriSpec);

        RestClient.RequestHeadersSpec requestHeadersSpec = mock(RestClient.RequestHeadersSpec.class);
        when(requestHeadersUriSpec.uri(any(Function.class))).thenReturn(requestHeadersSpec);

        RestClient.ResponseSpec responseSpec = mock(RestClient.ResponseSpec.class);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);

        List<GeoData> geoData = List.of(new GeoData("1013", "Budapest", 19, 23, "Hungary"));
        when(responseSpec.body(any(ParameterizedTypeReference.class))).thenReturn(geoData);

        GeoData result = geocodingService.getGeoData("Budapest");
        assertEquals(19, result.lat());
        assertEquals("Budapest", result.name());
    }

    @Test
    void getGeoData_throwsException_whenCityNotFound() {
        RestClient.RequestHeadersUriSpec requestHeadersUriSpec = mock(RestClient.RequestHeadersUriSpec.class);
        when(restClient.get()).thenReturn(requestHeadersUriSpec);

        RestClient.RequestHeadersSpec requestHeadersSpec = mock(RestClient.RequestHeadersSpec.class);
        when(requestHeadersUriSpec.uri(any(Function.class))).thenReturn(requestHeadersSpec);

        RestClient.ResponseSpec responseSpec = mock(RestClient.ResponseSpec.class);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);

        when(responseSpec.body(any(ParameterizedTypeReference.class))).thenReturn(List.of());

        assertThrows(RuntimeException.class, () -> geocodingService.getGeoData("Non-existent-city"));
    }

}
