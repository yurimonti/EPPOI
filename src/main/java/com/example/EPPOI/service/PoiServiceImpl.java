package com.example.EPPOI.service;

import com.example.EPPOI.model.PoiNode;
import com.example.EPPOI.repository.PoiRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PoiServiceImpl implements PoiService {
    private final PoiRepository poiRepository;
    @Override
    public PoiNode findPoiById(Long id) {
        return this.poiRepository.findById(id).orElseThrow(()->new NullPointerException("PoiNode not found"));
    }
}
