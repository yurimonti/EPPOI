package com.example.EPPOI.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.ArrayList;
import java.util.List;

@Node
@Data
public class ItineraryNode {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String description;
    private String createdBy;
    private Boolean isDefault;
    @Relationship(type = "ITINERARY_CONTAINS_POI",direction = Relationship.Direction.OUTGOING)
    private List<ItineraryRelPoi> points;
    private Double timeToVisit;
    private List<String> geoJsonList;

    public ItineraryNode() {
        this.points = new ArrayList<>();
        this.geoJsonList = new ArrayList<>();
    }

    public ItineraryNode(String name, String description) {
        this();
        this.name = name;
        this.description = description;
    }
}
