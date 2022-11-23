package com.example.EPPOI.service;

import com.example.EPPOI.model.PoiNode;
import org.springframework.stereotype.Service;

@Service
public interface PoiService {
    PoiNode findPoiById(Long id);
}
