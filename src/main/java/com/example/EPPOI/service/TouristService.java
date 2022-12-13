package com.example.EPPOI.service;

import com.example.EPPOI.model.ItineraryNode;
import com.example.EPPOI.model.poi.PoiNode;
import com.example.EPPOI.model.RequestPoiNode;
import com.example.EPPOI.model.user.TouristNode;
import com.example.EPPOI.repository.TouristRepository;
import com.example.EPPOI.utility.PoiForm;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TouristService extends AppUserService<TouristNode> {
    ItineraryNode createItinerary(TouristNode creator,String name, String description, List<PoiNode> POIs);
    RequestPoiNode createRequestPoi(TouristNode creator, PoiForm params, Long cityId);
    TouristRepository getRepository();

}
