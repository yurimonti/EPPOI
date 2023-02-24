package com.example.EPPOI.controller;

import com.example.EPPOI.dto.*;
import com.example.EPPOI.model.*;
import com.example.EPPOI.model.poi.PoiNode;
import com.example.EPPOI.model.user.EnteNode;
import com.example.EPPOI.repository.ItineraryRequestRepository;
import com.example.EPPOI.repository.ThirdRequestRegistrationRepository;
import com.example.EPPOI.service.EnteService;
import com.example.EPPOI.service.PoiRequestService;
import com.example.EPPOI.service.PoiService;
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
import java.util.*;
import java.util.stream.Stream;

@Controller
@RequestMapping("/ente")
@CrossOrigin("*")
@Slf4j
public class EnteController {

    private final MiddlewareToken<EnteNode> middlewareToken;
    private final ItineraryRequestRepository itineraryRequestRepository;
    private final PoiService poiService;
    private final PoiRequestService poiRequestService;
    private final DtoEntityManager<ItineraryRequestNode, ItineraryRequestDTO> itineraryRequestDTOManager;
    private final DtoEntityManager<ItineraryNode, ItineraryDTO> itineraryDTOManager;
    private final EnteService enteService;

    public EnteController(EnteService enteService,ItineraryRequestRepository itineraryRequestRepository,
                          PoiRequestService poiRequestService, PoiService poiService,
                          DtoEntityManager<ItineraryRequestNode,ItineraryRequestDTO> itineraryRequestDTOManager,
                          DtoEntityManager<ItineraryNode, ItineraryDTO> itineraryDTOManager) {
        this.enteService = enteService;
        this.middlewareToken = new MiddlewareToken<>(enteService.getRepository());
        this.itineraryRequestRepository = itineraryRequestRepository;
        this.poiRequestService = poiRequestService;
        this.poiService = poiService;
        this.itineraryRequestDTOManager = itineraryRequestDTOManager;
        this.itineraryDTOManager = itineraryDTOManager;
    }

    @GetMapping
    public ResponseEntity<EnteNode> getUserInfo(HttpServletRequest request) {
        EnteNode ente = this.middlewareToken.getUserFromToken(request);
        return ResponseEntity.ok(ente);
    }

    @GetMapping("/city")
    public ResponseEntity<?> getCity(HttpServletRequest request) {
        EnteNode ente = this.middlewareToken.getUserFromToken(request);
        return ResponseEntity.ok(new CityDTO(ente.getCity()));
    }

    //------------------------- ITINERARY Req------------------------------------------------
    @GetMapping("/itinerary-request")
    public ResponseEntity<?> getItineraryRequest(HttpServletRequest request) {
        EnteNode ente = this.middlewareToken.getUserFromToken(request);
        List<ItineraryRequestDTO> requests = ente.getItineraryRequests().stream()
                .map(ItineraryRequestRel::getRequest)
                .filter(i -> !i.getAccepted() && i.getConsensus()>0)
                .map(this.itineraryRequestDTOManager::getDtofromEntity)
                .toList();
        return ResponseEntity.ok(requests);


        /*return ResponseEntity.ok(ente.getItineraryRequests().stream().map(ItineraryRequestRel::getRequest)
                .map(ItineraryRequestDTO::new).toList());*/
    }

    @PostMapping("/itinerary-request")
    public ResponseEntity<?> setConsensusToItinerary(HttpServletRequest req, @RequestBody Map<String, ?> body) {
        EnteNode ente = this.middlewareToken.getUserFromToken(req);
        Long idRequest = Long.parseLong((String) body.get("idRequest"));
        log.info("request id {}", idRequest);
        Boolean consensus = (Boolean) body.get("consensus");
        log.info("request consensus {}", consensus);
        if (ente.getItineraryRequests().stream()
                .map(ItineraryRequestRel::getRequest)
                .map(ItineraryRequestNode::getId)
                .noneMatch(l -> l.equals(idRequest)))
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        ItineraryRequestNode request = this.itineraryRequestRepository.findById(idRequest).get();
        this.enteService.setConsensusToItinerary(ente, request, consensus);
        return ResponseEntity.ok().build();
    }

    //------------------------- ITINERARY ------------------------------------------------

    @GetMapping("/itinerary")
    public ResponseEntity<?> getItineraries(HttpServletRequest request, @RequestParam boolean details) {
        EnteNode ente = this.middlewareToken.getUserFromToken(request);
        Stream<ItineraryNode> stream = ente.getCity().getItineraries()
                .stream()
                .filter(ItineraryNode::getIsDefault);
        return details ? ResponseEntity.ok(stream.map(this.itineraryDTOManager::getDtofromEntity).toList())
                : ResponseEntity.ok(stream.map(ItineraryDTO::new).toList());
    }

    //FIXME:rivedere metodo
    @PostMapping("/itinerary")
    public ResponseEntity<?> createItinerary(@RequestBody ItineraryForm itineraryForm, HttpServletRequest request) {
        EnteNode ente = this.middlewareToken.getUserFromToken(request);
        CityNode city = ente.getCity();
        log.info("city ente {}", city.getName());
        log.info("form  {}", itineraryForm);
        boolean condition = city.getPOIs().stream().map(PoiNode::getId).toList().containsAll(itineraryForm.getPoisId());
        log.info("condizione {}", condition);
        if (condition) {
            return ResponseEntity.ok(new ItineraryDTO(this.enteService.createItinerary(ente, itineraryForm)));
        } else {
            ItineraryRequestNode result = this.enteService.createItineraryRequest(ente, itineraryForm);
            log.info("request id {}", result.getId());
            return ResponseEntity.ok(new ItineraryRequestDTO(result));
        }
    }

