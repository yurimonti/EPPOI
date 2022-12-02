package com.example.EPPOI.service;

import com.example.EPPOI.model.ItineraryNode;
import com.example.EPPOI.model.ThirdPartyRegistrationRequest;
import com.example.EPPOI.model.poi.PoiNode;
import com.example.EPPOI.model.user.EnteNode;
import com.example.EPPOI.repository.EnteRepository;
import com.example.EPPOI.utility.PoiForm;

import java.util.List;

public interface EnteService extends AppUserService<EnteNode> {
    PoiNode createPoi(EnteNode ente,PoiForm form);
    ItineraryNode createItinerary(EnteNode ente,String name, String description, List<PoiNode> POIs);
    //TODO:set request as target instead of itinerary
    void setConsensus(ItineraryNode target,boolean consensus);
    void setConsensusToRegistration(EnteNode ente,ThirdPartyRegistrationRequest target,boolean consensus);
    EnteRepository getRepository();
}
