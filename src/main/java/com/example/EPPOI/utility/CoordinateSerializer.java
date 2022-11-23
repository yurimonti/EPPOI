package com.example.EPPOI.utility;

import com.example.EPPOI.dto.CoordsDTO;
import com.example.EPPOI.model.CoordsNode;

import java.util.Map;

public class CoordinateSerializer implements SerializerObject<CoordsDTO> {
    @Override
    public CoordsDTO deserializer(Map<String,Object> toDeserializer) {
        Double lat = Double.parseDouble((String)toDeserializer.get("lat"));
        Double lon = Double.parseDouble((String)toDeserializer.get("lon"));
        return new CoordsDTO(lat,lon);
    }
}
