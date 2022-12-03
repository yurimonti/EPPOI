package com.example.EPPOI.service;

import com.example.EPPOI.model.CityNode;
import com.example.EPPOI.model.poi.PoiNode;
import com.example.EPPOI.repository.CityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CityServiceImpl implements CityService {
    private final CityRepository cityRepository;
    @Override
    public List<CityNode> getCitiesByPoi(PoiNode... pois) {
        List<CityNode> cities = new ArrayList<>();
        for (PoiNode p: pois) {
            cities.add(this.cityRepository.findAll().stream()
                    .filter(c->c.getPOIs().contains(p))
                    .findAny()
                    .orElseThrow(NullPointerException::new));
        }
        return cities.stream().distinct().toList();
    }
}
