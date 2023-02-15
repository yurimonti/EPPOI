package com.example.EPPOI.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.example.EPPOI.model.CityNode;
import com.example.EPPOI.model.user.*;
import com.example.EPPOI.repository.*;
import com.example.EPPOI.security.CustomAuthenticationFilter;
import com.example.EPPOI.security.TokenManager;
import com.example.EPPOI.service.GeneralUserService;
import com.example.EPPOI.service.GeneralUserServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@CrossOrigin("*")
@RequiredArgsConstructor
@RequestMapping("/auth")
@Slf4j
public class AuthController {
    private final CustomAuthenticationFilter customAuthenticationFilter;
    private final GeneralUserService userService;
    private final UserRoleRepository userRoleRepository;
    private final CityRepository cityRepository;

    @Data
    private static class UserBody {
        private String username;
        private String password;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserBody body) {
        log.info("Body {}", body);
        Map<String, String> tokens;
        try {
            tokens = this.customAuthenticationFilter.authenticate(body.getUsername(), body.getPassword(), "http://localhost:8080/api/v1/auth/login");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getStackTrace());
        }
        return ResponseEntity.ok(tokens);
    }

    @PostMapping("/registration")
    public ResponseEntity<?> signup(@RequestBody Map<String, Object> body) {
        String username = (String) body.get("username");
        String password = (String) body.get("password");
        String email = (String) body.get("email");
        String name = (String) body.get("name");
        String surname = (String) body.get("surname");
        UserRoleNode role = this.userRoleRepository.findByName("TOURIST");
        UserNode user = new TouristNode(name, surname, email, password, username, role);
        this.userService.saveUser(user);
        log.info("user registered {}", user);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/registration-ente")
    public ResponseEntity<?> signupEnte(@RequestBody Map<String, Object> body) {
        String username = (String) body.get("username");
        String password = (String) body.get("password");
        String email = (String) body.get("email");
        String name = (String) body.get("name");
        String surname = (String) body.get("surname");
        Long cityId = Long.parseLong((String) body.get("cityId"));
        UserRoleNode role = this.userRoleRepository.findByName("ENTE");
        CityNode cityNode = this.cityRepository.findById(cityId)
                .orElseThrow(() -> new NullPointerException("No such city"));
        UserNode user = new EnteNode(name, surname, email, password, username, cityNode, role);
        this.userService.saveUser(user);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/registration-third")
    public ResponseEntity<?> signupThird(@RequestBody Map<String, Object> body) {
        String companyName = (String) body.get("companyName");
        String username = (String) body.get("username");
        String password = (String) body.get("password");
        String email = (String) body.get("email");
        String name = (String) body.get("name");
        String surname = (String) body.get("surname");
        UserRoleNode role = this.userRoleRepository.findByName("THIRD_PARTY");
        UserNode newUser = new ThirdUserNode(companyName, name, surname,email, password, username, role);
        this.userService.saveUser(newUser);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/refresh")
    public void refresh(HttpServletRequest request, HttpServletResponse response, @RequestBody Map<String, String> body) throws IOException {
        try {
            String refresh_token = body.get("refresh_token");
            TokenManager tokenManager = TokenManager.getInstance();
            JWTVerifier verifier = JWT.require(tokenManager.getRefreshAlgorithm()).build();
            DecodedJWT decodedJWT = verifier.verify(refresh_token);
            if(decodedJWT.getExpiresAt().before(new Date())){
                response.setContentType("application/json");
                response.setHeader("error", "refresh token expired");
                response.setStatus(HttpStatus.FORBIDDEN.value());
            }
            String username = decodedJWT.getSubject();
            UserNode user = this.userService.getUser(username);
            String access_token = JWT.create().withSubject(user.getUsername())
                    .withExpiresAt(new Date(System.currentTimeMillis() + 1000 * 60 * 5))
                    .withIssuer("http://localhost:8080/api/v1/auth/refresh")
                    .withClaim("role", user.getRoles().stream().map(UserRoleNode::getName).collect(Collectors.toList()))
                    .sign(tokenManager.getAccessAlgorithm());
            String new_refresh = JWT.create().withSubject(user.getUsername())
                    .withExpiresAt(new Date(System.currentTimeMillis() + 1000 * 3600 * 7 * 24))
                    .withIssuer("http://localhost:8080/api/v1/auth/refresh")
                    .withClaim("role", user.getRoles().stream().map(UserRoleNode::getName).collect(Collectors.toList()))
                    .sign(tokenManager.getRefreshAlgorithm());
            Map<String, String> tokens = new HashMap<>();
            tokens.put("access_token", access_token);
            tokens.put("refresh_token", new_refresh);
            response.setContentType("application/json");
            new ObjectMapper().writeValue(response.getOutputStream(), tokens);
        } catch (Exception exception) {
            response.setHeader("error", exception.getMessage());
            response.setStatus(HttpStatus.FORBIDDEN.value());
            Map<String, String> errors = new HashMap<>();
            errors.put("error", exception.getMessage());
            response.setContentType("application/json");
            new ObjectMapper().writeValue(response.getOutputStream(), errors);
        }
    }

}
