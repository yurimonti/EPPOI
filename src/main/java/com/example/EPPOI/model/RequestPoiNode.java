package com.example.EPPOI.model;

import com.example.EPPOI.model.poi.PoiNode;
import com.example.EPPOI.model.user.UserNode;
import com.example.EPPOI.utility.PoiParamsProvider;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.List;

@Node
@Data
@NoArgsConstructor
//FIXME: reconsidering class parameters or using a relationship instead
public class RequestPoiNode {
    @Id @GeneratedValue
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
    @Relationship(type = "POI_REQUEST_REQUIRED_BY",direction = Relationship.Direction.OUTGOING)
    private UserNode madeBy;
    private PoiNode target;

    public RequestPoiNode(PoiParamsProvider params, UserNode madeBy) {
        this.name = params.getName();
        this.description = params.getDescription();
        this.coordinate = new CoordsNode(params.getCoordinate().getLat(), params.getCoordinate().getLon());
        this.madeBy = madeBy;
    }
}
