package com.example.EPPOI.service.dtoManager;

import com.example.EPPOI.dto.CoordsDTO;
import com.example.EPPOI.model.CoordsNode;
import com.example.EPPOI.repository.CoordsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CoordsDtoManager implements DtoEntityManager<CoordsNode, CoordsDTO>{
    private final CoordsRepository coordsRepository;
    @Override
    public CoordsNode getEntityfromDto(CoordsDTO dto) throws NullPointerException {
        CoordsNode result;
        if(Objects.isNull(dto.getId())) {
            result = new CoordsNode(dto.getLat(), dto.getLon());
            this.coordsRepository.save(result);
            return result;
        }
        result = this.coordsRepository.findById(dto.getId()).orElseThrow(NullPointerException::new);
        return result;
    }

    @Override
    public CoordsDTO getDtofromEntity(CoordsNode entity) throws NullPointerException {
        if(Objects.isNull(entity)) throw new NullPointerException("Coords entity is null");
        return new CoordsDTO(entity);
    }
}
