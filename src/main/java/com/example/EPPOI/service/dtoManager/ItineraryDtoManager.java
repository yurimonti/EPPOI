package com.example.EPPOI.service.dtoManager;

import com.example.EPPOI.dto.*;
import com.example.EPPOI.model.CategoryNode;
import com.example.EPPOI.model.CityNode;
import com.example.EPPOI.model.ItineraryNode;
import com.example.EPPOI.model.PoiTypeNode;
import com.example.EPPOI.service.CityService;
import com.example.EPPOI.service.PoiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItineraryDtoManager implements DtoEntityManager<ItineraryNode, ItineraryDTO> {
    private final CityService cityService;
    private final PoiService poiService;

    /*private List<CategoryDTO> distinctCategories(List<PoiDTO> targets){
        List<PoiTypeDTO> types = new ArrayList<>();
        List<CategoryDTO> categories = new ArrayList<>();
        targets.forEach(t-> types.addAll(t.getTypes()));
        types.forEach(t -> categories.addAll(t.getCategories()));
        return categories.stream().distinct().toList();
    }*/
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
        /*result.setCategories(this.distinctCategories(result.getPoints().stream().map(ItRelPoiDTO::getPoi).toList()));*/
        List<CityNode> cities = this.cityService.getCitiesByItinerary(entity);
        result.setCities(cities.stream().map(CityDTO::new).toList());
        return result;
    }
}
