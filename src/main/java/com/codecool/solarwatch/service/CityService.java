package com.codecool.solarwatch.service;

import com.codecool.solarwatch.dto.CityDto;
import com.codecool.solarwatch.exception.CityNotFoundException;
import com.codecool.solarwatch.model.City;
import com.codecool.solarwatch.repository.CityRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CityService {
    private final CityRepository repository;

    public CityService(CityRepository repository) {
        this.repository = repository;
    }

    public List<CityDto> findAll() {
        return repository.findAll().stream().map(CityDto::from).toList();
    }

    public CityDto findById(Long id) {
        return repository.findById(id).map(CityDto::from).orElseThrow(() -> new CityNotFoundException("City not found: " + id));
    }

    @Transactional
    public CityDto create(CityDto dto) {
        return CityDto.from(repository.save(new City(dto.name(), dto.longitude(), dto.latitude(), dto.state(), dto.country())));
    }

    @Transactional
    public CityDto update(Long id, CityDto dto) {
        City city = repository.findById(id).orElseThrow(() -> new CityNotFoundException("City not found: " + id));

        city.update(dto.name(), dto.longitude(), dto.latitude(), dto.state(), dto.country());

        return CityDto.from(city);
    }

    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) throw new CityNotFoundException("City not found: " + id);

        repository.deleteById(id);
    }
}
