package com.example.EPPOI.service.dtoManager;

import com.example.EPPOI.dto.ContactDTO;
import com.example.EPPOI.model.ContactNode;
import com.example.EPPOI.repository.ContactRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ContactDtoManager implements DtoEntityManager<ContactNode, ContactDTO> {
    private final ContactRepository contactRepository;
    @Override
    public ContactNode getEntityfromDto(ContactDTO dto) throws NullPointerException {
        ContactNode result;
        if(Objects.isNull(dto.getId())) {
            result = new ContactNode(dto.getEmail(), dto.getCellNumber(), dto.getFax());
            this.contactRepository.save(result);
            return result;
        }
        return this.contactRepository.findById(dto.getId())
                .orElseThrow(()-> new NullPointerException("Contact not exists"));
    }

    @Override
    public ContactDTO getDtofromEntity(ContactNode entity) throws NullPointerException {
        if(Objects.isNull(entity)) throw new NullPointerException("Contact entity is null");
        return new ContactDTO(entity);
    }
}
