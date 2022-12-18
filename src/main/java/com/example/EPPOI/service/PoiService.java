package com.example.EPPOI.service;

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

    PoiNode createPoiFromParams(PoiForm form);

    PoiNode setParamsToPoi(PoiNode target, PoiForm toSet);

    PoiNode poiFromRequest(RequestPoiNode toSet);

    void setCityToPoi(PoiNode poi,CityNode city);

    PoiNode savePoi(PoiNode toSave);
    List<PoiNode> getAllPois();
}
