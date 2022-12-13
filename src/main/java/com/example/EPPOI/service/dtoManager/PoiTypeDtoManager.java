package com.example.EPPOI.service.dtoManager;

import com.example.EPPOI.dto.PoiTypeDTO;
import com.example.EPPOI.model.PoiTypeNode;
import com.example.EPPOI.repository.PoiTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class PoiTypeDtoManager implements DtoEntityManager<PoiTypeNode, PoiTypeDTO> {
    private final PoiTypeRepository poiTypeRepository;
    @Override
    public PoiTypeNode getEntityfromDto(PoiTypeDTO dto) throws NullPointerException {
        return this.poiTypeRepository.findById(dto.getId())
                .orElseThrow(() -> new NullPointerException("This type not exists"));
    }

    @Override
    public PoiTypeDTO getDtofromEntity(PoiTypeNode entity) throws NullPointerException {
        if(Objects.isNull(entity)) throw new NullPointerException("PoiType entity is null");
        return new PoiTypeDTO(entity);
    }
}
