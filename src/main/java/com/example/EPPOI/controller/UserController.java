package com.example.EPPOI.controller;

import com.example.EPPOI.dto.*;
import com.example.EPPOI.model.*;
import com.example.EPPOI.model.poi.PoiNode;
import com.example.EPPOI.model.user.TouristNode;
import com.example.EPPOI.model.user.UserNode;
import com.example.EPPOI.repository.*;
import com.example.EPPOI.service.*;
import com.example.EPPOI.service.dtoManager.DtoEntityManager;
import com.example.EPPOI.utility.PoiParamsProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@CrossOrigin("*")
@RequestMapping("/user")
@Slf4j
@RequiredArgsConstructor
public class UserController {
    private final TouristService touristService;
    private final PoiService poiService;

    private final DtoEntityManager<PoiTypeNode,PoiTypeDTO> typeDtoManager;

    private final PoiTypeRepository poiTypeRepository;
    private final CategoryRepository categoryRepository;

    private final CityService cityService;

    private final UserNodeRepository userNodeRepository;

    private final ItineraryService itineraryService;

    private final DtoEntityManager<ItineraryNode, ItineraryDTO> itineraryDTOManager;
    private final DtoEntityManager<ItineraryRequestNode, ItineraryRequestDTO> itineraryRequestDTOManager;

    private final ItineraryRequestRepository itineraryRequestRepository;


    /*@GetMapping("/pois")
    public ResponseEntity<List<PoiDTO>> getPois(){
        return ResponseEntity.ok(this.poiService.getAllPois().stream().map(PoiDTO::new).toList());
    }*/

    @GetMapping("/city")
    public ResponseEntity<?> getCities(){
        return ResponseEntity.ok(this.cityService.getAllCities().stream().map(CityDTO::new).toList());
    }

    @GetMapping("/pois/{id}") //TODO controllare per poi terzi
    public ResponseEntity<PoiDTO> getPoi(@PathVariable String id){
        PoiNode poi;
        try{
            poi = this.poiService.findPoiById(Long.parseLong(id));
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok(this.poiService.createDTOfromNode(poi));
    }

    @GetMapping("/itinerary/{id}")
    public ResponseEntity<ItineraryDTO> getItinerary(@PathVariable String id){
        ItineraryNode result;
        try{
            result  = this.itineraryService.getItinerary(Long.parseLong(id));
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok(this.itineraryDTOManager.getDtofromEntity(result));
    }

    @GetMapping("/itinerary-request/{id}")
    public ResponseEntity<ItineraryRequestDTO> getItineraryRequest(@PathVariable String id){
        return this.itineraryRequestRepository.findById(Long.parseLong(id))
                .map(itineraryRequestNode ->
                        ResponseEntity.ok(this.itineraryRequestDTOManager.getDtofromEntity(itineraryRequestNode)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
    }

    @GetMapping("/categories")
    public ResponseEntity<List<CategoryDTO>> getCategories(){
        return ResponseEntity.ok(this.categoryRepository.findAll().stream().map(CategoryDTO::new).toList());
    }

    @PostMapping("/poiTypes")
    public ResponseEntity<List<PoiTypeDTO>> getPoiTypesFiltered(@RequestBody List<CategoryDTO> categories){
        List<PoiTypeNode> types;
        if(categories.size() >0)
            types = this.poiTypeRepository.findAll().stream()
                    .filter(t->t.getCategories().stream().map(CategoryNode::getId).toList()
                            .containsAll(categories.stream().map(CategoryDTO::getId).toList()))
                    .toList();
        else types = this.poiTypeRepository.findAll();
        return ResponseEntity.ok(types.stream().map(this.typeDtoManager::getDtofromEntity).toList());
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