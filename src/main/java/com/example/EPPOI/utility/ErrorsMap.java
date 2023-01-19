package com.example.EPPOI.utility;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data
@AllArgsConstructor
public class ErrorsMap {
    private Map<String,String> errors;

    public ErrorsMap() {
        this.errors = new HashMap<>();
    }

    public ErrorsMap(String message){
        this.errors = new HashMap<>();
        this.errors.put("error",message);
    }
}
