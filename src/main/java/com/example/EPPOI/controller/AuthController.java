package com.example.EPPOI.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.example.EPPOI.model.CityNode;
import com.example.EPPOI.model.ItineraryNode;
import com.example.EPPOI.model.ThirdPartyRegistrationRel;
import com.example.EPPOI.model.ThirdPartyRegistrationRequest;
import com.example.EPPOI.model.poi.PoiNode;
import com.example.EPPOI.model.user.*;
import com.example.EPPOI.repository.*;
import com.example.EPPOI.security.CustomAuthenticationFilter;
import com.example.EPPOI.security.JwtTokenProvider;
import com.example.EPPOI.security.TokenManager;
import com.example.EPPOI.service.EnteService;
import com.example.EPPOI.service.GeneralUserService;
import com.example.EPPOI.service.PoiService;
import com.example.EPPOI.service.TouristService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
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

    //TODO: verificare
    private final ThirdRequestRegistrationRepository thirdRequestRegistrationRepository;

    private final EnteRepository enteRepository;

    private final TouristService touristService;

    private final UserNodeRepository userNodeRepository;

    private final PoiService poiService;
    @Data
    private static class UserBody{
        private String username;
        private String password;
    }


    //TODO: delete this method
    private List<PoiNode> idsToPois(List<Long> ids){
        List<PoiNode> result = new ArrayList<>();
        for(Long id : ids){
            result.add(this.poiService.findPoiById(id));
        }
        return result;
    }
    //TODO: delete this method
    @GetMapping("/prova2")
    public ResponseEntity<?> getUserInfo(@RequestParam String username){
        return ResponseEntity.ok(this.userNodeRepository.findByUsername(username));
    }
    //TODO: delete this method
    @PostMapping("/itinerary")
    public ResponseEntity<?> prova(@RequestBody Map<String,?> body){
        String username = (String)body.get("username");
        TouristNode tourist;
        try{
            tourist =  this.touristService.getUserByUsername(username);
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        String name = (String)body.get("name");
        String description = (String)body.get("description");
        List<Long> ids = new ArrayList<>();
        List<String> s = (List<String>) body.get("ids");
        s.forEach(i -> ids.add(Long.parseLong(i)));
        ItineraryNode result = this.touristService.createItinerary(tourist,name, description, this.idsToPois(ids));
        return ResponseEntity.ok(result);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserBody body) {
        log.info("Body {}",body);
        Map<String,String> tokens;
        try {
            tokens = this.customAuthenticationFilter.authenticate(body.getUsername(), body.getPassword(), "http://localhost:8080/api/v1/auth/login");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getStackTrace());
        }
        return ResponseEntity.ok(tokens);
    }

    @PostMapping("/registration")
    public ResponseEntity<?> signup(@RequestBody Map<String,Object> body) {
        String username = (String)body.get("username");
        String password = (String)body.get("password");
        String email = (String)body.get("email");
        String name = (String)body.get("name");
        String surname = (String)body.get("surname");
        UserRoleNode role = this.userRoleRepository.findByName("TOURIST");
        UserNode user = new TouristNode(name, surname, email, password, username,role);
        this.userService.saveUser(user);
        log.info("user registered {}",user);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/registration-ente")
    public ResponseEntity<?> signupEnte(@RequestBody Map<String,Object> body) {
        String username = (String)body.get("username");
        String password = (String)body.get("password");
        String email = (String)body.get("email");
        String name = (String)body.get("name");
        String surname = (String)body.get("surname");
        String cityName = (String) body.get("cityName");
        UserRoleNode role = this.userRoleRepository.findByName("ENTE");
        CityNode cityNode = this.cityRepository.findAll().stream().filter(c -> c.getName().equals(cityName))
                .findFirst().orElseThrow(()-> new NullPointerException("No such city"));
        UserNode user = new EnteNode(name, surname, email, password, username,cityNode,role);
        this.userService.saveUser(user);
        return ResponseEntity.ok().build();
    }

    //TODO: there is a problem that not allows to an ente to login
    @PostMapping("/registration-third")
    public ResponseEntity<?> signupThird(@RequestBody Map<String,Object> body) {
        String username = (String)body.get("username");
        String password = (String)body.get("password");
        String email = (String)body.get("email");
        String name = (String)body.get("name");
        String surname = (String)body.get("surname");
        List<String> cityNames = (List<String>) body.get("cityNames");
        List<EnteNode> entes = new ArrayList<>();
        cityNames.forEach(cityName -> entes.addAll(this.enteRepository.findAll().stream().filter(e ->
                e.getCity().getName().equals(cityName)).toList()));
        ThirdPartyRegistrationRequest result =
                new ThirdPartyRegistrationRequest(name, surname, email, password, username,entes.size());
        this.thirdRequestRegistrationRepository.save(result);
        entes.forEach(enteNode -> {
            enteNode.getRegistrationRequests().add(new ThirdPartyRegistrationRel(result, false));
            this.enteRepository.save(enteNode);
        });
        /*UserNode user = new ThirdUserNode(name, surname, email, password, username, role);
        this.userService.saveUser(user);*/
        return ResponseEntity.ok(result);
    }

    @PostMapping("/refresh")
    public void refresh(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authorizationHeader = request.getHeader("Authorization");
        if(authorizationHeader!=null) {
            try{
                String refresh_token = authorizationHeader.substring("Bearer ".length());
                TokenManager tokenManager = TokenManager.getInstance();
                JWTVerifier verifier = JWT.require(tokenManager.getAccessAlgorithm()).build();
                DecodedJWT decodedJWT = verifier.verify(refresh_token);
                String username = decodedJWT.getSubject();
                UserNode user = this.userService.getUser(username);
                String access_token = JWT.create().withSubject(user.getUsername())
                        .withExpiresAt(new Date(System.currentTimeMillis()*1000*60*5))
                        .withIssuer("http://localhost:8080/api/v1/auth/refresh")
                        .withClaim("role",user.getRoles().stream().map(UserRoleNode::getName).collect(Collectors.toList()))
                        .sign(tokenManager.getAccessAlgorithm());
                Map<String,String> tokens = new HashMap<>();
                tokens.put("access_token",access_token);
                tokens.put("refresh_token",refresh_token);
                response.setContentType("application/json");
                new ObjectMapper().writeValue(response.getOutputStream(),tokens);
            }catch(Exception exception){
                response.setHeader("error", exception.getMessage());
                response.setStatus(HttpStatus.FORBIDDEN.value());
                Map<String,String> errors = new HashMap<>();
                errors.put("error", exception.getMessage());
                response.setContentType("application/json");
                new ObjectMapper().writeValue(response.getOutputStream(),errors);
            }
        }else{
            throw new RuntimeException("RefreshToken is invalid");
        }
    }

}
