package com.example.EPPOI.service;

import com.example.EPPOI.dto.ItRelPoiDTO;
import com.example.EPPOI.dto.ItineraryRequestDTO;
import com.example.EPPOI.model.*;
import com.example.EPPOI.model.poi.PoiNode;
import com.example.EPPOI.model.user.EnteNode;
import com.example.EPPOI.repository.CategoryRepository;
import com.example.EPPOI.repository.ItineraryRepository;
import com.example.EPPOI.repository.ItineraryRequestRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItineraryServiceImpl implements ItineraryService{
    private final ItineraryRepository itineraryRepository;
    private final ItineraryRequestRepository itineraryRequestRepository;

    private final CategoryRepository categoryRepository;

    private final CityService cityService;

    private List<CategoryNode> distinctCategories(List<PoiNode> targets){
        List<PoiTypeNode> types = new ArrayList<>();
        List<CategoryNode> categories = new ArrayList<>();
        targets.forEach(t-> types.addAll(t.getTypes()));
        types.forEach(t -> categories.addAll(t.getCategories()));
        return categories.stream().map(CategoryNode::getId).distinct().map(this.categoryRepository::findById)
                .map(Optional::get).toList();
    }

    @Override
    public void fillItinerary(ItineraryNode toFill, List<PoiNode> target) {
        for(int i = 1; i<=target.size();i++){
            PoiNode toAdd = target.get(i-1);
            toFill.getPoints().add(new ItineraryRelPoi(toAdd,i));
        }
        toFill.setCategories(this.distinctCategories(target));
        this.itineraryRepository.save(toFill);
    }

    @Override
    public ItineraryNode getItinerary(Long id) throws NullPointerException {
        return this.itineraryRepository.findById(id)
                .orElseThrow(()-> new NullPointerException("itinerary not found"));
    }

    @Override
    public void deleteItinerary(List<ItineraryNode> toDelete) {
        toDelete.forEach(this::deleteItinerary);
    }

    @Override
    public void deleteItinerary(ItineraryNode toDelete) {
        this.itineraryRepository.delete(toDelete);
    }

    @Override
    public List<ItineraryNode> getItinerariesFromPoi(PoiNode poi) {
        return this.itineraryRepository.findAll()
                .stream()
                .filter(i -> i.getPoints()
                        .stream()
                        .map(ItineraryRelPoi::getPoi)
                        .map(PoiNode::getId)
                        .anyMatch(p-> p.equals(poi.getId())))
                .toList();
    }

    @Override
    public void fillItinerary(ItineraryRequestNode toFill, List<PoiNode> target) {
        for(int i = 1; i<=target.size();i++){
            PoiNode toAdd = target.get(i-1);
            toFill.getPoints().add(new ItineraryRelPoi(toAdd,i));
        }
        toFill.setCategories(this.distinctCategories(target));
        this.itineraryRequestRepository.save(toFill);
    }

    @Override
    public void saveItinerary(ItineraryNode toSave) {
        if(Objects.isNull(toSave)) throw new IllegalArgumentException("the entity to save is null");
        this.itineraryRepository.save(toSave);
    }

    @Override
    public void saveItinerary(ItineraryRequestNode toSave) {
        if(Objects.isNull(toSave)) throw new IllegalArgumentException("the entity to save is null");
        this.itineraryRequestRepository.save(toSave);
    }
    @Override
    public ItineraryNode createBaseItinerary(String name,String description,List<PoiNode> points,
                                             List<String> geoJsonList,boolean isDefault){
        ItineraryNode result = new ItineraryNode(name,description);
        result.setGeoJsonList(geoJsonList);
        result.setIsDefault(isDefault);
        result.setTimeToVisit(points.stream().map(PoiNode::getTimeToVisit).reduce(0.0,Double::sum));
        this.fillItinerary(result,points);
        return result;
    }

    @Override
    public ItineraryNode createItineraryFromRequest(ItineraryRequestNode request) {
        ItineraryNode result = new ItineraryNode(request.getName(),request.getDescription());
        result.setIsDefault(true);
        result.setGeoJsonList(request.getGeoJsonList());
        request.getPoints().forEach(p -> result.getPoints().add(new ItineraryRelPoi(p.getPoi(),p.getIndex())));
        result.setCategories(request.getCategories());
        result.setTimeToVisit(request.getTimeToVisit());
        return result;
    }
}
