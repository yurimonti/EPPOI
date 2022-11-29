package com.example.EPPOI.controller;

import com.example.EPPOI.dto.CoordsDTO;
import com.example.EPPOI.model.ItineraryNode;
import com.example.EPPOI.model.PoiNode;
import com.example.EPPOI.model.RequestPoiNode;
import com.example.EPPOI.model.user.TouristNode;
import com.example.EPPOI.model.user.UserNode;
import com.example.EPPOI.repository.CityRepository;
import com.example.EPPOI.repository.UserNodeRepository;
import com.example.EPPOI.service.*;
import com.example.EPPOI.utility.PoiParamsProvider;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin("*")
@RequestMapping("/user")
@Slf4j
@RequiredArgsConstructor
public class UserController {
    private final TouristService userService;
    private final PoiService poiService;

    private final CityRepository cityRepository;

    private final UserNodeRepository userNodeRepository;

    private List<PoiNode> idsToPois(List<Long> ids){
        List<PoiNode> result = new ArrayList<>();
        for(Long id : ids){
            result.add(this.poiService.findPoiById(id));
        }
        return result;
    }

    @GetMapping
    public ResponseEntity<List<UserNode>> getAllUsers(){
        return ResponseEntity.ok(this.userNodeRepository.findAll());
    }

    @GetMapping("/pois")
    public ResponseEntity<List<PoiNode>> getPois(){
        return ResponseEntity.ok(this.poiService.getAllPois());
    }
    //FIXME: rivedere metodo
    @PostMapping("/poi_request")
    public ResponseEntity<RequestPoiNode> createRequestPoi(@RequestParam String username,
                                                           @RequestBody Map<String, Object> body){
        TouristNode tourist;
        try{
            tourist =  this.userService.getUserByUsername(username);
        }catch(Exception e){
            return ResponseEntity.badRequest().build();
        }
        log.info("tourist {}",tourist);
        RequestPoiNode result = this.userService.createRequestPoi(tourist,PoiParamsProvider.getFromBody(body),
                this.cityRepository.findById(Long.parseLong((String) body.get("city"))).get());
        return ResponseEntity.ok(result);
    }

    @PostMapping("/itinerary")
    public ResponseEntity<String> createItinerary(@RequestParam String username ,@RequestBody Map<String,Object> body) {
        TouristNode tourist;
        try{
            tourist =  this.userService.getUserByUsername(username);
        }catch(Exception e){
            return ResponseEntity.ok(e.getMessage());
        }
        log.info("tourist {}",tourist);
        String name = (String)body.get("name");
        String description = (String)body.get("description");
        List<Long> ids = new ArrayList<>();
        List<String> s = (List<String>) body.get("ids");
        s.forEach(i -> ids.add(Long.parseLong(i)));
        ItineraryNode result = this.userService.createItinerary(tourist,name, description, this.idsToPois(ids));
        return ResponseEntity.ok(result.toString());
    }
}