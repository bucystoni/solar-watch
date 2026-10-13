package com.codecool.solarwatch.repository;

import com.codecool.solarwatch.model.City;
import com.codecool.solarwatch.model.SolarTime;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface SolarTimeRepository extends JpaRepository<SolarTime, Long> {

    Optional<SolarTime> findByCityAndDate(City city, LocalDate date);
}
