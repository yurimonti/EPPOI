package com.example.EPPOI.service;

import com.example.EPPOI.model.ItineraryNode;
import com.example.EPPOI.model.ItineraryRequestNode;
import com.example.EPPOI.model.ThirdPartyRegistrationRequest;
import com.example.EPPOI.model.poi.PoiNode;
import com.example.EPPOI.model.user.EnteNode;
import com.example.EPPOI.repository.EnteRepository;
import com.example.EPPOI.utility.ItineraryForm;
import com.example.EPPOI.utility.PoiForm;

public interface EnteService extends AppUserService<EnteNode> {
    PoiNode createPoi(EnteNode ente,PoiForm form);
    ItineraryNode createItinerary(EnteNode ente, ItineraryForm itineraryForm);
    //TODO:set request as target instead of itinerary
    void setConsensusToItinerary(ItineraryRequestNode target,boolean consensus);
    void setConsensusToRegistration(EnteNode ente,ThirdPartyRegistrationRequest target,boolean consensus);

    void deleteRegistrationRequest(EnteNode ente,ThirdPartyRegistrationRequest target);
    EnteRepository getRepository();

    ItineraryRequestNode createItineraryRequest(EnteNode ente, ItineraryForm itineraryForm);
}
