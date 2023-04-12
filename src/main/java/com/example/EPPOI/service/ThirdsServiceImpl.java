package com.example.EPPOI.service;


import com.example.EPPOI.dto.ThirdPoiRequestDTO;
import com.example.EPPOI.model.CityNode;
import com.example.EPPOI.model.PoiTypeNode;
import com.example.EPPOI.model.RequestPoiNode;
import com.example.EPPOI.model.ThirdPartyPoiRequest;
import com.example.EPPOI.model.poi.PoiNode;
import com.example.EPPOI.model.poi.RestaurantPoiNode;
import com.example.EPPOI.model.user.EnteNode;
import com.example.EPPOI.model.user.ThirdUserNode;
import com.example.EPPOI.repository.EnteRepository;
import com.example.EPPOI.repository.PoiRepository;
import com.example.EPPOI.repository.ThirdPartyPoiRequestRepository;
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
    private final ThirdPartyPoiRequestRepository thirdPartyPoiRequestRepository;
    private final PoiRequestService poiRequestService;
    private final EnteRepository enteRepository;
    private final PoiService poiService;
    private final PoiRepository poiRepository;


    /**
     * Create a third POI request with the basic information passed
     * @param creator the user that created the request
     * @param form the form
     * @param cityId the id of the city
     * @return the created request poi node
     */
    @Override
    public ThirdPartyPoiRequest createRequestPoi(ThirdUserNode creator, PoiForm form, Long cityId) {
        ThirdPartyPoiRequest request = this.poiRequestService.createThirdPoiRequestFromParams(form);
        creator.getPoiRequests().add(request);
        this.thirdRepository.save(creator);

        List<EnteNode> entes;
        entes = this.isNewFilter(cityId);
        entes.forEach(e -> {
            e.getThirdsPoiRequests().add(request);
            this.enteRepository.save(e);
        });
        return request;
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
    public PoiNode modifyPoi(ThirdUserNode thirdUserNode, PoiFormThirds secondaryForm, Long idPoi) { //TODO controllare
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
     * Create a PoiNode for thirds from a third party poi request
     * @param toSet the request passed
     * @return the created Poi
     */
    @Override
    public PoiNode createPoiFromRequest(ThirdPartyPoiRequest toSet) {
        PoiNode poi = null;
        List<String> typesName = toSet.getTypes().stream().map(PoiTypeNode::getName).toList();
        //can become a switch if needed
        if(typesName.contains("Restaurant")){
            ThirdUserNode third = this.findThirdByRequest(toSet);
            poi = new RestaurantPoiNode(this.poiService.poiFromThirdRequest(toSet));
            poi.getContributors().add(third.getUsername());
            third.getThirdPOIs().add(poi);
            this.poiRepository.save(poi);
            this.thirdRepository.save(third);
        }
        else if(typesName.contains("Mobilita")){
            //TODO
        }
        else{
            log.info("Something wrong on the creation of a third poi from a request");
            return null;
        }
        return poi;
    }

    /*
    /**
     * Create a PoiNode for thirds from a PoiForm knowing its poi request and the ente of the city
     * @param ente the ente of the city in witch create the poi
     * @param toSet the starting request
     * @param form the form with the modified parameters
     * @return the created Poi
     */
    /*
    @Override
    public PoiNode createThirdPoiFromForm(EnteNode ente, RequestPoiNode toSet, PoiForm form) { //TODO fix
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
    */

    /**
     * This method create a list of DTOs of the requests of a Third User
     * @param third the third user
     * @return a list of all the request of the user as DTOs
     */
    @Override
    public List<ThirdPoiRequestDTO> getAllRequestDTOs(ThirdUserNode third) {
        return third.getPoiRequests().stream().map(this.poiRequestService::getDTOFromThirdRequest).toList();
    }


    /**
     * Get a specific third party poi request defined by its id
     * @param idRequest the id of the request
     * @return the request searched
     * @throws NullPointerException if not found
     */
    @Override
    public ThirdPartyPoiRequest getRequestById(Long idRequest) throws NullPointerException {
        return this.thirdPartyPoiRequestRepository.findById(idRequest)
                .orElseThrow(()-> new NullPointerException("No such request with id: "+idRequest));
    }

    /**
     * Save a third party poi request in the database
     * @param toSave the request to save
     */
    @Override
    public void saveRequest(ThirdPartyPoiRequest toSave) {
        this.thirdPartyPoiRequestRepository.save(toSave);
    }


    private ThirdUserNode findThirdByRequest(ThirdPartyPoiRequest request){
        return this.thirdRepository.findAll().stream()
                .filter(t -> t.getPoiRequests().contains(request)).findFirst().orElseThrow();
    }


}