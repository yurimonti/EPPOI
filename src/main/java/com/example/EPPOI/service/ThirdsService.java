package com.example.EPPOI.service;


import com.example.EPPOI.dto.ThirdPoiRequestDTO;
import com.example.EPPOI.model.RequestPoiNode;
import com.example.EPPOI.model.ThirdPartyPoiRequest;
import com.example.EPPOI.model.poi.PoiNode;
import com.example.EPPOI.model.user.EnteNode;
import com.example.EPPOI.model.user.ThirdUserNode;
import com.example.EPPOI.repository.ThirdRepository;
import com.example.EPPOI.utility.PoiForm;
import com.example.EPPOI.utility.PoiFormThirds;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ThirdsService {

    ThirdPartyPoiRequest createRequestPoi(ThirdUserNode thirdUserNode, PoiForm form, Long cityId);

    ThirdRepository getRepository();

    PoiNode modifyPoi(ThirdUserNode thirdUserNode, PoiForm form, Long idPoi);

    PoiNode modifyPoi(ThirdUserNode thirdUserNode, PoiFormThirds secondaryForm, Long idPoi);

    boolean isThirdUser(String createdBy);

    PoiNode createPoiFromRequest(ThirdPartyPoiRequest toSet);

    //PoiNode createThirdPoiFromForm(EnteNode ente,RequestPoiNode toSet, PoiForm form);

    List<ThirdPoiRequestDTO> getAllRequestDTOs(ThirdUserNode third);

    ThirdPartyPoiRequest getRequestById(Long idRequest) throws NullPointerException;

    void saveRequest(ThirdPartyPoiRequest toSet);
}