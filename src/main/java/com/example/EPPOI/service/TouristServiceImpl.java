package com.example.EPPOI.service;

import com.example.EPPOI.model.*;
import com.example.EPPOI.model.poi.PoiNode;
import com.example.EPPOI.model.user.EnteNode;
import com.example.EPPOI.model.user.TouristNode;
import com.example.EPPOI.model.user.UserRoleNode;
import com.example.EPPOI.repository.*;
import com.example.EPPOI.utility.PoiParamsProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class TouristServiceImpl implements TouristService {

    private final ItineraryRepository itineraryRepository;
    private final CityRepository cityRepository;
    private final TouristRepository touristRepository;
    private final UserRoleRepository userRoleRepository;
    private final EnteRepository enteRepository;
    private final RequestPoiRepository requestPoiRepository;

    private CityNode getCityFromPoi(PoiNode poi){
        return this.cityRepository.findAll().stream().filter(c -> c.getPOIs().contains(poi)).findFirst()
                .orElseThrow(()-> new NullPointerException("No city found for that poi"));
    }

    private void addItineraryToUser(TouristNode tourist, ItineraryNode toAdd){
        tourist.getItineraries().add(toAdd);
        this.touristRepository.save(tourist);
    }

    @Override
    public TouristNode getUserByUsername(String username) throws NullPointerException {
        log.info("start ");
        UserRoleNode touristRole = this.userRoleRepository.findByName("TOURIST");
        log.info("role {}",touristRole);
        TouristNode user = this.touristRepository.findAll().stream().filter((u)-> u.getUsername().equals(username))
                .findFirst().orElseThrow(()-> new NullPointerException("No such user"));
        log.info("user {}",user);
        if(user.getRoles().contains(touristRole)) return user;
        else throw new NullPointerException("User " + username + "has not role " + touristRole);
    }
    @Override
    public ItineraryNode createItinerary(TouristNode creator,String name, String description, List<PoiNode> POIs) {
        ItineraryNode result = new ItineraryNode(name, description);
        for (int i = 1; i <= POIs.size() ; i++) {
            PoiNode poi = POIs.get(i-1);
            result.getPoints().add(new ItineraryRelPoi(poi,i));
        }
        this.itineraryRepository.save(result);
        List<CityNode> cities = new ArrayList<>();
        for (PoiNode p : POIs) {
            cities.add(this.getCityFromPoi(p));
        }
        cities.forEach(cityNode -> {
            cityNode.getItineraries().add(result);
            //this.cityRepository.save(cityNode);
        });
        this.cityRepository.saveAll(cities);
        this.addItineraryToUser(creator,result);
        return result;
    }

    //FIXME: rivedere metodo
    @Override
    public RequestPoiNode createRequestPoi(TouristNode creator,PoiParamsProvider params,CityNode cityNode) {
        RequestPoiNode result = new RequestPoiNode(params,creator);
        this.requestPoiRepository.save(result);
        EnteNode enteNode = this.enteRepository.findAll().stream().filter(e-> e.getCity().equals(cityNode)).findFirst()
                .orElseThrow(()-> new NullPointerException("No such city"));
        enteNode.getPoiRequests().add(result);
        this.enteRepository.save(enteNode);
        creator.getPoiRequests().add(result);
        this.touristRepository.save(creator);
        return result;
    }

    @Override
    public RequestPoiNode createRequestPoi(TouristNode creator,PoiParamsProvider params, PoiNode toModify) {
        return null;
    }

    @Override
    public TouristRepository getRepository() {
        return this.touristRepository;
    }
}
