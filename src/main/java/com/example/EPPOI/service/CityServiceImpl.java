package com.example.EPPOI.service;

import com.example.EPPOI.model.CityNode;
import com.example.EPPOI.model.ItineraryNode;
import com.example.EPPOI.model.poi.PoiNode;
import com.example.EPPOI.repository.CityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

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
            cities.add(this.getCityByPoi(p.getId()));
        });
        return cities.stream().distinct().toList();
    }

    @Override
    public List<CityNode> getCitiesByItinerary(ItineraryNode itinerary)throws NullPointerException {
        if(Objects.isNull(itinerary)) throw new NullPointerException("Itinerary is null");
        List<CityNode> result = this.getAllCities().stream().filter(c -> c.getItineraries().stream().anyMatch(i->i.getId().equals(itinerary.getId()))).toList();
        if(result.size() <=0 ) throw new NullPointerException("No such city contains this itinerary "+itinerary.getId());
        return result;
        /*if(Objects.isNull(itinerary)) throw new NullPointerException("Itinerary is null");
        return this.cityRepository.getCityByItineraryId(itinerary.getId());*/
        //itineraries.forEach();
    }

    @Override
    public CityNode getCityById(Long cityId) {
        return this.cityRepository.findById(cityId).orElseThrow(()-> new NullPointerException("No such city"));
    }

    @Override
    public List<CityNode> getAllCities() {
        return this.cityRepository.findAll();
    }

    @Override
    public CityNode getCityByPoi(Long poiId) throws NullPointerException {
        /*CityNode c =  this.cityRepository.getCityByPoiId(poiId)
                .orElseThrow(()-> new NullPointerException("No such city contains this poi "+poiId));
        log.info("city {}",c);
        return c;*/
        return this.getAllCities().stream().filter(c -> c.getPOIs().stream().anyMatch(p->p.getId().equals(poiId))).findFirst()
                .orElseThrow(()-> new NullPointerException("No such city contains this poi "+poiId));
        /*return this.getAllCities().stream().filter(c -> c.getPOIs().stream().map(PoiNode::getId).toList().contains(poiId)).findFirst()
                .orElseThrow(()-> new NullPointerException("No such city contains this poi "+poiId));*/
    }

    @Override
    public void saveCity(CityNode toSave) {
        this.cityRepository.save(toSave);
    }


    @Override
    public void saveCities(List<CityNode> toSave) {
        this.cityRepository.saveAll(toSave);
    }

    @Override
    public void addItinerary(CityNode city,ItineraryNode toSave) {
        city.getItineraries().add(toSave);
        this.cityRepository.save(city);
    }
}
