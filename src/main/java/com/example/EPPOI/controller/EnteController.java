package com.example.EPPOI.controller;

import com.example.EPPOI.model.ThirdPartyRegistrationRequest;
import com.example.EPPOI.model.poi.PoiNode;
import com.example.EPPOI.model.user.EnteNode;
import com.example.EPPOI.repository.ThirdRequestRegistrationRepository;
import com.example.EPPOI.service.EnteService;
import com.example.EPPOI.utility.MiddlewareToken;
import com.example.EPPOI.utility.PoiForm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
@RequestMapping("/ente")
@CrossOrigin("*")
@Slf4j
public class EnteController {

    private final MiddlewareToken<EnteNode> middlewareToken;

    private final ThirdRequestRegistrationRepository thirdRequestRegistrationRepository;
    private EnteService enteService;

    public EnteController(EnteService enteService, ThirdRequestRegistrationRepository thirdRequestRegistrationRepository) {
        this.enteService = enteService;
        this.thirdRequestRegistrationRepository = thirdRequestRegistrationRepository;
        this.middlewareToken = new MiddlewareToken<>(enteService.getRepository());
    }

    @GetMapping
    public ResponseEntity<EnteNode> getUserInfo(HttpServletRequest request){
        EnteNode ente = this.middlewareToken.getUserFromToken(request);
        return ResponseEntity.ok(ente);
    }

    @PostMapping("/pois")
    public ResponseEntity<PoiNode> createPoi(@RequestBody PoiForm form,HttpServletRequest request){
        EnteNode ente = this.middlewareToken.getUserFromToken(request);
        PoiNode poi = this.enteService.createPoi(ente,form);
        return ResponseEntity.ok(poi);
    }

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
