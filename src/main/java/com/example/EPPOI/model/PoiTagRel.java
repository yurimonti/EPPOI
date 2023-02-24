package com.example.EPPOI.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.neo4j.core.schema.RelationshipId;
import org.springframework.data.neo4j.core.schema.RelationshipProperties;
import org.springframework.data.neo4j.core.schema.TargetNode;

@RelationshipProperties
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PoiTagRel {
    @RelationshipId
    private Long id;
    @TargetNode
    private TagNode tag;

    private Boolean booleanValue;

    private String stringValue;

    public PoiTagRel(TagNode tag) {
        this.tag = tag;
    }
    public PoiTagRel(TagNode tag, Boolean booleanValue, String stringValue) {
        this.tag = tag;
        this.booleanValue = booleanValue;
        this.stringValue = stringValue;
    }
}
