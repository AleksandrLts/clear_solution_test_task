package com.example.clearsolutiontesttask.service.mapper;

public interface DtoMapper<D, R, M> {
    M mapToModel(D dto);

    R mapToDto(M model);
}
