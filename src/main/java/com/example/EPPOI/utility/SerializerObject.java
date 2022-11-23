package com.example.EPPOI.utility;

import java.util.Map;

public interface SerializerObject<T> {
    T deserializer(Map<String,Object> toDeserializer);
}
