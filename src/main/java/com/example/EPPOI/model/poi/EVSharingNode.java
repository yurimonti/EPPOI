package com.example.EPPOI.model.poi;

import com.example.EPPOI.model.PaymentMethod;
import lombok.*;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.List;

/*@Data
@Node
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)*/
public class EVSharingNode/* extends PoiNode implements ThirdPoiNode*/{
    //TODO: clarify this with guys
    /*@Relationship(value = "EV_PROVIDES_PAYMENT")
    private List<PaymentMethod> payments;
    private Boolean membershipRequired;
    @Relationship(value = "EV_HAS_TYPE")
    private List<EVTypeRel> evTypes;*/

}
