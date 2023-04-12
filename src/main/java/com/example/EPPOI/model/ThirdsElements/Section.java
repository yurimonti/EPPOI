package com.example.EPPOI.model.ThirdsElements;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

import java.util.List;

@Data
@AllArgsConstructor
@Node
public class Section {

    @Id
    @GeneratedValue
    private Long id;

    private List<Element> elements;
}