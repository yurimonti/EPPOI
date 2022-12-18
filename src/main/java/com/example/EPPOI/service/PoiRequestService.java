package com.example.EPPOI.service;

import com.example.EPPOI.dto.PoiRequestDTO;
import com.example.EPPOI.model.RequestPoiNode;
import com.example.EPPOI.utility.PoiForm;
import org.springframework.stereotype.Service;

@Service
public interface PoiRequestService {

    RequestPoiNode getRequestById(Long idRequest) throws NullPointerException;
    void saveRequest(RequestPoiNode toSave);
    RequestPoiNode createPoiRequestFromParams(PoiForm form);
    PoiRequestDTO getDTOfromRequest(RequestPoiNode from);
    RequestPoiNode getRequestfromDTO(PoiRequestDTO from);
}
