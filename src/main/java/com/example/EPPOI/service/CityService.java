package com.example.EPPOI.service;

import com.example.EPPOI.model.CityNode;
import com.example.EPPOI.model.ItineraryNode;
import com.example.EPPOI.model.poi.PoiNode;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CityService {
    List<CityNode> getCitiesByPoi(List<PoiNode> pois);

    CityNode getCityById(Long cityId);

    List<CityNode> getAllCities();

    CityNode getCityByPoi(Long poiId);
    void saveCity(CityNode toSave);

    void saveCities(List<CityNode> toSave);

    void addItinerary(CityNode city,ItineraryNode target);

}
