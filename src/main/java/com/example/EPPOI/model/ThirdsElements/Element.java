package com.example.EPPOI.model.ThirdsElements;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

@Data
@AllArgsConstructor
@Node
public class Element {
    @Id
    @GeneratedValue
    private Long id;

    private String name;
    private Double price;
    private boolean isVegetarian;
    private boolean isVegan;
    private boolean isFrozen;

    //modify with various bool for the allergic food
    private boolean containAllergicStuff;

}