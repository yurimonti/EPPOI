package com.example.EPPOI.service;

import com.example.EPPOI.dto.CoordsDTO;
import com.example.EPPOI.model.*;
import com.example.EPPOI.model.poi.PoiNode;
import com.example.EPPOI.model.user.EnteNode;
import com.example.EPPOI.model.user.ThirdUserNode;
import com.example.EPPOI.model.user.UserRoleNode;
import com.example.EPPOI.repository.*;
import com.example.EPPOI.utility.ItineraryForm;
import com.example.EPPOI.utility.PoiForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class EnteServiceImpl implements EnteService {
    private final ItineraryRepository itineraryRepository;
    private final PoiRepository poiRepository;
    //TODO: use city service
    private final CityRepository cityRepository;
    private final CityService cityService;
    private final EnteRepository enteRepository;

    private final GeneralUserService generalUserService;
    private final UserRoleRepository userRoleRepository;

    private final ThirdRequestRegistrationRepository thirdRequestRegistrationRepository;
    private final PoiService poiService;
    private final ItineraryService itineraryService;

    //------------------------------------  POIs -----------------------------------------------------------------
    @Override
    public PoiNode createPoi(EnteNode ente, PoiForm form) {
        CoordsDTO coordsDTO = form.getCoordinate();
        CoordsNode coords = new CoordsNode(coordsDTO.getLat(), coordsDTO.getLon());
        PoiNode result = new PoiNode(form.getName(), form.getDescription(), coords, form.getTimeToVisit());
        CityNode city = ente.getCity();
        this.poiService.setCityToPoi(result, city);
        return this.poiService.savePoi(result);
    }

    //------------------------------------  ITINERARIES ----------------------------------------------------------
    @Override
    public ItineraryNode createItinerary(EnteNode ente, ItineraryForm itineraryForm) {
        ItineraryNode result = new ItineraryNode(itineraryForm.getName(),itineraryForm.getDescription());
        List<PoiNode> POIsToAdd = new ArrayList<>();
        itineraryForm.getPOIsId().forEach(i -> POIsToAdd.add(this.poiService.findPoiById(i)));
        this.itineraryService.fillItinerary(result,POIsToAdd);
        CityNode city = ente.getCity();
        city.getItineraries().add(result);
        this.cityRepository.save(city);
        return result;
    }

    @Override
    public ItineraryRequestNode createItineraryRequest(EnteNode ente, ItineraryForm itineraryForm) {
        ItineraryRequestNode result = new ItineraryRequestNode(itineraryForm.getName(),itineraryForm.getDescription());
        List<PoiNode> POIsToAdd = new ArrayList<>();
        List<EnteNode> entes = new ArrayList<>();
        itineraryForm.getPOIsId().forEach(i -> {
            PoiNode p = this.poiService.findPoiById(i);
            POIsToAdd.add(p);
        });
        this.itineraryService.fillItinerary(result,POIsToAdd);
        this.cityService.getCitiesByPoi(POIsToAdd.toArray(PoiNode[]::new))
                .forEach(c -> entes.addAll(this.enteRepository.findAllByCity_Name(c.getName())));
        entes.forEach(e -> e.getItineraryRequests().add(new ItineraryRequestRel(result,false)));
        this.enteRepository.saveAll(entes);
        return result;
    }
    //TODO: to implement
    @Override
    public void setConsensusToItinerary(ItineraryRequestNode target, boolean consensus) {

    }

    //------------------------------------  THIRD USER MANAGEMENT ---------------------------------------------
    @Override
    public void setConsensusToRegistration(EnteNode ente, ThirdPartyRegistrationRequest target, boolean consensus) {
        ThirdPartyRegistrationRel a = ente.getRegistrationRequests().stream()
                .filter(u -> u.getRequest().equals(target))
                .findFirst().orElseThrow(NullPointerException::new);
        if (consensus) {
            if (!a.getConsensus()) {
                a.setConsensus(true);
                target.setConsensus(target.getConsensus()-1);
                this.thirdRequestRegistrationRepository.save(target);
                if(target.getConsensus() == 0){
                    UserRoleNode role = this.userRoleRepository.findByName("THIRD_PARTY");
                    this.generalUserService.saveUser(new ThirdUserNode(target.getName(),target.getSurname(),target.getEmail(),
                            target.getPassword(), target.getUsername(),role));
                    List<EnteNode> entes = this.enteRepository.findAll().stream()
                            .filter(e ->
                                    e.getRegistrationRequests().stream()
                                            .map(ThirdPartyRegistrationRel::getRequest)
                                            .toList()
                                            .contains(target))
                            .toList();
                    entes.forEach(e -> this.deleteRegistrationRequest(e,target));
                    this.thirdRequestRegistrationRepository.delete(target);
                    //this.enteRepository.saveAll(entes);

                }
            }
        } else{
            this.thirdRequestRegistrationRepository.delete(target);
        }
    }

    @Override
    public void deleteRegistrationRequest(EnteNode ente, ThirdPartyRegistrationRequest target) {
        ente.getRegistrationRequests().remove(ente.getRegistrationRequests()
                .stream()
                .filter(r -> r.getRequest().equals(target))
                .findAny()
                .orElseThrow(NullPointerException::new));
        this.enteRepository.save(ente);
    }

    @Override
    public EnteRepository getRepository() {
        return this.enteRepository;
    }



    @Override
    public EnteNode getUserByUsername(String username) throws NullPointerException {
        EnteNode user = this.enteRepository.findAll().stream().filter((u) -> u.getUsername().equals(username))
                .findFirst().orElseThrow(() -> new NullPointerException("No such user"));
        UserRoleNode enteRole = this.userRoleRepository.findByName("ENTE");
        if (user.getRoles().contains(enteRole)) return user;
        else throw new NullPointerException("User " + username + "has not role " + enteRole);
    }
}
