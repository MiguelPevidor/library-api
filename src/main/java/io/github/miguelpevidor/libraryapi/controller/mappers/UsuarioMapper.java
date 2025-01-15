package io.github.miguelpevidor.libraryapi.controller.mappers;

import io.github.miguelpevidor.libraryapi.controller.dto.UsuarioDTO;
import io.github.miguelpevidor.libraryapi.model.Usuario;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {

    Usuario toEntity(UsuarioDTO dto);
}
