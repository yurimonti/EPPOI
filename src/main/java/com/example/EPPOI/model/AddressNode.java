package com.example.EPPOI.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

@Data
@NoArgsConstructor
@Node
public class AddressNode {
    @Id
    @GeneratedValue
    private Long id;
    private String street;
    private Integer number;

    public AddressNode(String street, Integer number) {
        this();
        this.street = street;
        this.number = number;
    }
}
