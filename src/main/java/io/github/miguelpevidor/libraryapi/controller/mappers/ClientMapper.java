package io.github.miguelpevidor.libraryapi.controller.mappers;

import io.github.miguelpevidor.libraryapi.controller.dto.ClientDTO;
import io.github.miguelpevidor.libraryapi.model.Client;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ClientMapper {

    Client toEntity(ClientDTO dto);
    ClientDTO toDTO(Client entity);
}
