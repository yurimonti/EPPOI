package com.example.EPPOI.model.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;
import org.springframework.data.neo4j.core.support.UUIDStringGenerator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Node
@Data
@AllArgsConstructor
public abstract class UserNode {
    @Id
    @GeneratedValue(generatorClass = UUIDStringGenerator.class)
    private String id;
    private String name;
    private String surname;
    private String email;
    private String password;
    private String username;
    @Relationship( value = "USER_HAS_ROLE",direction = Relationship.Direction.OUTGOING)
    private List<UserRoleNode> roles;

    public UserNode() {
        this.roles = new ArrayList<>();
    }

    public UserNode(String name,String surname,String email, String password, String username) {
        this();
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.password = password;
        this.username = username;
    }

    public UserNode(String name,String surname,String email, String password, String username, UserRoleNode ...roles) {
        this(name,surname,email, password, username);
        this.roles = Arrays.stream(roles).toList();
    }
}