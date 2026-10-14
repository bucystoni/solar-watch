package com.codecool.solarwatch.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class City {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private double longitude;
    private double latitude;
    private String state;
    private String country;
    private String timezone;

    @OneToMany(mappedBy = "city", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SolarTime> solarTimes = new ArrayList<>();

    protected City() {

    }

    public City(String name, double longitude, double latitude, String state, String country) {
        this.name = name;
        this.longitude = longitude;
        this.latitude = latitude;
        this.state = state;
        this.country = country;
    }

    public String getName() {
        return name;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getCountry() {
        return country;
    }

    public String getState() {
        return state;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public Long getId() {
        return id;
    }

    public void update(String name, double longitude, double latitude,
                       String state, String country) {
        this.name = name;
        this.longitude = longitude;
        this.latitude = latitude;
        this.state = state;
        this.country = country;
    }
}
