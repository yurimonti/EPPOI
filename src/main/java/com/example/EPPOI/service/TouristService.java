package com.example.EPPOI.service;

import com.example.EPPOI.dto.PoiRequestDTO;
import com.example.EPPOI.model.ItineraryNode;
import com.example.EPPOI.model.RequestPoiNode;
import com.example.EPPOI.model.user.TouristNode;
import com.example.EPPOI.repository.TouristRepository;
import com.example.EPPOI.utility.ItineraryForm;
import com.example.EPPOI.utility.PoiForm;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public interface TouristService extends AppUserService<TouristNode> {
    ItineraryNode createItinerary(TouristNode creator, ItineraryForm form);
    RequestPoiNode createRequestPoi(TouristNode creator, PoiForm params, Long cityId);

    List<PoiRequestDTO> getAllRequestDTOs(TouristNode tourist);

    void deletePoiRequest(TouristNode tourist,Long idRequest) throws NullPointerException;
    TouristRepository getRepository();

}
