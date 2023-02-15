package com.example.EPPOI.service;


import com.example.EPPOI.model.CityNode;
import com.example.EPPOI.model.PoiTypeNode;
import com.example.EPPOI.model.RequestPoiNode;
import com.example.EPPOI.model.poi.PoiNode;
import com.example.EPPOI.model.poi.RestaurantPoiNode;
import com.example.EPPOI.model.poi.ThirdPoiNode;
import com.example.EPPOI.model.user.EnteNode;
import com.example.EPPOI.model.user.ThirdUserNode;
import com.example.EPPOI.repository.EnteRepository;
import com.example.EPPOI.repository.PoiRepository;
import com.example.EPPOI.repository.RestaurantRepository;
import com.example.EPPOI.repository.ThirdRepository;
import com.example.EPPOI.utility.PoiForm;
import com.example.EPPOI.utility.PoiFormThirds;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Slf4j
public class ThirdsServiceImpl implements ThirdsService{

    private final ThirdRepository thirdRepository;
    private final PoiRequestService poiRequestService;
    private final EnteRepository enteRepository;
    private final PoiService poiService;
    private final PoiRepository poiRepository;


    /**
     * Create a basic POI request from the information passed
     * @param creator the user that created the request
     * @param form the form
     * @param cityId the id of the city
     * @return the created request poi node
     */
    @Override
    public RequestPoiNode createRequestPoi(ThirdUserNode creator, PoiForm form, Long cityId) {
        RequestPoiNode result = this.poiRequestService.createPoiRequestFromParams(form);
        result.setCreatedBy(creator.getUsername());
        creator.getPoiRequests().add(result);
        this.thirdRepository.save(creator);
        List<EnteNode> entes;
        entes = this.isNewFilter(cityId);

        entes.forEach(e -> {
            e.getPoiRequests().add(result);
            this.enteRepository.save(e);
        });
        return result;
    }

    private List<EnteNode> isNewFilter(Long filter) {
        Stream<EnteNode> optionals = this.enteRepository.findAll().stream();
        List<EnteNode> result;
        result = optionals.filter(e -> e.getCity().getId().equals(filter)).toList();
        if(result.isEmpty()) throw new NullPointerException();
        return result;
    }

    @Override
    public ThirdRepository getRepository() {
        return this.thirdRepository;
    }

    /**
     * Modify the primary information of a poi based on the form passed
     * @param thirdUserNode the user that own the poi
     * @param form the form containing the new information
     * @param idPoi the id of the poi to modify
     * @return the modified poi
     */
    @Override
    public PoiNode modifyPoi(ThirdUserNode thirdUserNode, PoiForm form, Long idPoi) {
        PoiNode poi = this.poiRepository.findById(idPoi).orElseThrow();
        if(thirdUserNode.getThirdPOIs().contains(poi)){
            poi = this.poiService.setParamsToPoi(poi,form);
            this.poiRepository.save(poi);
            return poi;
        }
        return null;
    }

    /**
     * Modify the secondary information of a poi based on the form passed
     * @param thirdUserNode the user that own the poi
     * @param secondaryForm the form containing the new information
     * @param idPoi the id of the poi to modify
     * @return the modified poi
     */
    @Override
    public PoiNode modifyPoi(ThirdUserNode thirdUserNode, PoiFormThirds secondaryForm, Long idPoi) {
        PoiNode poi = this.poiRepository.findById(idPoi).orElseThrow();
        if(thirdUserNode.getThirdPOIs().contains(poi)){
            List<String> typesName = poi.getTypes().stream().map(PoiTypeNode::getName).toList();
            if (typesName.contains("Restaurant")) {
                RestaurantPoiNode restPoi = (RestaurantPoiNode) poi;
                this.modifyRestaurantInformation(restPoi,secondaryForm);
                this.poiRepository.save(restPoi); //TODO control if it works
            } else if (typesName.contains("Mobilita")) {
                //TODO
            }
            //add else if for other types of third pois
            else {
                log.info("Something wrong while modifying a third poi secondary information");
                return null;
            }
        }
        return poi;
    }

    private void modifyRestaurantInformation(RestaurantPoiNode restPoi, PoiFormThirds secondaryForm) {
        restPoi.setMenu(secondaryForm.getMenu());
        //add more if more secondary information are implemented
    }


    /**
     * Control if the username passed is owned by a third party
     * @param createdBy the username to check
     * @return true if the username is owned by a third party, false otherwise
     */
    @Override
    public boolean isThirdUser(String createdBy) {
        try{
            this.thirdRepository.findByUsername(createdBy);
        }
        catch (Exception e){
            return false;
        }
        return true;
    }

    /**
     * Create a PoiNode for thirds from a PoiRequest
     * @param toSet the poi request passed
     * @return the crated Poi
     */
    @Override
    public PoiNode createPoiFromRequest(RequestPoiNode toSet) {
        PoiNode poi = null;
        List<String> typesName = toSet.getTypes().stream().map(PoiTypeNode::getName).toList();
        if(typesName.contains("Restaurant")){
            poi = new RestaurantPoiNode(this.poiService.poiFromRequest(toSet));
            ThirdUserNode third = this.thirdRepository.findByUsername(toSet.getCreatedBy());
            third.getThirdPOIs().add(poi);
            this.poiRepository.save(poi);
            this.thirdRepository.save(third);
        }
        else if(typesName.contains("Mobilita")){
            //TODO
        }
        //add else if for other types of third pois
        else{
            log.info("Something wrong on the creation of a third poi from a request");
            return null;
        }
        return poi;
    }

    /**
     * Create a PoiNode for thirds from a PoiForm knowing its poi request and the ente of the city
     * @param ente the ente of the city in witch create the poi
     * @param toSet the starting request
     * @param form the form with the modified parameters
     * @return the created Poi
     */
    @Override
    public PoiNode createThirdPoiFromForm(EnteNode ente, RequestPoiNode toSet, PoiForm form) {
        PoiNode poi = null;
        List<String> typesName = toSet.getTypes().stream().map(PoiTypeNode::getName).toList();
        if(typesName.contains("Restaurant")){
            poi = new RestaurantPoiNode(this.poiService.createPoiFromParams(form));
            ThirdUserNode third = this.thirdRepository.findByUsername(toSet.getCreatedBy());
            third.getThirdPOIs().add(poi);
            this.poiRepository.save(poi);
            this.thirdRepository.save(third);
        }
        else if(typesName.contains("Mobilita")){
            //TODO
        }
        //add else if for other types of third pois
        else{
            log.info("Something wrong on the creation of a third poi from a form");
            return null;
        }
        CityNode city = ente.getCity();
        this.poiService.setCityToPoi(poi, city);
        return poi;
    }
}
