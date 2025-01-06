package io.github.miguelpevidor.libraryapi.controller.mappers;

import io.github.miguelpevidor.libraryapi.controller.dto.CadastroLivroDTO;
import io.github.miguelpevidor.libraryapi.controller.dto.ResultadopesquisaLivroDTO;
import io.github.miguelpevidor.libraryapi.model.Livro;
import io.github.miguelpevidor.libraryapi.repository.AutorRepository;
import io.github.miguelpevidor.libraryapi.repository.LivroRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class LivroMapper {

   @Autowired
   AutorRepository autorRepository;

   @Mapping(expression = "java(autorRepository.findById(dto.idAutor()).orElse(null))", target = "autor" )
   public abstract Livro toEntity(CadastroLivroDTO dto);

   abstract Livro toEntity(ResultadopesquisaLivroDTO dto);

   abstract CadastroLivroDTO toCadastroLivroDTO(Livro livro);

   abstract ResultadopesquisaLivroDTO toResultadopesquisaLivroDTO(Livro livro);
}
