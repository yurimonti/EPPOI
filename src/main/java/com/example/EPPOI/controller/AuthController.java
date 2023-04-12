package com.example.EPPOI.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.example.EPPOI.model.CityNode;
import com.example.EPPOI.model.CoordsNode;
import com.example.EPPOI.model.user.*;
import com.example.EPPOI.repository.*;
import com.example.EPPOI.security.CustomAuthenticationFilter;
import com.example.EPPOI.security.TokenCycleService;
import com.example.EPPOI.security.TokenManager;
import com.example.EPPOI.service.GeneralUserService;
import com.example.EPPOI.utility.ErrorsMap;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
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

    private final EnteRepository enteRepository;

    private final CoordsRepository coordinatesRepository;

    private final TokenCycleService tokenCycleService;

    @Data
    private static class LoginUserBody {
        private String username;
        private String password;
    }

    @Data
    @EqualsAndHashCode(callSuper = false)
    private static class RegistrationUserBody extends LoginUserBody {
        private String name;
        private String surname;
        private String email;
    }

    @Data
    @EqualsAndHashCode(callSuper = false)
    private static class RegistrationEnteBody extends RegistrationUserBody {
        private Long cityId;
    }

    @Data
    private static class ORSCity {
        private List<String> identifiers;
        private Double lat;
        private Double lon;
        private String name;
    }

    @Data
    @EqualsAndHashCode(callSuper = false)
    private static class RegistrationEnteJson extends RegistrationUserBody {
        private ORSCity city;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginUserBody body) {
        log.info("Body {}", body);
        Map<String, String> tokens;
        try {
            tokens = this.customAuthenticationFilter.authenticate(body.getUsername(), body.getPassword(), "http://localhost:8080/api/v1/auth/login");
            log.info("Body {}", body);
            this.tokenCycleService.addToken(tokens.get("refresh_token"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getStackTrace());
        }
        return ResponseEntity.ok(tokens);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody Map<String,String> body){
        String refreshToken = body.get("refresh_token");
        if(refreshToken == null) return new ResponseEntity<>(new ErrorsMap("token is required"),HttpStatus.FORBIDDEN);
        this.tokenCycleService.removeToken(refreshToken);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    private boolean verifyCondition(String email, String username) {
        List<UserNode> users = this.userService.getUsers();
        return users.stream().anyMatch(u -> u.getUsername().equals(username) || u.getEmail().equals(email));
    }

    @PostMapping("/registration")
    public ResponseEntity<?> signup(@RequestBody RegistrationUserBody body) {
        UserRoleNode role = this.userRoleRepository.findByName("TOURIST");
        if (this.verifyCondition(body.getEmail(), body.getUsername())) return new ResponseEntity<>(
                new ErrorsMap("esiste un utente con queste credenziali"), HttpStatus.NOT_ACCEPTABLE);
        UserNode user = new TouristNode(body.getName(), body.getSurname(), body.getEmail(), body.getPassword(),
                body.getUsername(), role);
        this.userService.saveUser(user);
        log.info("user registered {}", user);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/registration-ente")
    public ResponseEntity<?> signupEnte(@RequestBody RegistrationEnteBody body) {
        if (this.verifyCondition(body.getEmail(), body.getUsername())) return new ResponseEntity<>(
                new ErrorsMap("esiste un utente con queste credenziali"), HttpStatus.NOT_ACCEPTABLE);
        UserRoleNode role = this.userRoleRepository.findByName("ENTE");
        CityNode cityNode = this.cityRepository.findById(body.getCityId())
                .orElseThrow(() -> new NullPointerException("No such city"));
        UserNode user = new EnteNode(body.getUsername(), body.getSurname(), body.getEmail(), body.getPassword(),
                body.getUsername(), cityNode, role);
        this.userService.saveUser(user);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/ente/registration")
    public ResponseEntity<?> signEnte(@RequestBody RegistrationEnteJson body) {
        if (Objects.isNull(body.getCity())) return new ResponseEntity<>(
                new ErrorsMap("manca la città nelle credenziali"), HttpStatus.NOT_ACCEPTABLE);
        List<Long> identifiers = new ArrayList<>();
        body.getCity().getIdentifiers().forEach(i -> {
            String toAdd = i.contains(":") ? i.substring(i.lastIndexOf(":") + 1, i.length() - 1) : i;
            identifiers.add(Long.parseLong(toAdd));
        });
        boolean cityIsPresent = false;
        Long cityId = null;
        for (Long id : identifiers) {
            if (this.cityRepository.findAll().stream().anyMatch(i -> i.getIdentifiers().contains(id))) {
                cityId = this.cityRepository.findAll().stream().filter(i -> i.getIdentifiers().contains(id)).map(i -> i.getId()).findFirst().get();
                cityIsPresent = true;
            }
        }
        log.info("City Found: " + cityIsPresent);
        if (this.verifyCondition(body.getEmail(), body.getUsername())) return new ResponseEntity<>(
                new ErrorsMap("esiste un utente con queste credenziali"), HttpStatus.NOT_ACCEPTABLE);
        UserRoleNode role = this.userRoleRepository.findByName("ENTE");
        CityNode city;
        if (!cityIsPresent) {
            CoordsNode cityCoords = new CoordsNode(body.getCity().lat, body.getCity().getLon());
            this.coordinatesRepository.save(cityCoords);
            city = new CityNode(body.getCity().getName(), cityCoords);
            city.setIdentifiers(identifiers);
            this.cityRepository.save(city);
        } else {
            city = this.cityRepository.findById(cityId).get();
        }
        log.info("This is city {}", city.getName());
        UserNode user = new EnteNode(body.getName(), body.getSurname(), body.getEmail(), body.getPassword(),
                body.getUsername(), city, role);
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

    @PostMapping("/real_refresh")
    public void realRefresh(HttpServletRequest request, HttpServletResponse response, @RequestBody Map<String, String> body) throws IOException {
        String refresh_token = body.get("refresh_token");
        if(Objects.isNull(refresh_token) || !tokenCycleService.containsToken(refresh_token)){
            response.setContentType("application/json");
            response.setHeader("error", "refresh token not valid");
            response.setStatus(HttpStatus.FORBIDDEN.value());
            Map<String, String> errors = new HashMap<>();
            errors.put("error", "refresh token not valid");
            new ObjectMapper().writeValue(response.getOutputStream(), errors);
        }else {
            try {
                TokenManager tokenManager = TokenManager.getInstance();
                JWTVerifier verifier = JWT.require(tokenManager.getRefreshAlgorithm()).build();
                DecodedJWT decodedJWT = verifier.verify(refresh_token);
                if (decodedJWT.getExpiresAt().before(new Date())) {
                    response.setContentType("application/json");
                    response.setHeader("error", "refresh token expired");
                    response.setStatus(HttpStatus.FORBIDDEN.value());
                }
                String username = decodedJWT.getSubject();
                UserNode user = this.userService.getUser(username);
                this.tokenCycleService.removeToken(refresh_token);
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
                this.tokenCycleService.addToken(new_refresh);
                response.setContentType("application/json");
                new ObjectMapper().writeValue(response.getOutputStream(), tokens);
            } catch (Exception exception) {
                response.setHeader("error", exception.getMessage());
                response.setStatus(HttpStatus.FORBIDDEN.value());
                this.tokenCycleService.removeToken(refresh_token);
                Map<String, String> errors = new HashMap<>();
                errors.put("error", exception.getMessage());
                response.setContentType("application/json");
                new ObjectMapper().writeValue(response.getOutputStream(), errors);
            }
        }
    }

    @PostMapping("/refresh")
    public void refresh(HttpServletRequest request, HttpServletResponse response, @RequestBody Map<String, String> body) throws IOException {
        try {
            String refresh_token = body.get("refresh_token");
            TokenManager tokenManager = TokenManager.getInstance();
            JWTVerifier verifier = JWT.require(tokenManager.getRefreshAlgorithm()).build();
            DecodedJWT decodedJWT = verifier.verify(refresh_token);
            if (decodedJWT.getExpiresAt().before(new Date())) {
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
