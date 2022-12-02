package com.example.EPPOI.service;

import com.example.EPPOI.model.CityNode;
import com.example.EPPOI.model.poi.PoiNode;
import com.example.EPPOI.repository.CityRepository;
import com.example.EPPOI.repository.ItineraryRepository;
import com.example.EPPOI.repository.PoiRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class DefaultService {
    private final ItineraryRepository itineraryRepository;
    private final PoiRepository poiRepository;
    private final CityRepository cityRepository;

    public CityNode getCityFromPoi(PoiNode poi){
        return this.cityRepository.findAll().stream().filter(c -> c.getPOIs().contains(poi)).findFirst()
                .orElseThrow(()-> new NullPointerException("No city found for that poi"));
    }

    public PoiNode getPoiByName(String name) {
        Optional<PoiNode> opt = this.poiRepository.findAll().stream().filter(poi -> poi.getName().equals(name))
                .findFirst();
        return opt.orElseThrow(()->new NullPointerException("Could not find Poi"));
    }
}
