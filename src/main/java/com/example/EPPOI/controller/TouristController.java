package com.example.EPPOI.controller;

import com.example.EPPOI.dto.ItineraryDTO;
import com.example.EPPOI.dto.PoiDTO;
import com.example.EPPOI.model.CityNode;
import com.example.EPPOI.model.ItineraryNode;
import com.example.EPPOI.model.RequestPoiNode;
import com.example.EPPOI.model.poi.PoiNode;
import com.example.EPPOI.model.user.TouristNode;
import com.example.EPPOI.service.CityService;
import com.example.EPPOI.service.PoiService;
import com.example.EPPOI.service.TouristService;
import com.example.EPPOI.service.dtoManager.DtoEntityManager;
import com.example.EPPOI.utility.ItineraryForm;
import com.example.EPPOI.utility.MiddlewareToken;
import com.example.EPPOI.utility.PoiForm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Controller
@CrossOrigin("*")
@RequestMapping("/tourist")
@Slf4j
public class TouristController {
    private final TouristService touristService;
    private final PoiService poiService;

    private final CityService cityService;
    private final DtoEntityManager<ItineraryNode, ItineraryDTO> itineraryDTOManager;


    private final MiddlewareToken<TouristNode> middlewareToken;

    public TouristController(TouristService touristService, PoiService poiService, CityService cityService,
                             DtoEntityManager<ItineraryNode, ItineraryDTO> itineraryDTOManager) {
        this.touristService = touristService;
        this.poiService = poiService;
        this.cityService = cityService;
        this.itineraryDTOManager = itineraryDTOManager;
        this.middlewareToken = new MiddlewareToken<>(touristService.getRepository());
    }

    @GetMapping
    public ResponseEntity<TouristNode> getUserInfo(HttpServletRequest request) {
        TouristNode tourist = this.middlewareToken.getUserFromToken(request);
        return ResponseEntity.ok(tourist);
    }

    //------------------------------ Pois -----------------------------

    //-----------------------Poi Request -----------------------------------------------------

    @GetMapping("/poi-requests")
    public ResponseEntity<?> getRequests(HttpServletRequest request) {
        TouristNode tourist = this.middlewareToken.getUserFromToken(request);
        return ResponseEntity.ok(this.touristService.getAllRequestDTOs(tourist));
    }

    @PostMapping("/poi-requests")
    public ResponseEntity<?> createPoiRequest(HttpServletRequest request, @RequestBody PoiForm form, @RequestParam Long cityId) {
        TouristNode tourist = this.middlewareToken.getUserFromToken(request);
        RequestPoiNode result;
        result = this.touristService.createRequestPoi(tourist, form, cityId);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/poi-requests/{id}")
    public ResponseEntity<?> deletePoiRequest(HttpServletRequest request, @PathVariable String id) {
        TouristNode tourist = this.middlewareToken.getUserFromToken(request);
        Long idRequest = Long.parseLong(id);
        Map<String, String> message = new HashMap<>();
        HttpStatus status;
        try {
            this.touristService.deletePoiRequest(tourist, idRequest);
            message.put("success", "Richiesta eliminata");
            status = HttpStatus.OK;
        } catch (Exception e) {
            if (Objects.equals(e.getClass(), NullPointerException.class))
                status = HttpStatus.NOT_FOUND;
            else status = HttpStatus.BAD_REQUEST;
            message.put("error", "Errore Elaborazione");
        }
        return new ResponseEntity<>(message, status);
    }

    //-------------------------- ITINERARIES ------------------------

    @GetMapping("/itinerary")
    public ResponseEntity<List<ItineraryDTO>> getItineraries(HttpServletRequest request) {
        TouristNode tourist = this.middlewareToken.getUserFromToken(request);
        return ResponseEntity.ok(tourist.getItineraries().stream().map(this.itineraryDTOManager::getDtofromEntity).toList());
    }

    @DeleteMapping("/itinerary/{id}")
    public ResponseEntity<?> deleteItinerary(HttpServletRequest request, @PathVariable String id) {
        TouristNode tourist = this.middlewareToken.getUserFromToken(request);
        Map<String, String> message = new HashMap<>();
        HttpStatus status;
        try {
            this.touristService.deleteItinerary(tourist, Long.parseLong(id));
            message.put("success", "Itinerario eliminato");
            status = HttpStatus.OK;
        } catch (Exception e) {
            if (Objects.equals(e.getClass(), NullPointerException.class))
                status = HttpStatus.NOT_FOUND;
            else status = HttpStatus.BAD_REQUEST;
            message.put("error", "Errore Elaborazione");
        }
        return new ResponseEntity<>(message, status);
    }

    @GetMapping("/city/{id}/itineraries")
    public ResponseEntity<List<ItineraryDTO>> getItineraries(HttpServletRequest request, @PathVariable String id) {
        TouristNode tourist = this.middlewareToken.getUserFromToken(request);
        Long cityId = Long.parseLong(id);
        try {
            CityNode cityNode = this.cityService.getCityById(cityId);
            return ResponseEntity.ok(cityNode.getItineraries().stream().filter(ItineraryNode::getIsDefault).map(ItineraryDTO::new).toList());
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/itinerary")
    public ResponseEntity<ItineraryDTO> createItinerary(HttpServletRequest request,
                                                        @RequestBody ItineraryForm itineraryForm) {
        TouristNode tourist = this.middlewareToken.getUserFromToken(request);
        ItineraryNode itinerary = this.touristService.createItinerary(tourist, itineraryForm);
        return ResponseEntity.ok(new ItineraryDTO(itinerary));
    }

    @PostMapping("/itinerary/{id}")
    public ResponseEntity<?> proposeItinerary(HttpServletRequest request, @PathVariable String id) {
        TouristNode tourist = this.middlewareToken.getUserFromToken(request);
        try {
            return ResponseEntity.ok(this.touristService.proposeItinerary(tourist, Long.parseLong(id)));
        } catch (Exception e) {
            if (Objects.equals(e.getClass(), NullPointerException.class)) {
                Map<String, String> response = new HashMap<>();
                response.put("error", e.getMessage());
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
            return ResponseEntity.badRequest().build();
        }
    }


}
