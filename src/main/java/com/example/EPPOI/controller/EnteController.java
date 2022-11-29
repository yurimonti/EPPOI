package com.example.EPPOI.controller;

import com.example.EPPOI.model.PoiNode;
import com.example.EPPOI.model.user.EnteNode;
import com.example.EPPOI.service.EnteService;
import com.example.EPPOI.utility.MiddlewareToken;
import com.example.EPPOI.utility.PoiForm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/ente")
@CrossOrigin("*")
@Slf4j
public class EnteController {

    private final MiddlewareToken<EnteNode> middlewareToken;
    private EnteService enteService;

    public EnteController(EnteService enteService) {
        this.enteService = enteService;
        this.middlewareToken = new MiddlewareToken<>(enteService.getRepository());
    }

    @GetMapping
    public ResponseEntity<EnteNode> getUserInfo(HttpServletRequest request){
        EnteNode ente = this.middlewareToken.getUserFromToken(request);
        return ResponseEntity.ok(ente);
    }

    //TODO: finire metodo
    @PostMapping("/pois")
    public ResponseEntity<PoiNode> createPoi(@RequestBody PoiForm form,HttpServletRequest request){
        EnteNode ente = this.middlewareToken.getUserFromToken(request);
        PoiNode poi = this.enteService.createPoi(ente,form);
        return ResponseEntity.ok(poi);
    }

}
