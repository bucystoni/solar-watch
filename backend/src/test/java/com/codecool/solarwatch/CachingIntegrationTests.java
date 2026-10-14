package com.codecool.solarwatch;

import com.codecool.solarwatch.model.GeoData;
import com.codecool.solarwatch.model.SolarData;
import com.codecool.solarwatch.repository.CityRepository;
import com.codecool.solarwatch.service.GeocodingService;
import com.codecool.solarwatch.service.SolarService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.convention.TestBean;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.*;

import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyString;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class CachingIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CityRepository cityRepository;

    @MockitoBean
    private GeocodingService geocodingService;

    @MockitoBean
    private SolarService solarService;

    @TestBean
    Clock clock;

    static Clock clock() {
        return Clock.fixed(Instant.parse("2026-10-14T20:00:00Z"), ZoneOffset.UTC);
    }

    @BeforeEach
    void setup() {
        when(geocodingService.getGeoData(anyString()))
                .thenReturn(new GeoData("Pest", "Budapest", 1, 2, "Hungary"));

        OffsetDateTime sunrise = OffsetDateTime.parse("2026-01-15T07:30:00+01:00");
        OffsetDateTime sunset = OffsetDateTime.parse("2026-01-15T21:30:00+01:00");
        when(solarService.getSolarData(anyDouble(), anyDouble(), any(LocalDate.class))).thenReturn(new SolarData(sunrise, sunset, "Europe/Budapest"));
    }

    @Test
    @WithMockUser(roles = "USER")
    void getSolarData_callsExternalApiOnce_whenRequestedTwice() throws Exception {
        performGetSolarDataRequest("Budapest");
        performGetSolarDataRequest("Budapest");


        verify(geocodingService, times(1)).getGeoData(anyString());

        verify(solarService, times(1)).getSolarData(anyDouble(), anyDouble(), any(LocalDate.class));

    }

    @Test
    @WithMockUser(roles = "USER")
    void getSolarData_reusesCachedCity_whenNameDiffersOnlyInCase() throws Exception {
        performGetSolarDataRequest("Budapest");
        performGetSolarDataRequest("budapest");

        verify(geocodingService, times(1)).getGeoData(anyString());
        assertThat(cityRepository.count()).isEqualTo(1);
    }

    @Test
    @WithMockUser(roles = "USER")
    void getSolarData_reusesExistingCity_whenAlternateSpellingResolvesToSameCity() throws Exception {
        performGetSolarDataRequest("Budapest");
        performGetSolarDataRequest("Budapeszt");

        verify(geocodingService, times(2)).getGeoData(anyString());
        assertThat(cityRepository.count()).isEqualTo(1);
    }

    @Test
    @WithMockUser(roles = "USER")
    void getSolarData_requestsRequestedDate_whenDateParamProvided() throws Exception {
        mockMvc.perform(get("/api/solardata")
                .param("city", "Budapest")
                .param("date", "2026-06-21"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.date").value("2026-06-21"));

        verify(solarService).getSolarData(anyDouble(), anyDouble(), eq(LocalDate.of(2026, 6, 21)));
    }

    @Test
    @WithMockUser(roles = "USER")
    void getSolarData_usesCityLocalDate_whenNoDateGiven() throws Exception {
        when(geocodingService.getGeoData(anyString()))
                .thenReturn(new GeoData("Tokyo", "Tokyo", 35.68, 139.69, "JP"));

        when(solarService.getSolarData(anyDouble(), anyDouble(), any(LocalDate.class)))
                .thenReturn(new SolarData(
                        OffsetDateTime.parse("2026-10-15T05:46:00+09:00"),
                        OffsetDateTime.parse("2026-10-15T17:07:00+09:00"),
                        "Asia/Tokyo")); // this is the important part

        performGetSolarDataRequest("Tokyo"); // the city is new and has no timezone yet
        performGetSolarDataRequest("Tokyo"); // now it has a timezone

        verify(solarService).getSolarData(anyDouble(), anyDouble(), eq(LocalDate.of(2026, 10, 14)));

        verify(solarService).getSolarData(anyDouble(), anyDouble(), eq(LocalDate.of(2026, 10, 15))); // if this fails
    }

    private void performGetSolarDataRequest(String city) throws Exception {
        mockMvc.perform(get("/api/solardata")
                .param("city", city))
                .andExpect(status().isOk());
    }
}
