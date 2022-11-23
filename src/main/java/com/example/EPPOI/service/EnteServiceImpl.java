package com.example.EPPOI.service;

import com.example.EPPOI.model.ItineraryNode;
import com.example.EPPOI.model.PoiNode;
import com.example.EPPOI.model.user.EnteNode;
import com.example.EPPOI.model.user.UserRoleNode;
import com.example.EPPOI.repository.*;
import com.example.EPPOI.utility.PoiParamsProvider;
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
    @Override
    public PoiNode createPoi(PoiParamsProvider params) {
        return null;
    }

    @Override
    public ItineraryNode createItinerary(String name, String description, List<PoiNode> POIs) {
        return null;
    }

    @Override
    public void setConsensus(ItineraryNode target, boolean consensus) {

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
