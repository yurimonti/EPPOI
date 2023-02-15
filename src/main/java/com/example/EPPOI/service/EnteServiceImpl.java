package com.example.EPPOI.service;

import com.example.EPPOI.model.*;
import com.example.EPPOI.model.poi.PoiNode;
import com.example.EPPOI.model.user.EnteNode;
import com.example.EPPOI.model.user.UserRoleNode;
import com.example.EPPOI.repository.*;
import com.example.EPPOI.utility.ItineraryForm;
import com.example.EPPOI.utility.PoiForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class EnteServiceImpl implements EnteService {
    private final CityService cityService;
    private final EnteRepository enteRepository;

    private final GeneralUserService generalUserService;
    private final UserRoleRepository userRoleRepository;
    private final PoiRequestService poiRequestService;
    private final PoiService poiService;
    private final ItineraryService itineraryService;

    private final ThirdsService thirdsService;

    //------------------------------------  POIs -----------------------------------------------------------------
    @Override
    public PoiNode createPoi(EnteNode ente, PoiForm form) {
        PoiNode result = this.poiService.createPoiFromParams(form);
        log.info("result");
        CityNode city = ente.getCity();
        log.info("city {}", city.getName());
        this.poiService.setCityToPoi(result, city);
        log.info("city saved {}", city.getName());
        return result;
    }

    @Override
    public void deletePoi(EnteNode ente, Long toDelete) {
        PoiNode poi = this.poiService.findPoiById(toDelete);
        if (ente.getCity().getPOIs().stream().map(PoiNode::getId).anyMatch(i -> i.equals(toDelete))) {
            this.poiService.deletePoi(poi);
        }
    }


    @Override
    public PoiNode modifyPoi(EnteNode ente, PoiForm form, Long poiToModify) throws IllegalArgumentException {
        CityNode city = ente.getCity();
        log.info("city :{}", city.getName());
        if (city.getPOIs().stream().map(PoiNode::getId).noneMatch(i -> i.equals(poiToModify)))
            throw new IllegalArgumentException("This POI cannot be changed by this user");
        PoiNode toModify = this.poiService.findPoiById(poiToModify);
        log.info("poi to modify :{}", toModify.getName());
        return this.poiService.setParamsToPoi(toModify, form);
    }

    @Override
    public void modifyPoiRequest(EnteNode ente, Long idRequest, PoiForm form) throws NullPointerException {
        RequestPoiNode toSet = this.poiRequestService.getRequestById(idRequest);
        toSet.setIsAccepted(true);
        this.poiRequestService.saveRequest(toSet);
        PoiNode result;
        if (Objects.isNull(toSet.getTarget())){
            if(this.isThirdRequest(toSet.getCreatedBy())){
                result = this.thirdsService.createThirdPoiFromForm(ente,toSet,form);
            }
            else{
                result = this.createPoi(ente, form);
            }
        }
        else result = this.modifyPoi(ente, form, toSet.getTarget().getId());
        result.getContributors().add(toSet.getCreatedBy());
        this.poiService.savePoi(result);
    }

    @Override
    public void setConsensusToPoiRequest(EnteNode ente, Long idRequest, Boolean consensus) {
        RequestPoiNode toSet = this.poiRequestService.getRequestById(idRequest);
        if (consensus) {
            //if it's in pending
            if (Objects.isNull(toSet.getIsAccepted())) {
                toSet.setIsAccepted(true);

                PoiNode poi;
                if(this.isThirdRequest(toSet.getCreatedBy())){
                    poi = this.thirdsService.createPoiFromRequest(toSet);
                }
                else{
                    poi = this.poiService.poiFromRequest(toSet);
                }
                CityNode city = ente.getCity();
                if (city.getPOIs()
                        .stream()
                        .map(PoiNode::getId)
                        .noneMatch(id -> poi.getId().equals(id))) {
                    city.getPOIs().add(poi);
                    this.cityService.saveCity(city);
                }

            }
        } else toSet.setIsAccepted(false);
        this.poiRequestService.saveRequest(toSet);
    }

    private boolean isThirdRequest(String createdBy){
        return this.thirdsService.isThirdUser(createdBy);
    }




    //------------------------------------  ITINERARIES ----------------------------------------------------------
    @Override
    public ItineraryNode createItinerary(EnteNode ente, ItineraryForm itineraryForm) {
        log.info("Entrato nella creazione Itinerario");
        List<PoiNode> POIsToAdd = new ArrayList<>();
        itineraryForm.getPoisId().forEach(i -> POIsToAdd.add(this.poiService.findPoiById(i)));
        ItineraryNode result = this.itineraryService.createBaseItinerary(itineraryForm.getName(),
                itineraryForm.getDescription(), POIsToAdd, itineraryForm.getGeoJsonList(), true);
        result.setCreatedBy(ente.getUsername());
        this.itineraryService.saveItinerary(result);
        CityNode city = ente.getCity();
        city.getItineraries().add(result);
        this.cityService.saveCity(city);
        return result;
    }

    private List<EnteNode> getEntesByCity(CityNode city) {
        return this.enteRepository.findAll().stream().filter(e -> e.getCity().getId().equals(city.getId())).toList();
    }

    @Override
    public ItineraryRequestNode createItineraryRequest(EnteNode ente, ItineraryForm itineraryForm) {
        ItineraryRequestNode result = new ItineraryRequestNode(itineraryForm.getName(), itineraryForm.getDescription());
        log.info("1");
        result.setCreatedBy(ente.getUsername());
        List<PoiNode> POIsToAdd = new ArrayList<>();
        List<EnteNode> entes = new ArrayList<>();
        itineraryForm.getPoisId().forEach(i -> {
            PoiNode p = this.poiService.findPoiById(i);
            POIsToAdd.add(p);
        });
        result.setGeoJsonList(itineraryForm.getGeoJsonList());
        result.setTimeToVisit(POIsToAdd.stream().map(PoiNode::getTimeToVisit).reduce(0.0, Double::sum));
        log.info("2");
        List<CityNode> cities = this.cityService.getCitiesByPoi(POIsToAdd);
        log.info("cities : {}", cities.stream().map(CityNode::getName).toList());
        cities.forEach(c -> entes.addAll(this.getEntesByCity(c)));
        log.info("enti coinvolti {}", entes.stream().map(EnteNode::getName).toList());
        result.setConsensus(entes.size() - 1);
        log.info("entes size {}", entes.size());
        this.itineraryService.fillItinerary(result, POIsToAdd);
        entes.forEach(e -> {
            boolean toSet = e.getId().equals(ente.getId());
            if (toSet) e.getItineraryRequests().add(new ItineraryRequestRel(result, true));
            else e.getItineraryRequests().add(new ItineraryRequestRel(result, false));
        });
        this.enteRepository.saveAll(entes);
        log.info("requests {}", ente.getItineraryRequests());
        return result;
    }

    @Override
    public void deleteItinerary(EnteNode ente, Long itineraryId) throws NullPointerException, IllegalArgumentException {
        ItineraryNode itineraryNode = this.itineraryService.getItinerary(itineraryId);
        if (ente.getCity().getItineraries().stream()
                .filter(ItineraryNode::getIsDefault)
                .map(ItineraryNode::getId)
                .noneMatch(l -> l.equals(itineraryId)))
            throw new IllegalArgumentException("This itinerary is not available for your city");
        this.itineraryService.deleteItinerary(itineraryNode);
    }

    //consensus false and number of consensus  0 -> rejected, number consensus > 0 and consensus false -> pending, 0 and true -> accepted
    @Override
    public void setConsensusToItinerary(EnteNode ente, ItineraryRequestNode target, boolean consensus) {
        ItineraryRequestRel request = ente.getItineraryRequests().stream()
                .filter(i -> i.getRequest().getId().equals(target.getId()))
                .findFirst()
                .orElseThrow(() -> {
                    log.warn("target request not found");
                    return new NullPointerException("target request not found");
                });
        if (consensus) {
            if (!request.getConsensus() && target.getConsensus() > 0) {
                request.setConsensus(true);
                target.setConsensus(target.getConsensus() - 1);
                if (target.getConsensus() == 0) {
                    target.setAccepted(true);
                    ItineraryNode it = this.itineraryService.createItineraryFromRequest(target);
                    this.enteRepository.findAll().stream()
                            .filter(e -> e.getItineraryRequests().stream()
                                    .map(ItineraryRequestRel::getRequest)
                                    .map(ItineraryRequestNode::getId)
                                    .toList()
                                    .contains(target.getId()))
                            .map(EnteNode::getCity)
                            .forEach(c -> this.cityService.addItinerary(c, it));
                }
            }
        } else {
            target.setConsensus(0);
            target.setAccepted(false);
        }
        this.itineraryService.saveItinerary(target);
    }

    //------------------------------------  THIRD USER MANAGEMENT ---------------------------------------------

    //TODO




    //------------------------------------  OTHER ---------------------------------------------
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
