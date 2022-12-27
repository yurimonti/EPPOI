package com.example.EPPOI.utility;

import com.example.EPPOI.model.*;
import com.example.EPPOI.model.poi.EVSharingNode;
import com.example.EPPOI.model.poi.PoiNode;
import com.example.EPPOI.model.poi.RestaurantPoiNode;
import com.example.EPPOI.repository.PoiRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ConcreteFactoryPoi implements AbstractFactoryPoi{
    private final PoiRepository poiRepository;

    @Override
    public PoiNode createBasePoi(String name, String description, CoordsNode coordinate, TimeSlotNode hours, Double timeToVisit,
                                 AddressNode address, Double ticketPrice, List<PoiTypeNode> types, ContactNode contact,
                                 List<PoiTagRel> tagValues) {
        PoiNode result = new PoiNode(name, description, coordinate,hours, timeToVisit,address,ticketPrice,types,contact);
        result.setTagValues(tagValues);
        this.poiRepository.save(result);
        return result;
    }

    @Override
    public RestaurantPoiNode createRestaurantPoi(String name, String description, CoordsNode coordinate, TimeSlotNode hours, Double timeToVisit,
                                                 AddressNode address, Double ticketPrice, List<PoiTypeNode> types,
                                                 ContactNode contact,List<PoiTagRel> tagValues) {
        RestaurantPoiNode result = new RestaurantPoiNode(name, description, coordinate,hours, timeToVisit,address,ticketPrice,types,contact);
        result.setTagValues(tagValues);
        this.poiRepository.save(result);
        return result;
    }

    @Override
    public EVSharingNode createEvPoi(String name, String description, CoordsNode coordinate, TimeSlotNode hours, Double timeToVisit,
                                     AddressNode address, Double ticketPrice, List<PoiTypeNode> types,
                                     ContactNode contact,List<PoiTagRel> tagValues) {
        EVSharingNode result = new EVSharingNode(name, description, coordinate,hours, timeToVisit,address,ticketPrice,
                types,contact);
        result.setTagValues(tagValues);
        this.poiRepository.save(result);
        return result;
    }
}
