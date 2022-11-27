package com.example.EPPOI.controller;

import com.example.EPPOI.service.TouristServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*")
@RestController
@RequiredArgsConstructor
public class DefaultController {


    @GetMapping("/")
    public ResponseEntity<String> hello(){
        return ResponseEntity.ok("Hello");
    }



}
