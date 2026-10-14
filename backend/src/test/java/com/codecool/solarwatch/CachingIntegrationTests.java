package com.codecool.solarwatch;

import com.codecool.solarwatch.model.GeoData;
import com.codecool.solarwatch.model.SolarData;
import com.codecool.solarwatch.service.GeocodingService;
import com.codecool.solarwatch.service.SolarService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.OffsetDateTime;

import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyString;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class CachingIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private GeocodingService geocodingService;

    @MockitoBean
    private SolarService solarService;

    @BeforeEach
    void setup() {
        when(geocodingService.getGeoData(anyString()))
                .thenReturn(new GeoData("Pest", "Budapest", 1, 2, "Hungary"));

        OffsetDateTime sunrise = OffsetDateTime.parse("2026-01-15T07:30:00+01:00");
        OffsetDateTime sunset = OffsetDateTime.parse("2026-01-15T21:30:00+01:00");
        when(solarService.getSolarData(anyDouble(), anyDouble())).thenReturn(new SolarData(sunrise, sunset));
    }

    @Test
    @WithMockUser(roles = "USER")
    void getSolarData_callsExternalApiOnce_whenRequestedTwice() throws Exception {
        mockMvc.perform(get("/api/solardata")
                        .param("city", "Budapest"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/solardata")
                        .param("city", "Budapest"))
                .andExpect(status().isOk());


        verify(geocodingService, times(1)).getGeoData(anyString());

        verify(solarService, times(1)).getSolarData(anyDouble(), anyDouble());

    }
}
