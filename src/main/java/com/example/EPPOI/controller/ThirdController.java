package com.example.EPPOI.controller;


import com.example.EPPOI.model.RequestPoiNode;
import com.example.EPPOI.model.poi.PoiNode;
import com.example.EPPOI.model.user.ThirdUserNode;
import com.example.EPPOI.repository.ThirdRepository;
import com.example.EPPOI.repository.UserRoleRepository;
import com.example.EPPOI.service.ThirdsService;
import com.example.EPPOI.utility.MiddlewareToken;
import com.example.EPPOI.utility.PoiForm;
import com.example.EPPOI.utility.PoiFormThirds;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/thirds")
@CrossOrigin("*")
@Slf4j
public class ThirdController {

    private final ThirdsService thirdsService;
    private final MiddlewareToken<ThirdUserNode> middlewareToken;

    public ThirdController(ThirdsService thirdsService) {
        this.thirdsService = thirdsService;
        this.middlewareToken = new MiddlewareToken<>(thirdsService.getRepository());
    }

    /**
     * Get all the third poi requests owned by the user requesting them
     * @param request the http request
     * @return the list of third poi request
     */
    @PostMapping("/pois") //TODO control if is better to pass them with a dtos
    public ResponseEntity<?> getOwnedPoiRequest(HttpServletRequest request) {
        ThirdUserNode thirdUserNode = this.middlewareToken.getUserFromToken(request);
        return ResponseEntity.ok(thirdUserNode.getPoiRequests());
    }


    /**
     * Get all the third pois owned by the user requesting them
     * @param request the http request
     * @return the list of third pois
     */
    @PostMapping("/pois") //TODO control if is better to pass them with a dtos
    public ResponseEntity<?> getOwnedPois(HttpServletRequest request) {
        ThirdUserNode thirdUserNode = this.middlewareToken.getUserFromToken(request);
        return ResponseEntity.ok(thirdUserNode.getThirdPOIs());
    }

    /**
     * Create a basic Poi Request for an Ente
     * @param request the http request
     * @param form the form with the information
     * @param cityId the id of the city
     * @return the resulting request
     */
    @PostMapping("/poi-request") //TODO fixme
    public ResponseEntity<?> createPoiRequest(HttpServletRequest request, @RequestBody PoiForm form, @RequestParam Long cityId) {
        ThirdUserNode thirdUserNode = this.middlewareToken.getUserFromToken(request);
        RequestPoiNode result = this.thirdsService.createRequestPoi(thirdUserNode, form, cityId);
        return ResponseEntity.ok(result);
    }

    /**
     * Modify a third party poi primary information passed with the form
     * @param request the http request
     * @param form the form with the primary information of the poi
     * @param id the id of the poi
     * @return the modified poi
     */
    @PostMapping("/poi/modify-primary/{id}") //TODO
    public ResponseEntity<?> modifyPoi(HttpServletRequest request, @RequestBody PoiForm form,
                                       @PathVariable String id) {
        ThirdUserNode thirdUserNode = this.middlewareToken.getUserFromToken(request);
        PoiNode result;
        result = this.thirdsService.modifyPoi(thirdUserNode, form, Long.valueOf(id));
        if(result == null) return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(result);
    }


    /**
     * Modify a third party poi secondary information passed with the form
     * @param request the http request
     * @param secondaryForm the form with the secondary information of the poi
     * @param id the id of the poi
     * @return the modified poi
     */
    @PostMapping("/poi/modify-secondary/{id}")
    public ResponseEntity<?> modifyPoi(HttpServletRequest request, @RequestBody PoiFormThirds secondaryForm,
                                       @PathVariable String id) {
        ThirdUserNode thirdUserNode = this.middlewareToken.getUserFromToken(request);
        PoiNode result;
        result = this.thirdsService.modifyPoi(thirdUserNode, secondaryForm, Long.valueOf(id));
        if(result == null) return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(result);
    }


}
