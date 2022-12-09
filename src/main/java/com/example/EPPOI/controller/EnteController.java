package com.example.EPPOI.controller;

import com.example.EPPOI.dto.PoiDTO;
import com.example.EPPOI.model.CityNode;
import com.example.EPPOI.model.ItineraryRequestNode;
import com.example.EPPOI.model.ItineraryRequestRel;
import com.example.EPPOI.model.ThirdPartyRegistrationRequest;
import com.example.EPPOI.model.poi.PoiNode;
import com.example.EPPOI.model.user.EnteNode;
import com.example.EPPOI.repository.ItineraryRequestRepository;
import com.example.EPPOI.repository.ThirdRequestRegistrationRepository;
import com.example.EPPOI.service.EnteService;
import com.example.EPPOI.utility.ItineraryForm;
import com.example.EPPOI.utility.MiddlewareToken;
import com.example.EPPOI.utility.PoiForm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.Map;

@Controller
@RequestMapping("/ente")
@CrossOrigin("*")
@Slf4j
public class EnteController {

    private final MiddlewareToken<EnteNode> middlewareToken;

    private final ThirdRequestRegistrationRepository thirdRequestRegistrationRepository;
    private final ItineraryRequestRepository itineraryRequestRepository;
    private EnteService enteService;

    public EnteController(EnteService enteService, ThirdRequestRegistrationRepository thirdRequestRegistrationRepository,
                          ItineraryRequestRepository itineraryRequestRepository) {
        this.enteService = enteService;
        this.thirdRequestRegistrationRepository = thirdRequestRegistrationRepository;
        this.middlewareToken = new MiddlewareToken<>(enteService.getRepository());
        this.itineraryRequestRepository = itineraryRequestRepository;
    }

    @GetMapping
    public ResponseEntity<EnteNode> getUserInfo(HttpServletRequest request){
        EnteNode ente = this.middlewareToken.getUserFromToken(request);
        return ResponseEntity.ok(ente);
    }
    @GetMapping("/city")
    public ResponseEntity<CityNode> getCity(HttpServletRequest request){
        EnteNode ente = this.middlewareToken.getUserFromToken(request);
        return ResponseEntity.ok(ente.getCity());
    }
    //------------------------- ITINERARY Req------------------------------------------------
    @GetMapping("/itinerary-request")
    public ResponseEntity<?> createItinerary(HttpServletRequest request){
        EnteNode ente = this.middlewareToken.getUserFromToken(request);
        return ResponseEntity.ok(ente.getItineraryRequests());
    }

    @PostMapping("/itinerary-request")
    public ResponseEntity<?> setConsensusToItinerary(HttpServletRequest req,@RequestBody Map<String,?> body){
        EnteNode ente = this.middlewareToken.getUserFromToken(req);
        Long idRequest = Long.parseLong((String)body.get("idRequest"));
        log.info("request id {}",idRequest);
        Boolean consensus = (Boolean) body.get("consensus");
        log.info("request consensus {}",consensus);
        if(ente.getItineraryRequests().stream()
                .map(ItineraryRequestRel::getRequest)
                .map(ItineraryRequestNode::getId)
                .noneMatch(l -> l.equals(idRequest)))
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        ItineraryRequestNode request = this.itineraryRequestRepository.findById(idRequest).get();
        this.enteService.setConsensusToItinerary(ente,request,consensus);
        return ResponseEntity.ok().build();
    }

        //------------------------- ITINERARY ------------------------------------------------

    @GetMapping("/itinerary")
    public ResponseEntity<?> getItineraries(HttpServletRequest request){
        EnteNode ente = this.middlewareToken.getUserFromToken(request);
        return ResponseEntity.ok(ente.getCity().getItineraries());
    }

    @PostMapping("/itinerary")
    public ResponseEntity<?> createItinerary(@RequestBody ItineraryForm itineraryForm, HttpServletRequest request){
        EnteNode ente = this.middlewareToken.getUserFromToken(request);
        if(new HashSet<>(ente.getCity().getPOIs().stream().map(PoiNode::getId).toList())
                .containsAll(itineraryForm.getPOIsId())){
            return ResponseEntity.ok(this.enteService.createItinerary(ente,itineraryForm));
        } else return ResponseEntity.ok(this.enteService.createItineraryRequest(ente,itineraryForm));
    }

    //------------------------- POIs ------------------------------------------------

    @GetMapping("/pois")
    public ResponseEntity<?> getPOIs(HttpServletRequest request){
        EnteNode ente = this.middlewareToken.getUserFromToken(request);
        return ResponseEntity.ok(ente.getCity().getPOIs().stream().map(PoiDTO::new).toList());
    }

    @PatchMapping("/pois")
    public ResponseEntity<?> modifyPoi(@RequestBody PoiForm form,HttpServletRequest request){
        log.info("form : {}",form);
        EnteNode ente = this.middlewareToken.getUserFromToken(request);
        PoiNode poi = this.enteService.modifyPoi(ente,form,form.getIdPoi());
        return ResponseEntity.ok(new PoiDTO(poi));
    }

    @PostMapping("/pois")
    public ResponseEntity<?> createPoi(@RequestBody PoiForm form,HttpServletRequest request){
        EnteNode ente = this.middlewareToken.getUserFromToken(request);
        PoiNode poi = this.enteService.createPoi(ente,form);
        return ResponseEntity.ok(new PoiDTO(poi));
    }

    //------------------------- THIRD  ------------------------------------------------

    @PostMapping("/third-registration")
    public ResponseEntity<?> singUpThird(@RequestParam Boolean consensus,@RequestBody Map<String,Object> body,
                                         HttpServletRequest request){
        EnteNode ente = this.middlewareToken.getUserFromToken(request);
        ThirdPartyRegistrationRequest requestBody = this.thirdRequestRegistrationRepository
                .findById(Long.parseLong((String) body.get("request"))).orElseThrow(NullPointerException::new);
        log.info("ThirdPartyRegistration {}",requestBody);
        this.enteService.setConsensusToRegistration(ente,requestBody,consensus);
        log.info("POST method -> ThirdPartyRegistration {}",requestBody);
        return ResponseEntity.ok().build();
    }
}
