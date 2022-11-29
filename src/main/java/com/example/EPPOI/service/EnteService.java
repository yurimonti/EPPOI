package com.example.EPPOI.service;

import com.example.EPPOI.model.ItineraryNode;
import com.example.EPPOI.model.PoiNode;
import com.example.EPPOI.model.user.EnteNode;
import com.example.EPPOI.repository.EnteRepository;
import com.example.EPPOI.repository.TouristRepository;
import com.example.EPPOI.utility.PoiForm;
import com.example.EPPOI.utility.PoiParamsProvider;

import java.util.List;

public interface EnteService extends AppUserService<EnteNode> {
    PoiNode createPoi(EnteNode ente,PoiForm form);
    ItineraryNode createItinerary(String name, String description, List<PoiNode> POIs);
    void setConsensus(ItineraryNode target,boolean consensus);

    EnteRepository getRepository();
}
