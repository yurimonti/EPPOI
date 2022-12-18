package com.example.EPPOI.model;

import com.example.EPPOI.model.poi.PoiNode;
import com.example.EPPOI.model.user.UserNode;
import com.example.EPPOI.utility.PoiParamsProvider;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.ArrayList;
import java.util.List;

@Node
@Data
@AllArgsConstructor
//FIXME: reconsidering class parameters or using a relationship instead
public class RequestPoiNode {
    @Id @GeneratedValue
    private Long id;
    private String name;
    private String description;
    private Boolean isAccepted;
    private CoordsNode coordinate;
    private TimeSlotNode hours;
    private Double timeToVisit;//visit time's average expressed in minutes
    private AddressNode address;
    private Double ticketPrice;
    @Relationship(type = "POI_HAS_TYPE",direction = Relationship.Direction.OUTGOING)
    private List<PoiTypeNode> types;
    private ContactNode contact;
    @Relationship(type = "POI_TAG_VALUE",direction = Relationship.Direction.OUTGOING)
    private List<PoiTagRel> tagValues;

    //TODO: consider to get this value directly from User who made that
    /*@Relationship(type = "POI_REQUEST_REQUIRED_BY",direction = Relationship.Direction.INCOMING)*/
    private String createdBy;
    private PoiNode target;

    public RequestPoiNode() {
        this.isAccepted = null;
        this.types = new ArrayList<>();
        this.tagValues = new ArrayList<>();
        this.target = null;
    }

    public RequestPoiNode(String name, String description, CoordsNode coordinate, TimeSlotNode hours, Double timeToVisit,
                          AddressNode address, Double ticketPrice, List<PoiTypeNode> types, ContactNode contact,
                          List<PoiTagRel> tagValues, PoiNode target) {
        this();
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
        this.target = target;
    }
}
