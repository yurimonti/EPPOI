package com.example.EPPOI.service;

import com.example.EPPOI.model.ItineraryNode;
import com.example.EPPOI.model.PoiNode;
import com.example.EPPOI.model.user.EnteNode;
import com.example.EPPOI.utility.PoiParamsProvider;

import java.util.List;

public interface EnteService extends AppUserService<EnteNode> {
    PoiNode createPoi(PoiParamsProvider params);
    ItineraryNode createItinerary(String name, String description, List<PoiNode> POIs);
    void setConsensus(ItineraryNode target,boolean consensus);
}
