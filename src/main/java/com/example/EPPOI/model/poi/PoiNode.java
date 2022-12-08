package com.example.EPPOI.model.poi;

import com.example.EPPOI.model.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.ArrayList;
import java.util.List;

@Node @Data @AllArgsConstructor
public class PoiNode {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String description;
    private CoordsNode coordinate;
    private TimeSlotNode hours;
    private Double timeToVisit;//visit time's average expressed in minutes
    private AddressNode address;
    private Double ticketPrice;
    private List<String> contributors;
    @Relationship(type = "POI_HAS_TYPE",direction = Relationship.Direction.OUTGOING)
    private List<PoiTypeNode> types;
    private ContactNode contact;
    @Relationship(type = "POI_TAG_VALUE",direction = Relationship.Direction.OUTGOING)
    private List<PoiTagRel> tagValues;

    public PoiNode(){
        this.contributors=new ArrayList<>();
        this.types=new ArrayList<>();
        this.tagValues=new ArrayList<>();
    }

    //TODO: eliminate this constructor
    public PoiNode(String name, String description,CoordsNode coordinate, Double timeToVisit) {
        this();
        this.name = name;
        this.description = description;
        this.coordinate = coordinate;
        this.timeToVisit = timeToVisit;
    }

    public PoiNode(String name, String description, CoordsNode coordinate, TimeSlotNode hours, Double timeToVisit,
                   AddressNode address, Double ticketPrice, List<PoiTypeNode> types, ContactNode contact) {
        this(name, description, coordinate, timeToVisit);
        this.hours = hours;
        this.address = address;
        this.ticketPrice = ticketPrice;
        this.types = types;
        this.contact = contact;
    }
}
