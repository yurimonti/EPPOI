package com.example.EPPOI.model;

import lombok.AllArgsConstructor;
import lombok.Data;
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
    //private Boolean isAccepted;
    private List<ItineraryRelPoi> points;

    /*private List<CategoryNode> categories;*/
    private Boolean accepted;
    private Integer consensus;
    private String createdBy;
    private Double timeToVisit;
    private List<String> geoJsonList;

    public ItineraryRequestNode() {
        this.points = new ArrayList<>();
        /*this.categories = new ArrayList<>();*/
        this.geoJsonList = new ArrayList<>();
        this.accepted = false;
    }

    public ItineraryRequestNode(String name, String description) {
        this();
        this.name = name;
        this.description = description;
    }
}
