package com.example.EPPOI.service;

import com.example.EPPOI.dto.CoordsDTO;
import com.example.EPPOI.model.*;
import com.example.EPPOI.model.poi.PoiNode;
import com.example.EPPOI.model.user.EnteNode;
import com.example.EPPOI.model.user.ThirdUserNode;
import com.example.EPPOI.model.user.UserRoleNode;
import com.example.EPPOI.repository.*;
import com.example.EPPOI.utility.PoiForm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EnteServiceImpl implements EnteService {
    private final ItineraryRepository itineraryRepository;
    private final PoiRepository poiRepository;
    private final CityRepository cityRepository;
    private final EnteRepository enteRepository;

    private final UserNodeRepository userNodeRepository;
    private final UserRoleRepository userRoleRepository;

    private final ThirdRequestRegistrationRepository thirdRequestRegistrationRepository;
    private final PoiService poiService;

    @Override
    public PoiNode createPoi(EnteNode ente, PoiForm form) {
        CoordsDTO coordsDTO = form.getCoordinate();
        CoordsNode coords = new CoordsNode(coordsDTO.getLat(), coordsDTO.getLon());
        PoiNode result = new PoiNode(form.getName(), form.getDescription(), coords, form.getTimeToVisit());
        CityNode city = ente.getCity();
        this.poiService.setCityToPoi(result, city);
        return this.poiService.savePoi(result);
    }

    @Override
    public ItineraryNode createItinerary(EnteNode ente,String name, String description, List<PoiNode> POIs) {
        return null;
    }

    //TODO: to implement
    @Override
    public void setConsensus(ItineraryNode target, boolean consensus) {

    }

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
                    this.userNodeRepository.save(new ThirdUserNode(target.getName(),target.getSurname(),target.getEmail(),
                            target.getPassword(), target.getUsername(),role));
                }
            }
        } else{
            this.thirdRequestRegistrationRepository.delete(target);
        }
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

    /**
     log.info("start ");
     UserRoleNode touristRole = this.userRoleRepository.findByName("TOURIST");
     log.info("role {}",touristRole);
     TouristNode user = this.touristRepository.findAll().stream().filter((u)-> u.getUsername().equals(username))
     .findFirst().orElseThrow(()-> new NullPointerException("No such user"));
     log.info("user {}",user);
     if(user.getRoles().contains(touristRole)) return user;
     else throw new NullPointerException("User " + username + "has not role " + touristRole);
     */

    @Override
    public void login(String username, String password) {

    }

    @Override
    public void logout(UUID uuid) {

    }
}
