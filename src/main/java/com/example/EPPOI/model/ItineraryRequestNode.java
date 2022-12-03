package com.example.EPPOI.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

import java.util.ArrayList;
import java.util.List;

@Node
@Data
@AllArgsConstructor
public class ItineraryRequestNode {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String description;
    private List<ItineraryRelPoi> points;
    private Boolean accepted;
    private Integer consensus;
    private String createdBy;
    private Double timeToVisit;
    private List<String> geoJsonList;

    public ItineraryRequestNode() {
        this.points = new ArrayList<>();
        this.geoJsonList = new ArrayList<>();
    }

    public ItineraryRequestNode(String name, String description) {
        this();
        this.name = name;
        this.description = description;
    }
}
