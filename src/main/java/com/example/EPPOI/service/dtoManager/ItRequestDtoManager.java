package com.example.EPPOI.service.dtoManager;

import com.example.EPPOI.dto.CategoryDTO;
import com.example.EPPOI.dto.CityDTO;
import com.example.EPPOI.dto.ItRelPoiDTO;
import com.example.EPPOI.dto.ItineraryRequestDTO;
import com.example.EPPOI.model.CityNode;
import com.example.EPPOI.model.ItineraryRelPoi;
import com.example.EPPOI.model.ItineraryRequestNode;
import com.example.EPPOI.service.CityService;
import com.example.EPPOI.service.PoiService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItRequestDtoManager implements DtoEntityManager<ItineraryRequestNode, ItineraryRequestDTO> {
    private final PoiService poiService;
    private final CityService cityService;

    @Override
    public ItineraryRequestNode getEntityfromDto(ItineraryRequestDTO dto) throws NullPointerException {
        return null;
    }

    @Override
    public ItineraryRequestDTO getDtofromEntity(ItineraryRequestNode entity) throws NullPointerException {
        ItineraryRequestDTO result = new ItineraryRequestDTO();
        result.setId(entity.getId());
        result.setName(entity.getName());
        result.setDescription(entity.getDescription());
        result.setCreatedBy(entity.getCreatedBy());
        result.setConsensus(entity.getConsensus());
        result.setAccepted(entity.getAccepted());
        result.setPoints(entity.getPoints().stream().map(i ->
                        new ItRelPoiDTO(i.getId(), this.poiService.createDTOfromNode(i.getPoi()), i.getIndex()))
                .toList());
        result.setTimeToVisit(entity.getTimeToVisit());
        result.setCategories(entity.getCategories().stream().map(CategoryDTO::new).toList());
        result.setGeoJsonList(entity.getGeoJsonList());
        List<CityNode> cities = this.cityService.getCitiesByPoi(
                entity.getPoints().stream().map(ItineraryRelPoi::getPoi).toList());
        result.setCities(cities.stream().map(CityDTO::new).toList());
        return result;
    }
}
