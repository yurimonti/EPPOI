package com.example.EPPOI.service.dtoManager;

import com.example.EPPOI.dto.CityDTO;
import com.example.EPPOI.dto.ItineraryDTO;
import com.example.EPPOI.dto.ItineraryRequestDTO;
import com.example.EPPOI.model.CityNode;
import com.example.EPPOI.model.ItineraryRelPoi;
import com.example.EPPOI.model.ItineraryRequestNode;
import com.example.EPPOI.model.poi.PoiNode;
import com.example.EPPOI.service.CityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/*@Service
@RequiredArgsConstructor
@Slf4j*/
public class ItineraryRequestDtoManager /*implements DtoEntityManager<ItineraryRequestNode, ItineraryRequestDTO> */{
    /*private final CityService cityService;

    @Override
    public ItineraryRequestDTO getDtofromEntity(ItineraryRequestNode entity) throws NullPointerException {
        ItineraryRequestDTO result = new ItineraryRequestDTO(entity);
        log.info("pre cities {}",result.getCities());
        List<CityNode> cities = this.cityService.getCitiesByPoi(
                entity.getPoints().stream().map(ItineraryRelPoi::getPoi).toList());
        log.info("post cities {}",cities);
        result.setCities(cities.stream().map(CityDTO::new).toList());
        log.info("post set cities {}",result.getCities());
        return result;
    }*/
}
