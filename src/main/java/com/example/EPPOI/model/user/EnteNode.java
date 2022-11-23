package com.example.EPPOI.model.user;

import com.example.EPPOI.model.CityNode;
import com.example.EPPOI.model.ItineraryNode;
import com.example.EPPOI.model.RequestPoiNode;
import lombok.AllArgsConstructor;
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
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class EnteNode extends UserNode{
    @Relationship(type = "ENTE_MANAGES_CITY",direction = Relationship.Direction.OUTGOING)
    private CityNode city;

    @Relationship(type = "ENTE_MANAGES_POI_REQUEST",direction = Relationship.Direction.OUTGOING)
    private List<RequestPoiNode> poiRequests;

    public EnteNode(){
        super();
        this.poiRequests = new ArrayList<>();
    }
    public EnteNode(String name, String surname, String email, String password, String username, UserRoleNode... roles) {
        super(name, surname, email, password, username, roles);
        this.poiRequests = new ArrayList<>();
    }

    public EnteNode(String name, String surname, String email, String password, String username, CityNode city,
                    UserRoleNode... roles) {
        this(name, surname, email, password, username, roles);
        this.city = city;
    }
}
