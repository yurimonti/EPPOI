package com.example.EPPOI.model.poi;

import com.example.EPPOI.model.*;
import lombok.*;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.List;
@Data
@Node
@NoArgsConstructor
/*@AllArgsConstructor*/
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
//TODO: clarify this with guys
public class EVSharingNode extends PoiNode{
    public EVSharingNode(String name, String description, CoordsNode coordinate, TimeSlotNode hours, Double timeToVisit,
                         AddressNode address, Double ticketPrice, List<PoiTypeNode> types, ContactNode contact) {
        super(name, description, coordinate, hours, timeToVisit, address, ticketPrice, types, contact);
    }
    /*@Relationship(value = "EV_PROVIDES_PAYMENT")
    private List<PaymentMethod> payments;
    private Boolean membershipRequired;
    @Relationship(value = "EV_HAS_TYPE")
    private List<EVTypeRel> evTypes;*/
}
