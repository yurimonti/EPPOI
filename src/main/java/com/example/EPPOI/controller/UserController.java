package com.example.EPPOI.controller;

import com.example.EPPOI.dto.CategoryDTO;
import com.example.EPPOI.dto.PoiDTO;
import com.example.EPPOI.dto.PoiTypeDTO;
import com.example.EPPOI.model.CategoryNode;
import com.example.EPPOI.model.ItineraryNode;
import com.example.EPPOI.model.PoiTypeNode;
import com.example.EPPOI.model.poi.PoiNode;
import com.example.EPPOI.model.RequestPoiNode;
import com.example.EPPOI.model.user.TouristNode;
import com.example.EPPOI.model.user.UserNode;
import com.example.EPPOI.repository.CategoryRepository;
import com.example.EPPOI.repository.CityRepository;
import com.example.EPPOI.repository.PoiTypeRepository;
import com.example.EPPOI.repository.UserNodeRepository;
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

    private final CityRepository cityRepository;

    private final UserNodeRepository userNodeRepository;

    /*@GetMapping("/pois")
    public ResponseEntity<List<PoiDTO>> getPois(){
        return ResponseEntity.ok(this.poiService.getAllPois().stream().map(PoiDTO::new).toList());
    }*/

    @GetMapping("/pois/{id}")
    public ResponseEntity<PoiDTO> getPoi(@PathVariable String id){
        PoiNode poi;
        try{
            poi = this.poiService.findPoiById(Long.parseLong(id));
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok(this.poiService.createDTOfromNode(poi));
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