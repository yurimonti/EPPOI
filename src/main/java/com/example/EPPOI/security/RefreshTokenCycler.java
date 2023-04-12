package com.example.EPPOI.security;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Node
@Data
public class RefreshTokenCycler {
    @Id @GeneratedValue
    private Long id;
    private List<String> refreshTokens;

    public RefreshTokenCycler(){
        this.refreshTokens = new ArrayList<>();
    }
}
