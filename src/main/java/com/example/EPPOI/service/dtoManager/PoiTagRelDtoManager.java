package com.example.EPPOI.service.dtoManager;

import com.example.EPPOI.dto.PoiTagRelDTO;
import com.example.EPPOI.model.PoiTagRel;
import com.example.EPPOI.repository.TagRepository;
import com.example.EPPOI.service.dtoManager.DtoEntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class PoiTagRelDtoManager implements DtoEntityManager<PoiTagRel,PoiTagRelDTO> {
    private final TagRepository tagRepository;
    @Override
    public PoiTagRel getEntityfromDto(PoiTagRelDTO dto) throws NullPointerException{
        PoiTagRel rel = new PoiTagRel(this.tagRepository.findById(dto.getTag().getId())
                .orElseThrow(() -> new NullPointerException("This type not exists")));
        if (dto.getTag().getIsBooleanType()) rel.setBooleanValue(dto.getBooleanValue());
        else rel.setStringValue(dto.getStringValue());
        return rel;
    }

    @Override
    public PoiTagRelDTO getDtofromEntity(PoiTagRel entity) throws NullPointerException {
        if(Objects.isNull(entity)) throw new NullPointerException("PoiTagRel entity is null");
        return new PoiTagRelDTO(entity);
    }
}
