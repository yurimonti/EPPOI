package com.example.EPPOI.utility;

import com.example.EPPOI.model.*;
import com.example.EPPOI.model.poi.EVSharingNode;
import com.example.EPPOI.model.poi.PoiNode;
import com.example.EPPOI.model.poi.RestaurantPoiNode;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AbstractFactoryPoi {
    PoiNode createBasePoi(String name, String description, CoordsNode coordinate, TimeSlotNode hours, Double timeToVisit,
                          AddressNode address, Double ticketPrice, List<PoiTypeNode> types, ContactNode contact,
                          List<PoiTagRel> tagValues);
    RestaurantPoiNode createRestaurantPoi(String name, String description, CoordsNode coordinate, TimeSlotNode hours,
                                          Double timeToVisit,AddressNode address, Double ticketPrice,
                                          List<PoiTypeNode> types, ContactNode contact,List<PoiTagRel> tagValues);
    EVSharingNode createEvPoi(String name, String description, CoordsNode coordinate, TimeSlotNode hours, Double timeToVisit,
                              AddressNode address, Double ticketPrice, List<PoiTypeNode> types,ContactNode contact,
                              List<PoiTagRel> tagValues);
}
