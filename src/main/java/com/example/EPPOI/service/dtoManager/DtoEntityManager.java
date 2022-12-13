package com.example.EPPOI.service.dtoManager;

import org.springframework.stereotype.Service;

@Service
public interface DtoEntityManager<T,E> {
    T getEntityfromDto(E dto) throws NullPointerException;
    E getDtofromEntity(T entity) throws NullPointerException;
}
