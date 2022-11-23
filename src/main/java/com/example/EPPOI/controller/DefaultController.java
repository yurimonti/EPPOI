package com.example.EPPOI.controller;

import com.example.EPPOI.model.CityNode;
import com.example.EPPOI.model.ItineraryNode;
import com.example.EPPOI.model.PoiNode;
import com.example.EPPOI.service.DefaultService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequiredArgsConstructor
public class DefaultController {

    @GetMapping("/")
    public ResponseEntity<String> hello(){
        return ResponseEntity.ok("Hello");
    }

}
