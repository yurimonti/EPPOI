package com.example.EPPOI.service;

import com.example.EPPOI.dto.CoordsDTO;
import com.example.EPPOI.model.CityNode;
import com.example.EPPOI.model.CoordsNode;
import com.example.EPPOI.model.ItineraryNode;
import com.example.EPPOI.model.PoiNode;
import com.example.EPPOI.model.user.EnteNode;
import com.example.EPPOI.model.user.UserRoleNode;
import com.example.EPPOI.repository.*;
import com.example.EPPOI.utility.PoiForm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EnteServiceImpl implements EnteService{
    private final ItineraryRepository itineraryRepository;
    private final PoiRepository poiRepository;
    private final CityRepository cityRepository;
    private final EnteRepository enteRepository;
    private final UserRoleRepository userRoleRepository;
    private final PoiService poiService;
    @Override
    public PoiNode createPoi(EnteNode ente,PoiForm form) {
        CoordsDTO coordsDTO = form.getCoordinate();
        CoordsNode coords = new CoordsNode(coordsDTO.getLat(), coordsDTO.getLon());
        PoiNode result = new PoiNode(form.getName(),form.getDescription(),coords, form.getTimeToVisit());
        CityNode city = ente.getCity();
        this.poiService.setCityToPoi(result,city);
        return this.poiService.savePoi(result);
    }

    @Override
    public ItineraryNode createItinerary(String name, String description, List<PoiNode> POIs) {
        return null;
    }

    @Override
    public void setConsensus(ItineraryNode target, boolean consensus) {

    }

    @Override
    public EnteRepository getRepository() {
        return this.enteRepository;
    }

    @Override
    public EnteNode getUserByUsername(String username) throws NullPointerException {
        EnteNode user = this.enteRepository.findAll().stream().filter((u)-> u.getUsername().equals(username))
                .findFirst().orElseThrow(()-> new NullPointerException("No such user"));
        UserRoleNode enteRole = this.userRoleRepository.findByName("ENTE");
        if(user.getRoles().contains(enteRole)) return user;
        else throw new NullPointerException("User " + username + "has not role " + enteRole);
    }

    @Override
    public void login(String username, String password) {

    }

    @Override
    public void logout(UUID uuid) {

    }
}
