package com.example.EPPOI.model.user;

import com.example.EPPOI.model.ItineraryNode;
import com.example.EPPOI.model.RequestPoiNode;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.ArrayList;
import java.util.List;

@Node
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class TouristNode extends UserNode{
    //private List<PoiRequestNode> poiRequests;
    @Relationship(type = "TOURIST_CREATES_ITINERARY",direction = Relationship.Direction.OUTGOING)
    private List<ItineraryNode> itineraries;
    @Relationship(type = "TOURIST_REQUIRES_POI",direction = Relationship.Direction.OUTGOING)
    private List<RequestPoiNode> poiRequests;
    //private List<ItineraryRequestNode> itineraryRequests;

    public TouristNode(){
        super();
        this.itineraries = new ArrayList<>();
        this.poiRequests = new ArrayList<>();
    }
    public TouristNode(String name,String surname,String email, String password, String username, UserRoleNode ...roles) {
        super(name,surname,email,password,username,roles);
        //this.poiRequests = new ArrayList<>();
        this.itineraries = new ArrayList<>();
        this.poiRequests = new ArrayList<>();
        //this.itineraryRequests = new ArrayList<>();
    }
}
