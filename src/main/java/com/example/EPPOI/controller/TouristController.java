package com.example.EPPOI.controller;

import com.example.EPPOI.dto.ItineraryDTO;
import com.example.EPPOI.dto.PoiDTO;
import com.example.EPPOI.model.ItineraryNode;
import com.example.EPPOI.model.RequestPoiNode;
import com.example.EPPOI.model.poi.PoiNode;
import com.example.EPPOI.model.user.TouristNode;
import com.example.EPPOI.service.PoiService;
import com.example.EPPOI.service.TouristService;
import com.example.EPPOI.utility.ItineraryForm;
import com.example.EPPOI.utility.MiddlewareToken;
import com.example.EPPOI.utility.PoiForm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;

@Controller
@CrossOrigin("*")
@RequestMapping("/tourist")
@Slf4j
public class TouristController {
    private final TouristService touristService;
    private final PoiService poiService;

    private final MiddlewareToken<TouristNode> middlewareToken;

    public TouristController(TouristService touristService,PoiService poiService) {
        this.touristService = touristService;
        this.poiService = poiService;
        this.middlewareToken = new MiddlewareToken<>(touristService.getRepository());
    }

    @GetMapping
    public ResponseEntity<TouristNode> getUserInfo(HttpServletRequest request) {
        TouristNode tourist = this.middlewareToken.getUserFromToken(request);
        return ResponseEntity.ok(tourist);
    }

    //------------------------------ Pois -----------------------------

    //-----------------------Poi Request -----------------------------------------------------
    @PostMapping("/poi-requests")
    public ResponseEntity<?> createPoiRequest(HttpServletRequest request, @RequestBody PoiForm form, @RequestParam Long cityId) {
        TouristNode tourist = this.middlewareToken.getUserFromToken(request);
        RequestPoiNode result;
        result = this.touristService.createRequestPoi(tourist, form, cityId);
        return ResponseEntity.ok(result);
    }

    //-------------------------- ITINERARIES ------------------------

    @GetMapping("/itinerary")
    public ResponseEntity<List<ItineraryDTO>> getItineraries(HttpServletRequest request) {
        TouristNode tourist = this.middlewareToken.getUserFromToken(request);
        return ResponseEntity.ok(tourist.getItineraries().stream().map(ItineraryDTO::new).toList());
    }

    @PostMapping("/itinerary")
    public ResponseEntity<ItineraryDTO> createItinerary(HttpServletRequest request,
                                                        @RequestBody ItineraryForm itineraryForm) {
        TouristNode tourist = this.middlewareToken.getUserFromToken(request);
        ItineraryNode itinerary = this.touristService.createItinerary(tourist, itineraryForm);
        return ResponseEntity.ok(new ItineraryDTO(itinerary));
    }


}
