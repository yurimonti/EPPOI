package com.example.EPPOI.service.dtoManager;

import com.example.EPPOI.dto.CategoryDTO;
import com.example.EPPOI.dto.CityDTO;
import com.example.EPPOI.dto.ItRelPoiDTO;
import com.example.EPPOI.dto.ItineraryDTO;
import com.example.EPPOI.model.CityNode;
import com.example.EPPOI.model.ItineraryNode;
import com.example.EPPOI.service.CityService;
import com.example.EPPOI.service.PoiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItineraryDtoManager implements DtoEntityManager<ItineraryNode, ItineraryDTO> {
    private final CityService cityService;
    private final PoiService poiService;
    @Override
    public ItineraryDTO getDtofromEntity(ItineraryNode entity) throws NullPointerException {
        /*List<ItRelPoiDTO> pois = entity.getPoints().stream().map(i ->
                        new ItRelPoiDTO(i.getId(), this.poiService.createDTOfromNode(i.getPoi()), i.getIndex()))
                .toList();
        List<CategoryDTO> categories = entity.getCategories().stream().map(CategoryDTO::new).toList();
        List<CityDTO> cities = this.cityService.getCitiesByItinerary(entity).stream().map(CityDTO::new).toList();
        ItineraryDTO result = new ItineraryDTO(entity.getId(), entity.getName(), entity.getDescription(),
                entity.getCreatedBy(), entity.getIsDefault(),cities,pois,categories,
                entity.getTimeToVisit(), entity.getGeoJsonList());
        return result;*/
        ItineraryDTO result = new ItineraryDTO(entity);
        log.info("pre cities {}",result.getCities());
        List<CityNode> cities = this.cityService.getCitiesByItinerary(entity);
        log.info("post cities {}",cities);
        result.setCities(cities.stream().map(CityDTO::new).toList());
        log.info("post set cities {}",result.getCities());
        return result;
    }
}
