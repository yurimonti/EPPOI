package com.example.EPPOI.dto;

import com.example.EPPOI.model.ContactNode;
import lombok.Data;

@Data
public class ContactDTO {
    private Long id;
    private String email;
    private String cellNumber;
    private String fax;


    public ContactDTO(){
        this.id = null;
        this.cellNumber = "";
        this.email = "";
        this.fax = "";
    }

    public ContactDTO(String email, String cellNumber, String fax) {
        this();
        this.email = email;
        this.cellNumber = cellNumber;
        this.fax = fax;
    }

    public ContactDTO(ContactNode contact){
        this.id = contact.getId();
        this.cellNumber = contact.getCellNumber();
        this.email = contact.getEmail();
        this.fax = contact.getFax();
    }
}
