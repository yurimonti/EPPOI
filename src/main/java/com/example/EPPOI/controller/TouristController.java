package com.example.EPPOI.controller;

import com.example.EPPOI.model.ItineraryNode;
import com.example.EPPOI.model.user.TouristNode;
import com.example.EPPOI.service.TouristService;
import com.example.EPPOI.utility.MiddlewareToken;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@CrossOrigin("*")
@RequestMapping("/tourist")
public class TouristController {
    private final TouristService touristService;

    private final MiddlewareToken<TouristNode> middlewareToken;

    public TouristController(TouristService touristService) {
        this.touristService = touristService;
        this.middlewareToken = new MiddlewareToken<>(touristService.getRepository());
    }

    @GetMapping
    public ResponseEntity<TouristNode> getUserInfo(HttpServletRequest request){
        TouristNode tourist = this.middlewareToken.getUserFromToken(request);
        return ResponseEntity.ok(tourist);
    }

    @GetMapping("/itinerary")
    public ResponseEntity<List<ItineraryNode>> createItinerary(HttpServletRequest request){
        TouristNode tourist = this.middlewareToken.getUserFromToken(request);
        return ResponseEntity.ok(tourist.getItineraries());
    }



}
