package com.example.EPPOI.service;

import com.example.EPPOI.model.CityNode;
import com.example.EPPOI.model.poi.PoiNode;
import com.example.EPPOI.repository.CityRepository;
import com.example.EPPOI.repository.PoiRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PoiServiceImpl implements PoiService {
    private final PoiRepository poiRepository;
    private final CityRepository cityRepository;
    @Override
    public PoiNode findPoiById(Long id) {
        return this.poiRepository.findById(id).orElseThrow(()->new NullPointerException("PoiNode not found"));
    }

    @Override
    public void setCityToPoi(PoiNode poi,CityNode city) {
        city.getPOIs().add(poi);
        this.cityRepository.save(city);
    }

    @Override
    public PoiNode savePoi(PoiNode toSave) {
        return this.poiRepository.save(toSave);
    }

    @Override
    public List<PoiNode> getAllPois() {
        return this.poiRepository.findAll();
    }
}
