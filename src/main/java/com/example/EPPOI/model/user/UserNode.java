package com.example.EPPOI.model.user;

import lombok.Data;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.support.UUIDStringGenerator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Node
@Data
public abstract class UserNode {
    @Id
    @GeneratedValue(generatorClass = UUIDStringGenerator.class)
    private String id;
    private String name;
    private String surname;
    private String email;
    private String password;
    private String username;
    private List<UserRoleNode> roles;

    //email verification elements needed
    private String verificationCode;
    private boolean enabled;

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