package com.example.EPPOI.service;

import com.example.EPPOI.dto.*;
import com.example.EPPOI.model.*;
import com.example.EPPOI.model.poi.PoiNode;
import com.example.EPPOI.model.user.TouristNode;
import com.example.EPPOI.model.user.UserNode;
import com.example.EPPOI.model.user.UserRoleNode;
import com.example.EPPOI.repository.RequestPoiRepository;
import com.example.EPPOI.service.dtoManager.DtoEntityManager;
import com.example.EPPOI.utility.PoiForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class PoiRequestServiceImpl implements PoiRequestService {
    private final GeneralUserService generalUserService;
    private final RequestPoiRepository requestPoiRepository;
    private final CityService cityService;
    private final DtoEntityManager<PoiTagRel, PoiTagRelDTO> poiTagRelDTOManager;
    private final DtoEntityManager<CoordsNode, CoordsDTO> coordsDtoManager;
    private final DtoEntityManager<PoiTypeNode, PoiTypeDTO> typeDtoManager;
    private final DtoEntityManager<AddressNode, AddressDTO> addressDtoManager;
    private final DtoEntityManager<ContactNode, ContactDTO> contactDtoManager;
    private final DtoEntityManager<TimeSlotNode, TimeSlotDTO> timeDtoManager;

    @Override
    public RequestPoiNode getRequestById(Long idRequest) throws NullPointerException {
        return this.requestPoiRepository.findById(idRequest)
                .orElseThrow(()-> new NullPointerException("No such request with id: "+idRequest));
    }

    @Override
    public void saveRequest(RequestPoiNode toSave) {
        this.requestPoiRepository.save(toSave);
    }

    @Override
    public void deleteRequest(RequestPoiNode toDelete) {
        this.requestPoiRepository.delete(toDelete);
    }

    @Override
    public RequestPoiNode createPoiRequestFromParams(PoiForm form) {
        RequestPoiNode result = new RequestPoiNode();
        result.setName(form.getName());
        result.setDescription(form.getDescription());
        result.setTimeToVisit(form.getTimeToVisit());
        result.setTicketPrice(form.getTicketPrice());
        log.info("settaggio base {} {} {} {}", result.getName(), result.getDescription(), result.getTimeToVisit()
                , result.getTicketPrice());
        result.setCoordinate(this.coordsDtoManager.getEntityfromDto(form.getCoordinate()));
        log.info("settaggio coords {}", result.getCoordinate());
        form.getTagValues().forEach(t -> result.getTagValues().add(this.poiTagRelDTOManager.getEntityfromDto(t)));
        log.info("settaggio tagValues {}", result.getTagValues());
        form.getTypes().forEach(t -> result.getTypes().add(this.typeDtoManager.getEntityfromDto(t)));
        log.info("settaggio types {}", result.getTypes());
        result.setAddress(this.addressDtoManager.getEntityfromDto(form.getAddress()));
        log.info("settaggio address {}", result.getAddress());
        result.setContact(this.contactDtoManager.getEntityfromDto(form.getContact()));
        log.info("settaggio contact {}", result.getContact());
        result.setHours(this.timeDtoManager.getEntityfromDto(form.getTimeSlot()));
        log.info("settaggio time {}", result.getHours());
        this.requestPoiRepository.save(result);
        log.info("request created : {}", result);
        return result;
    }

    @Override
    public PoiRequestDTO getDTOfromRequest(RequestPoiNode from) {
        /*List<? extends UserNode> t = this.generalUserService.getUsers()
                .stream()
                .filter(u -> u.getRoles().stream().map(UserRoleNode::getName).toList().contains("TOURIST"))
                .toList();
        String username = t.stream()
                .map(u -> (TouristNode) u)
                .filter(u -> u.getPoiRequests()
                        .stream()
                        .map(RequestPoiNode::getId)
                        .anyMatch(r -> r.equals(from.getId())))
                .findFirst()
                .orElseThrow(NullPointerException::new)
                .getUsername();*/
        //String username = from.getMadeBy().getUsername();
        String username = from.getCreatedBy();
        log.info("from poiRequest is {}",from);
        CityDTO cityDto = null;
        PoiDTO target = null;
        if (!Objects.isNull(from.getTarget())) {
            cityDto = new CityDTO(this.cityService.getCityByPoi(from.getTarget().getId()));
            target = new PoiDTO(from.getTarget());;
        }
        List<PoiTagRelDTO> tagRelDTOs = from.getTagValues().stream().map(PoiTagRelDTO::new).toList();
        List<PoiTypeDTO> poiTypeDTOs = from.getTypes().stream().map(PoiTypeDTO::new).toList();
        StatusEnum status;
        Boolean isAccepted = from.getIsAccepted();
        if (Objects.isNull(isAccepted)) status = StatusEnum.PENDING;
        else {
            if (isAccepted) {
                status = StatusEnum.ACCEPTED;
            } else status = StatusEnum.REJECTED;
        }
        return new PoiRequestDTO(from.getId(), status, from.getName(), from.getDescription(), cityDto,
                new CoordsDTO(from.getCoordinate()), new TimeSlotDTO(from.getHours()), from.getTimeToVisit(),
                new AddressDTO(from.getAddress()), from.getTicketPrice(), username, target, poiTypeDTOs,
                new ContactDTO(from.getContact()), tagRelDTOs);
    }

    @Override
    public RequestPoiNode getRequestfromDTO(PoiRequestDTO from) {

        return null;
    }
}
