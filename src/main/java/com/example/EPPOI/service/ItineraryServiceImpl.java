package com.example.EPPOI.service;

import com.example.EPPOI.model.ItineraryNode;
import com.example.EPPOI.model.ItineraryRelPoi;
import com.example.EPPOI.model.ItineraryRequestNode;
import com.example.EPPOI.model.ItineraryRequestRel;
import com.example.EPPOI.model.poi.PoiNode;
import com.example.EPPOI.repository.ItineraryRepository;
import com.example.EPPOI.repository.ItineraryRequestRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
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

    @Override
    public void saveItinerary(ItineraryNode toSave) {
        if(Objects.isNull(toSave)) throw new IllegalArgumentException("the entity to save is null");
        this.itineraryRepository.save(toSave);
    }

    @Override
    public void saveItinerary(ItineraryRequestNode toSave) {
        if(Objects.isNull(toSave)) throw new IllegalArgumentException("the entity to save is null");
        this.itineraryRequestRepository.save(toSave);
    }

    @Override
    public ItineraryNode createItineraryFromRequest(ItineraryRequestNode request) {
        ItineraryNode result = new ItineraryNode(request.getName(),request.getDescription());
        request.getPoints().forEach(p -> result.getPoints().add(new ItineraryRelPoi(p.getPoi(),p.getIndex())));
        this.saveItinerary(result);
        return result;
    }
}
