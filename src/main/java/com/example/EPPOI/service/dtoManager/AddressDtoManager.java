package com.example.EPPOI.service.dtoManager;

import com.example.EPPOI.dto.AddressDTO;
import com.example.EPPOI.dto.CoordsDTO;
import com.example.EPPOI.model.AddressNode;
import com.example.EPPOI.model.CoordsNode;
import com.example.EPPOI.repository.AddressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;
@Service
@RequiredArgsConstructor
public class AddressDtoManager implements DtoEntityManager<AddressNode, AddressDTO>{
    private final AddressRepository addressRepository;
    @Override
    public AddressNode getEntityfromDto(AddressDTO dto) throws NullPointerException {
        AddressNode result;
        if(Objects.isNull(dto.getId())) {
            result = new AddressNode(dto.getStreet(), dto.getNumber());
            this.addressRepository.save(result);
            return result;
        }
        return this.addressRepository.findById(dto.getId())
                .orElseThrow(()-> new NullPointerException("Address not exists"));
    }

    @Override
    public AddressDTO getDtofromEntity(AddressNode entity) throws NullPointerException {
        if(Objects.isNull(entity)) throw new NullPointerException("Address entity is null");
        return new AddressDTO(entity);
    }
}
