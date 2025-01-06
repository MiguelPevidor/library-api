package io.github.miguelpevidor.libraryapi.controller.mappers;

import io.github.miguelpevidor.libraryapi.controller.dto.AutorDTO;
import io.github.miguelpevidor.libraryapi.model.Autor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AutorMapper {

    // Essa anotation é usada quando o objeto de origem, e o objeto que vai ser
    // mapeado, tem o mesmo campo, mas com nome diferente
    @Mapping(source = "nome", target = "nome" )
    @Mapping(source = "dataNascimento", target = "dataNascimento")
    Autor toEntity(AutorDTO dto);

    AutorDTO toDTO(Autor autor);
}