    @DeleteMapping("/itinerary/{id}")
    public ResponseEntity<?> deleteItinerary(HttpServletRequest req, @PathVariable String id) {
        EnteNode ente = this.middlewareToken.getUserFromToken(req);
        Long itId = Long.parseLong(id);
        try {
            this.enteService.deleteItinerary(ente, itId);
        } catch (Exception e) {
            if (Objects.equals(e.getClass(), NullPointerException.class))
                return ResponseEntity.notFound().build();
            else
                return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().build();
    }

    //------------------------- POIs ------------------------------------------------

    @GetMapping("/pois")
    public ResponseEntity<?> getPOIs(HttpServletRequest request) {
        EnteNode ente = this.middlewareToken.getUserFromToken(request);
        return ResponseEntity.ok(ente.getCity().getPOIs().stream().map(this.poiService::createDTOfromNode).toList());
    }

    @PatchMapping("/pois")
    public ResponseEntity<?> modifyPoi(@RequestBody PoiForm form, HttpServletRequest request) {
        log.info("form : {}", form);
        EnteNode ente = this.middlewareToken.getUserFromToken(request);
        PoiNode poi;
        try {
            poi = this.enteService.modifyPoi(ente, form, form.getIdPoi());
        } catch (Exception e) {
            log.error(e.getClass() + " " + e.getMessage());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.ok(new PoiDTO(poi));
    }

    @PostMapping("/pois")
    public ResponseEntity<?> createPoi(@RequestBody PoiForm form, HttpServletRequest request) {
        EnteNode ente = this.middlewareToken.getUserFromToken(request);
        PoiNode poi = this.enteService.createPoi(ente, form);
        return ResponseEntity.ok(new PoiDTO(poi));
    }

    @DeleteMapping("/pois/{id}")
    public ResponseEntity<?> deletePoi(@PathVariable String id, HttpServletRequest request) {
        EnteNode ente = this.middlewareToken.getUserFromToken(request);
        Long idPoi = Long.parseLong(id);
        try {
            this.enteService.deletePoi(ente, idPoi);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (Exception e) {
            if (Objects.equals(e.getClass(), NullPointerException.class))
                return ResponseEntity.notFound().build();
            return ResponseEntity.badRequest().build();
        }
    }

    //------------------------- POIs REQUESTS ------------------------------------------------
    @GetMapping("/poi-requests")
    public ResponseEntity<?> getPoiRequests(HttpServletRequest request) {
        EnteNode ente = this.middlewareToken.getUserFromToken(request);
        return ResponseEntity.ok(ente.getPoiRequests().stream().map(this.poiRequestService::getDTOfromRequest).toList());
    }

    @GetMapping("/poi-requests/{id}")
    public ResponseEntity<?> getPoiRequest(HttpServletRequest request,@PathVariable String id) {
        EnteNode ente = this.middlewareToken.getUserFromToken(request);
        Long idRequest = Long.parseLong(id);
        Map<String, String> errors = new HashMap<>();
        try {
            this.poiRequestService.getRequestById(idRequest);
            if(ente.getPoiRequests().stream().noneMatch(r -> r.getId().equals(idRequest))){
                errors.put("error","Non puoi gestire questa richiesta");
                return new ResponseEntity<>(errors,HttpStatus.FORBIDDEN);
            }
            return ResponseEntity.ok(
                    this.poiRequestService.getDTOfromRequest(this.poiRequestService.getRequestById(idRequest)));
        }catch(Exception e){
            if (Objects.equals(e.getClass(), NullPointerException.class)) {
                errors.put("error",e.getMessage());
                return new ResponseEntity<>(errors,HttpStatus.NOT_FOUND);
            }
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/poi-requests")
    public ResponseEntity<?> acceptPoiRequests(HttpServletRequest request, @RequestBody Map<String, ?> body) {
        EnteNode ente = this.middlewareToken.getUserFromToken(request);
        boolean consensus = (boolean) body.get("consensus");
        Long idRequest = Long.parseLong((String) body.get("idRequest"));
        this.enteService.setConsensusToPoiRequest(ente, idRequest, consensus);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/poi-requests/{id}")
    public ResponseEntity<?> modifyRequest(HttpServletRequest request,@PathVariable String id,
                                               @RequestBody PoiForm form) {
        EnteNode ente = this.middlewareToken.getUserFromToken(request);
        Long idRequest = Long.parseLong(id);
        try{
            this.enteService.modifyPoiRequest(ente, idRequest, form);
            return ResponseEntity.ok().build();
        }catch(Exception e){
            if (Objects.equals(e.getClass(), NullPointerException.class)) {
                Map<String, String> errors = new HashMap<>();
                errors.put("error",e.getMessage());
                return new ResponseEntity<>(errors,HttpStatus.NOT_FOUND);
            }
            return ResponseEntity.badRequest().build();
        }
    }

}
