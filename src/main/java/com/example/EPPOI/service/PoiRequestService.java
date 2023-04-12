package com.example.EPPOI.service;

import com.example.EPPOI.dto.PoiRequestDTO;
import com.example.EPPOI.dto.ThirdPoiRequestDTO;
import com.example.EPPOI.model.RequestPoiNode;
import com.example.EPPOI.model.ThirdPartyPoiRequest;
import com.example.EPPOI.utility.PoiForm;
import org.springframework.stereotype.Service;

@Service
public interface PoiRequestService {

    RequestPoiNode getRequestById(Long idRequest) throws NullPointerException;
    void saveRequest(RequestPoiNode toSave);
    void deleteRequest(RequestPoiNode toDelete);
    RequestPoiNode createPoiRequestFromParams(PoiForm form);
    ThirdPartyPoiRequest createThirdPoiRequestFromParams(PoiForm form);
    PoiRequestDTO getDTOFromRequest(RequestPoiNode from);
    ThirdPoiRequestDTO getDTOFromThirdRequest(ThirdPartyPoiRequest from);
}
