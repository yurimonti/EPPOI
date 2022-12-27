package com.example.EPPOI.service;

import com.example.EPPOI.model.ItineraryNode;
import com.example.EPPOI.model.ItineraryRequestNode;
import com.example.EPPOI.model.poi.PoiNode;
import com.example.EPPOI.repository.ItineraryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ItineraryService {
    void fillItinerary(ItineraryNode toFill, List<PoiNode> target);

    ItineraryNode getItinerary(Long id) throws NullPointerException;

    void deleteItinerary(List<ItineraryNode> toDelete);
    void deleteItinerary(ItineraryNode toDelete);

    List<ItineraryNode> getItinerariesFromPoi(PoiNode poi);
    void fillItinerary(ItineraryRequestNode toFill, List<PoiNode> target);

    void saveItinerary(ItineraryNode toSave);
    void saveItinerary(ItineraryRequestNode toSave);

    ItineraryNode createItineraryFromRequest(ItineraryRequestNode request);

    ItineraryNode createBaseItinerary(String name,String description,List<PoiNode> points,
                                      List<String> geoJsonList,boolean isDefault);
}
