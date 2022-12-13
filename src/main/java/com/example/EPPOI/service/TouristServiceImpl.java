package com.example.EPPOI.service;

import com.example.EPPOI.model.*;
import com.example.EPPOI.model.poi.PoiNode;
import com.example.EPPOI.model.user.EnteNode;
import com.example.EPPOI.model.user.TouristNode;
import com.example.EPPOI.model.user.UserRoleNode;
import com.example.EPPOI.repository.*;
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

    private final ItineraryRepository itineraryRepository;
    private final CityService cityService;
    private final TouristRepository touristRepository;
    private final UserRoleRepository userRoleRepository;
    private final PoiService poiService;
    private final EnteRepository enteRepository;
    private final PoiRequestService poiRequestService;

    private void addItineraryToUser(TouristNode tourist, ItineraryNode toAdd) {
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
    public ItineraryNode createItinerary(TouristNode creator, String name, String description, List<PoiNode> POIs) {
        ItineraryNode result = new ItineraryNode(name, description);
        for (int i = 1; i <= POIs.size(); i++) {
            PoiNode poi = POIs.get(i - 1);
            result.getPoints().add(new ItineraryRelPoi(poi, i));
        }
        this.itineraryRepository.save(result);
        List<CityNode> cities = new ArrayList<>();
        for (PoiNode p : POIs) {
            cities.add(this.cityService.getCityByPoi(p.getId()));
        }
        cities.forEach(cityNode -> {
            cityNode.getItineraries().add(result);
            //this.cityRepository.save(cityNode);
        });
        this.cityService.saveCity(cities.toArray(CityNode[]::new));
        this.addItineraryToUser(creator, result);
        return result;
    }

    //FIXME: rivedere metodo
    @Override
    public RequestPoiNode createRequestPoi(TouristNode creator, PoiForm params, Long cityId) {
        RequestPoiNode result = this.poiRequestService.createPoiRequestFromParams(params);
        result.setMadeBy(creator);
        log.info("setting madeBy {}", creator.getUsername());

        log.info("saving result after madeBy {}", result.getMadeBy().getUsername());
        /*creator.getPoiRequests().add(result);
        this.touristRepository.save(creator);
        log.info("saving creator {}",creator.getPoiRequests().size());*/
        List<EnteNode> entes;
        if (Objects.isNull(params.getIdPoi()))
            entes = this.isNewFilter(true, cityId);
        else {
            result.setTarget(this.poiService.findPoiById(params.getIdPoi()));
            this.poiRequestService.saveRequest(result);
            entes = this.isNewFilter(false, params.getIdPoi());
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


    private List<EnteNode> isNewFilter(boolean isNew, Long filter) {
        Stream<EnteNode> optionals = this.enteRepository.findAll().stream();
        if (isNew) {
            return optionals
                    .filter(e -> e.getCity().getId().equals(filter))
                    .toList();
        } else return optionals
                .filter(e -> e.getCity().getId().equals(this.cityService.getCityByPoi(filter).getId()))
                .toList();
    }

    @Override
    public TouristRepository getRepository() {
        return this.touristRepository;
    }
}
