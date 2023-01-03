package com.example.EPPOI.service.dtoManager;

import org.springframework.stereotype.Service;

@Service
public interface DtoEntityManager<T,E> {
    default T getEntityfromDto(E dto) throws NullPointerException{
        return null;
    };
    E getDtofromEntity(T entity) throws NullPointerException;
}
