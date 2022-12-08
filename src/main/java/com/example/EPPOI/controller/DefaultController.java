package com.example.EPPOI.controller;

import com.example.EPPOI.dto.CityDTO;
import com.example.EPPOI.dto.PoiDTO;
import com.example.EPPOI.model.CityNode;
import com.example.EPPOI.repository.CityRepository;
import com.example.EPPOI.repository.PoiRepository;
import com.example.EPPOI.service.CityService;
import com.example.EPPOI.service.PoiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequiredArgsConstructor
public class DefaultController {
    private final PoiService poiService;
    private final CityService cityService;

    @GetMapping("/")
    public ResponseEntity<String> hello(){
        return ResponseEntity.ok("Hello");
    }

    @GetMapping("/pois")
    public ResponseEntity<List<PoiDTO>> getPois(){
        return ResponseEntity.ok(this.poiService.getAllPois().stream().map(PoiDTO::new).toList());
    }

    @GetMapping("/cities")
    public ResponseEntity<List<CityDTO>> getCities(){
        return ResponseEntity.ok(this.cityService.getAllCities().stream().map(CityDTO::new).toList());
    }

}
