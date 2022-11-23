package com.example.EPPOI.model;

import lombok.Data;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.ArrayList;
import java.util.List;

@Node
@Data
public class PoiTypeNode {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    @Relationship(type = "TYPE_HAS_CATEGORY",direction = Relationship.Direction.OUTGOING)
    private List<CategoryNode> categories;
    @Relationship(type = "TYPE_HAS_TAG",direction = Relationship.Direction.OUTGOING)
    private List<TagNode> tags;

    public PoiTypeNode() {
        this.categories = new ArrayList<>();
        this.tags = new ArrayList<>();
    }

    public PoiTypeNode(String name, List<CategoryNode> categories, List<TagNode> tags) {
        this();
        this.name = name;
        this.categories = categories;
        this.tags = tags;
    }
}
