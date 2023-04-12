package com.example.EPPOI.service;

import com.example.EPPOI.dto.PoiRequestDTO;
import com.example.EPPOI.model.*;
import com.example.EPPOI.model.poi.PoiNode;
import com.example.EPPOI.model.user.EnteNode;
import com.example.EPPOI.model.user.TouristNode;
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
import java.util.stream.Stream;

@Slf4j
@Service
@RequiredArgsConstructor
public class TouristServiceImpl implements TouristService {

    private final ItineraryService itineraryService;
    private final CityService cityService;
    private final TouristRepository touristRepository;
    private final UserRoleRepository userRoleRepository;
    private final PoiService poiService;
    private final EnteRepository enteRepository;
    private final PoiRequestService poiRequestService;

    private void addItineraryToUser(TouristNode tourist, ItineraryNode toAdd) {
        toAdd.setCreatedBy(tourist.getUsername());
        this.itineraryService.saveItinerary(toAdd);
        tourist.getItineraries().add(toAdd);
        this.touristRepository.save(tourist);
    }

    @Override
    public TouristNode getUserByUsername(String username) throws NullPointerException {
        log.info("start ");
        UserRoleNode touristRole = this.userRoleRepository.findByName("TOURIST");
        log.info("role {}", touristRole);
        TouristNode user = this.touristRepository.findAll().stream().filter((u) -> u.getUsername().equals(username))
                .findFirst().orElseThrow(() -> new NullPointerException("No such user"));
        log.info("user {}", user);
        if (user.getRoles().contains(touristRole)) return user;
        else throw new NullPointerException("User " + username + "has not role " + touristRole);
    }

    @Override
    public ItineraryNode createItinerary(TouristNode creator, ItineraryForm form) {
        List<PoiNode> POIsToAdd = new ArrayList<>();
        form.getPoisId().forEach(i -> POIsToAdd.add(this.poiService.findPoiById(i)));
        ItineraryNode result = this.itineraryService.createBaseItinerary(form.getName(),
                form.getDescription(),POIsToAdd,form.getGeoJsonList(),false);
        List<CityNode> cities = new ArrayList<>();
        for (PoiNode p : POIsToAdd) {
            cities.add(this.cityService.getCityByPoi(p.getId()));
        }
        cities.forEach(cityNode -> {
            cityNode.getItineraries().add(result);
            //this.cityRepository.save(cityNode);
        });
        this.cityService.saveCities(cities);
        this.addItineraryToUser(creator, result);
        return result;
    }

    //FIXME: rivedere metodo
    @Override
    public RequestPoiNode createRequestPoi(TouristNode creator, PoiForm params, Long cityId) {
        RequestPoiNode result = this.poiRequestService.createPoiRequestFromParams(params);
        result.setCreatedBy(creator.getUsername());
        log.info("setting madeBy {}", creator.getUsername());
        creator.getPoiRequests().add(result);
        this.touristRepository.save(creator);
        log.info("saving creator {}",creator.getPoiRequests().size());
        List<EnteNode> entes;
        log.info("target id {}",params.getIdPoi());
        if (Objects.isNull(params.getIdPoi())) {
            entes = this.isNewFilter(true, cityId);
            log.info("is new");
        }
        else {
            PoiNode poi = this.poiService.findPoiById(params.getIdPoi());
            result.setTarget(poi);
            log.info("poi is {}", poi);
            this.poiRequestService.saveRequest(result);
            entes = this.isNewFilter(false, params.getIdPoi());
            log.info("is modify");
        }
        entes.forEach(e -> {
            e.getPoiRequests().add(result);
            log.info("adding result to ente {}", e.getUsername());
            this.enteRepository.save(e);
            log.info("saving ente {}", e.getPoiRequests().size());
        });
        /*this.enteRepository.findAll()
                .stream()
                .filter(e -> e.getCity().getId().equals(cityId))
                .forEach(e -> {
                    e.getPoiRequests().add(result);
                    log.info("adding result to ente {}",e.getUsername());
                    this.enteRepository.save(e);
                    log.info("saving ente {}",e.getPoiRequests().size());
                });*/
        /*RequestPoiNode result = new RequestPoiNode(params,creator);
        this.requestPoiRepository.save(result);
        EnteNode enteNode = this.enteRepository.findAll().stream().filter(e-> e.getCity().equals(cityNode)).findFirst()
                .orElseThrow(()-> new NullPointerException("No such city"));
        enteNode.getPoiRequests().add(result);
        this.enteRepository.save(enteNode);
        creator.getPoiRequests().add(result);
        this.touristRepository.save(creator);*/
        log.info("result : {}", result);
        return result;
    }

