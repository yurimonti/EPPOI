package com.example.EPPOI.model.poi;

import com.example.EPPOI.model.*;
import lombok.*;
import org.springframework.data.neo4j.core.schema.Node;

import java.util.List;

@Data
@Node
@NoArgsConstructor
//@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class RestaurantPoiNode extends PoiNode{
    //private Menu menu;

    public RestaurantPoiNode(String name, String description, CoordsNode coordinate, TimeSlotNode hours,
                             Double timeToVisit, AddressNode address, Double ticketPrice, List<PoiTypeNode> types,
                             ContactNode contact) {
        super(name, description, coordinate, hours, timeToVisit, address, ticketPrice, types, contact);
    }
}
