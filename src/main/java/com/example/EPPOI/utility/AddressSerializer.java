package com.example.EPPOI.utility;

import com.example.EPPOI.model.AddressNode;

import java.util.Map;

public class AddressSerializer implements SerializerObject<AddressNode> {
    @Override
    public AddressNode deserializer(Map<String, Object> toDeserializer) {
        String street = (String) toDeserializer.get("street");
        Integer number = Integer.parseInt((String) toDeserializer.get("number"));
        return new AddressNode(street,number);
    }
}