    private ItineraryRequestNode createRequestFromItinerary(ItineraryNode itinerary) {
        log.info("richiesta 0 {}",itinerary.getPoints().size());
        ItineraryRequestNode result = new ItineraryRequestNode(itinerary.getName(), itinerary.getDescription());
        log.info("richiesta 1 {}",result.getName());
        itinerary.getPoints().forEach(p -> result.getPoints().add(new ItineraryRelPoi(p.getPoi(),p.getIndex())));
        log.info("richiesta 2 {}",result.getPoints().size());
        List<EnteNode> entes = new ArrayList<>();
        result.setGeoJsonList(itinerary.getGeoJsonList());
        log.info("richiesta 3 {}",result.getGeoJsonList().size());
        result.setTimeToVisit(itinerary.getTimeToVisit());
        log.info("richiesta 4 {}",result.getTimeToVisit());
        List<CityNode> cities = this.cityService.getCitiesByItinerary(itinerary);
        log.info("richiesta 5 {}",cities.size());
        cities.forEach(c -> entes.addAll(
                this.enteRepository.findAll().stream()
                        .filter(e -> e.getCity().getId().equals(c.getId())).toList()));
        log.info("richiesta 6 {}",entes.size());
        result.setConsensus(entes.size());
        result.setCategories(itinerary.getCategories());
        log.info("richiesta 7 {}",result.getCategories().size());
        this.itineraryService.saveItinerary(result);
        entes.forEach(e -> e.getItineraryRequests().add(new ItineraryRequestRel(result, false)));
        log.info("richiesta 8 {}",entes.size());
        this.enteRepository.saveAll(entes);
        return result;
    }

    @Override
    public ItineraryRequestNode proposeItinerary(TouristNode tourist, Long itineraryId) throws NullPointerException{
        log.info("start");
        ItineraryNode toPropose = this.itineraryService.getItinerary(itineraryId);
        log.info("itinerary by id {}",toPropose.getName());
        if(tourist.getItineraries().stream().noneMatch(i -> i.getId().equals(itineraryId)))
            throw new NullPointerException("Itinerary not present");
        ItineraryRequestNode result = this.createRequestFromItinerary(toPropose);
        result.setCreatedBy(tourist.getUsername());
        this.itineraryService.saveItinerary(result);
        log.info("ritorno -> {}",result.getPoints());
        return result;
    }

    @Override
    public void deleteItinerary(TouristNode tourist, Long itineraryId) {
        ItineraryNode itineraryNode = this.itineraryService.getItinerary(itineraryId);
        if (tourist.getItineraries().stream().map(ItineraryNode::getId).noneMatch(l -> l.equals(itineraryId)))
            throw new IllegalArgumentException("This itinerary is not available for your city");
        this.itineraryService.deleteItinerary(itineraryNode);
    }

    @Override
    public List<PoiRequestDTO> getAllRequestDTOs(TouristNode tourist) {
        return tourist.getPoiRequests().stream().map(this.poiRequestService::getDTOFromRequest).toList();
    }

    @Override
    public void deletePoiRequest(TouristNode tourist, Long idRequest) throws NullPointerException {
        if(tourist.getPoiRequests().stream()
                .filter(p-> Objects.isNull(p.getIsAccepted()))
                .noneMatch(p -> p.getId().equals(idRequest)))
            throw new NullPointerException();
        RequestPoiNode toDelete = this.poiRequestService.getRequestById(idRequest);
        this.poiRequestService.deleteRequest(toDelete);

    }


    private List<EnteNode> isNewFilter(boolean isNew, Long filter) {
        Stream<EnteNode> optionals = this.enteRepository.findAll().stream();
        List<EnteNode> result;
        if (isNew) {
            result = optionals
                    .filter(e -> e.getCity().getId().equals(filter)).toList();
        } else result = optionals
                .filter(e -> e.getCity().getId().equals(this.cityService.getCityByPoi(filter).getId())).toList();
        if(result.isEmpty()) throw new NullPointerException();
        return result;
    }

    @Override
    public TouristRepository getRepository() {
        return this.touristRepository;
    }
}
