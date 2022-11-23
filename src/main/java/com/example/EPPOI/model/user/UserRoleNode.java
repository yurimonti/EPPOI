package com.example.EPPOI.model.user;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

@Node
@Data
@NoArgsConstructor
public class UserRoleNode {
    @Id
    @GeneratedValue
    private Long id;
    private String name;

    public UserRoleNode(String name) {
        this.name = name;
    }
}
