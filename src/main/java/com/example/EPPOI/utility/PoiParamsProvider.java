package com.example.EPPOI.utility;

import com.example.EPPOI.dto.CoordsDTO;
import com.example.EPPOI.model.*;
import lombok.Data;

import java.util.List;
import java.util.Map;
//FIXME: rivedere classe

/**
 * Classe per passare parametri per poi e richieste, può passarli direttamente o dal body di una richiesta
 */
@Data
public class PoiParamsProvider {
    private String name;
    private String description;
    private CoordsDTO coordinate;
    private TimeSlotNode hours;
    private Double timeToVisit;
    private AddressNode address;
    private Double ticketPrice;
    private List<PoiTypeNode> types;
    private ContactNode contact;
    private List<PoiTagRel> tagValues;

    private PoiParamsProvider(String name, String description, CoordsDTO coordinate, TimeSlotNode hours,
                                           Double timeToVisit, AddressNode address, Double ticketPrice, List<PoiTypeNode> types,
                                           ContactNode contact, List<PoiTagRel> tagValues) {
        this.name = name;
        this.description = description;
        this.coordinate = coordinate;
        this.hours = hours;
        this.timeToVisit = timeToVisit;
        this.address = address;
        this.ticketPrice = ticketPrice;
        this.types = types;
        this.contact = contact;
        this.tagValues = tagValues;
    }

    private PoiParamsProvider(String name, String description, CoordsDTO coordinate) {
        this.name = name;
        this.description = description;
        this.coordinate = coordinate;
    }

    public static PoiParamsProvider getFromParams(String name, String description, CoordsDTO coordinate, TimeSlotNode hours,
                             Double timeToVisit, AddressNode address, Double ticketPrice, List<PoiTypeNode> types,
                             ContactNode contact, List<PoiTagRel> tagValues) {
        return new PoiParamsProvider(name,description,coordinate,hours,timeToVisit,address,ticketPrice,types,contact,tagValues);
    }

    public static PoiParamsProvider getFromBody(Map<String,Object> body){
        String name = (String) body.get("name");
        String description = (String) body.get("description");
        CoordsDTO coordsNode = new CoordinateSerializer().deserializer(body);
        return new PoiParamsProvider(name,description,coordsNode);
    }
}
