package com.example.EPPOI.controller;

import com.example.EPPOI.dto.PoiDTO;
import com.example.EPPOI.model.ItineraryNode;
import com.example.EPPOI.model.poi.PoiNode;
import com.example.EPPOI.model.RequestPoiNode;
import com.example.EPPOI.model.user.TouristNode;
import com.example.EPPOI.model.user.UserNode;
import com.example.EPPOI.repository.CityRepository;
import com.example.EPPOI.repository.UserNodeRepository;
import com.example.EPPOI.service.*;
import com.example.EPPOI.utility.PoiParamsProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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
    private final TouristService touristService;
    private final PoiService poiService;

    private final CityRepository cityRepository;

    private final UserNodeRepository userNodeRepository;

    @GetMapping("/pois")
    public ResponseEntity<List<PoiDTO>> getPois(){
        return ResponseEntity.ok(this.poiService.getAllPois().stream().map(PoiDTO::new).toList());
    }

    @GetMapping("/pois/{id}")
    public ResponseEntity<PoiDTO> getPoi(@PathVariable String id){
        log.info("start");
        PoiNode poi;
        try{
            poi = this.poiService.findPoiById(Long.parseLong(id));
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok(new PoiDTO(poi));
    }
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
}