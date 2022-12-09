package com.example.EPPOI.dto;

import com.example.EPPOI.model.AddressNode;
import lombok.Data;

@Data
public class AddressDTO {
    private Long id;
    private String street;
    private Integer number;

    public AddressDTO() {
        this.id = null;
        this.street = "";;
        this.number = null;
    }

    public AddressDTO(String street, Integer number) {
        this();
        this.street = street;
        this.number = number;
    }

    public AddressDTO(AddressNode address){
        this.id = address.getId();
        this.street = address.getStreet();;
        this.number = address.getNumber();
    }
}
