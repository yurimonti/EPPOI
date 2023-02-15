package com.example.EPPOI.model.user;

import com.example.EPPOI.model.RequestPoiNode;
import com.example.EPPOI.model.poi.PoiNode;
import com.example.EPPOI.model.poi.ThirdPoiNode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.ArrayList;
import java.util.List;

@Node
@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ThirdUserNode extends UserNode {
    private String companyName;
    @Relationship(type = "THIRD_MANAGES_POIS", direction = Relationship.Direction.OUTGOING)
    private List<PoiNode> thirdPOIs;

    @Relationship(type = "THIRD_MANAGES_POI_REQUEST", direction = Relationship.Direction.OUTGOING)
    private List<RequestPoiNode> poiRequests;

    public ThirdUserNode(String companyName, String name, String surname, String email, String password, String username, UserRoleNode... roles) {
        super(name, surname, email, password, username, roles);
        this.companyName = companyName;
        this.poiRequests = new ArrayList<>();
        this.thirdPOIs = new ArrayList<>();
    }

}
