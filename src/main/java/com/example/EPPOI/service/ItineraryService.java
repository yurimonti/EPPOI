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
    void fillItinerary(ItineraryRequestNode toFill, List<PoiNode> target);
}
