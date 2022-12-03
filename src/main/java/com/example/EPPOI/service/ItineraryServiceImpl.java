package com.example.EPPOI.service;

import com.example.EPPOI.model.ItineraryNode;
import com.example.EPPOI.model.ItineraryRelPoi;
import com.example.EPPOI.model.ItineraryRequestNode;
import com.example.EPPOI.model.poi.PoiNode;
import com.example.EPPOI.repository.ItineraryRepository;
import com.example.EPPOI.repository.ItineraryRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItineraryServiceImpl implements ItineraryService{
    private final ItineraryRepository itineraryRepository;
    private final ItineraryRequestRepository itineraryRequestRepository;

    @Override
    public void fillItinerary(ItineraryNode toFill, List<PoiNode> target) {
        for(int i = 1; i<=target.size();i++){
            toFill.getPoints().add(new ItineraryRelPoi(target.get(i-1),i));
        }
        this.itineraryRepository.save(toFill);
    }

    @Override
    public void fillItinerary(ItineraryRequestNode toFill, List<PoiNode> target) {
        for(int i = 1; i<=target.size();i++){
            toFill.getPoints().add(new ItineraryRelPoi(target.get(i-1),i));
        }
        this.itineraryRequestRepository.save(toFill);
    }
}
