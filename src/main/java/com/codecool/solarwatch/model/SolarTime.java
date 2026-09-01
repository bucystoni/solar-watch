package com.codecool.solarwatch.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.OffsetDateTime;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"city_id", "date"}))
public class SolarTime {
    @Id
    @GeneratedValue
    private Long id;
    private LocalDate date;
    private OffsetDateTime sunrise;
    private OffsetDateTime sunset;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "city_id")
    private City city;



    protected SolarTime() {
    }

    public SolarTime(LocalDate date, OffsetDateTime sunrise, OffsetDateTime sunset, City city) {
        this.date = date;
        this.sunrise = sunrise;
        this.sunset = sunset;
        this.city = city;
    }

    public City getCity() {
        return city;
    }

    public LocalDate getDate() {
        return date;
    }

    public OffsetDateTime getSunrise() {
        return sunrise;
    }

    public OffsetDateTime getSunset() {
        return sunset;
    }
}
