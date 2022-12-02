package com.example.EPPOI.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.RelationshipProperties;
import org.springframework.data.neo4j.core.schema.TargetNode;

@RelationshipProperties
@Data
@NoArgsConstructor
public class ThirdPartyRegistrationRel {
    @Id
    @GeneratedValue
    private Long id;
    @TargetNode
    private ThirdPartyRegistrationRequest request;
    private Boolean consensus;

    public ThirdPartyRegistrationRel(ThirdPartyRegistrationRequest request, Boolean consensus) {
        this.request = request;
        this.consensus = consensus;
    }
}
