package com.example.EPPOI.model.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.data.neo4j.core.schema.Node;

@Node
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@AllArgsConstructor
public class AdminNode extends UserNode{

    public AdminNode(String name, String surname, String email, String password, String username, UserRoleNode... roles) {
        super(name, surname, email, password, username, roles);

    }
}
