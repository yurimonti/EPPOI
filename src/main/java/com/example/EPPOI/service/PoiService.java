package com.example.EPPOI.service;

import com.example.EPPOI.dto.PoiDTO;
import com.example.EPPOI.dto.PoiRequestDTO;
import com.example.EPPOI.model.CityNode;
import com.example.EPPOI.model.RequestPoiNode;
import com.example.EPPOI.model.poi.PoiNode;
import com.example.EPPOI.utility.PoiForm;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PoiService {
    PoiNode findPoiById(Long id);

    void deletePoi(PoiNode toDelete) throws NullPointerException,IllegalArgumentException;

    PoiNode createPoiFromParams(PoiForm form);

    PoiNode setParamsToPoi(PoiNode target, PoiForm toSet);

    PoiNode poiFromRequest(RequestPoiNode toSet);

    void setCityToPoi(PoiNode poi,CityNode city);

    PoiDTO createDTOfromNode(PoiNode poi);

    PoiNode savePoi(PoiNode toSave);
    List<PoiNode> getAllPois();
}
