package com.example.EPPOI.service;

import com.example.EPPOI.model.CityNode;
import com.example.EPPOI.model.poi.PoiNode;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PoiService {
    PoiNode findPoiById(Long id);

    void setCityToPoi(PoiNode poi,CityNode city);

    PoiNode savePoi(PoiNode toSave);
    List<PoiNode> getAllPois();
}
