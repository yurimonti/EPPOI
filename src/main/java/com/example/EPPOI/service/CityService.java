package com.example.EPPOI.service;

import com.example.EPPOI.model.CityNode;
import com.example.EPPOI.model.ItineraryNode;
import com.example.EPPOI.model.poi.PoiNode;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CityService {
    List<CityNode> getCitiesByPoi(List<PoiNode> pois);

    List<CityNode> getAllCities();

    CityNode getCityByPoi(PoiNode poi);
    void saveCity(CityNode ...toSave);

    void addItinerary(CityNode city,ItineraryNode target);

}
