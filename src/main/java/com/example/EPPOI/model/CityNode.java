package com.example.EPPOI.model;

import com.example.EPPOI.model.poi.PoiNode;
import lombok.Data;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.ArrayList;
import java.util.List;

@Node
@Data
public class CityNode {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    @Relationship(type = "CITY_CONTAINS_POI",direction = Relationship.Direction.OUTGOING)
    private List<PoiNode> POIs;
    @Relationship(type = "CITY_PROVIDES_ITINERARY",direction = Relationship.Direction.OUTGOING)
    private List<ItineraryNode> itineraries;
    private CoordsNode coords;

    public CityNode(){
        this.POIs = new ArrayList<>();
        this.itineraries = new ArrayList<>();
    }

    public CityNode(String name,CoordsNode  coords) {
        this();
        this.name = name;
        this.coords = coords;
    }
}
