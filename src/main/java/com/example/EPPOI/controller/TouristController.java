package com.example.EPPOI.controller;

import com.example.EPPOI.model.ItineraryNode;
import com.example.EPPOI.model.RequestPoiNode;
import com.example.EPPOI.model.user.TouristNode;
import com.example.EPPOI.service.TouristService;
import com.example.EPPOI.utility.MiddlewareToken;
import com.example.EPPOI.utility.PoiForm;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;

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

    //-----------------------Poi Request -----------------------------------------------------
    @PostMapping("/poi-requests")
    public ResponseEntity<?> createPoiRequest(HttpServletRequest request, @RequestBody PoiForm form, @RequestParam Long cityId){
        TouristNode tourist = this.middlewareToken.getUserFromToken(request);
        RequestPoiNode result;
        result = this.touristService.createRequestPoi(tourist,form,cityId);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/itinerary")
    public ResponseEntity<List<ItineraryNode>> createItinerary(HttpServletRequest request){
        TouristNode tourist = this.middlewareToken.getUserFromToken(request);
        return ResponseEntity.ok(tourist.getItineraries());
    }



}
