package com.example.EPPOI.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

@Data
@NoArgsConstructor
@Node
@AllArgsConstructor
public class CoordsNode {
    @Id
    @GeneratedValue
    private Long id;
    private Double lat;
    private Double lon;

    public CoordsNode(Double lat, Double lon) {
        this.lat = lat;
        this.lon = lon;
    }
}
