package com.example.EPPOI.service;

import com.example.EPPOI.model.CityNode;
import com.example.EPPOI.model.ItineraryNode;
import com.example.EPPOI.model.poi.PoiNode;
import com.example.EPPOI.repository.CityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CityServiceImpl implements CityService {
    private final CityRepository cityRepository;

    @Override
    public List<CityNode> getCitiesByPoi(List<PoiNode> pois) {
        List<CityNode> cities = new ArrayList<>();
        pois.forEach(p -> {
            log.info("finding city for poi: {}",p.getName());
            //Here
            cities.add(this.getCityByPoi(p));
        });
        return cities.stream().distinct().toList();
    }

    @Override
    public List<CityNode> getAllCities() {
        return this.cityRepository.findAll();
    }

    @Override
    public CityNode getCityByPoi(PoiNode poi) {
        return this.getAllCities().stream().filter(c -> c.getPOIs().stream().map(PoiNode::getId).toList().contains(poi.getId())).findFirst()
                .orElseThrow(()-> new NullPointerException("No such city contains this poi "+poi.getName()));
    }

    @Override
    public void saveCity(CityNode... toSave) {
        for(CityNode city : toSave){
            this.saveCity(city);
        }
    }

    @Override
    public void addItinerary(CityNode city,ItineraryNode toSave) {
        city.getItineraries().add(toSave);
        this.cityRepository.save(city);
    }
}
