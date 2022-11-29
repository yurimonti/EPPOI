package com.example.EPPOI.utility;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.example.EPPOI.model.user.UserNode;
import com.example.EPPOI.security.TokenManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class MiddlewareToken<T extends UserNode> {
    private Neo4jRepository<T, String> repository;

    public MiddlewareToken(Neo4jRepository<T, String> repository) {
        this.repository = repository;
    }

    public T getUserFromToken(String token){
        String filtered = token.substring("Bearer ".length());
        JWTVerifier verifier = JWT.require(TokenManager.getInstance().getAccessAlgorithm()).build();
        DecodedJWT decodedJWT = verifier.verify(token);
        String username = decodedJWT.getSubject();
        T user = repository.findAll().stream().filter(u -> u.getUsername().equals(username)).findFirst()
                .orElseThrow(() -> new UsernameNotFoundException("Username not found"));
        log.info("user with username : {} found in token",user.getUsername());
        return user;
    }

    public T getUserFromToken(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        String token = authorizationHeader.substring("Bearer ".length());
        JWTVerifier verifier = JWT.require(TokenManager.getInstance().getAccessAlgorithm()).build();
        DecodedJWT decodedJWT = verifier.verify(token);
        String username = decodedJWT.getSubject();
        T user = repository.findAll().stream().filter(u -> u.getUsername().equals(username)).findFirst()
                .orElseThrow(() -> new UsernameNotFoundException("Username not found"));
        log.info("user with username : {} found in token",user.getUsername());
        return user;
    }
}
