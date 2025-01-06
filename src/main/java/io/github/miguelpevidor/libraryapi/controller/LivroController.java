package io.github.miguelpevidor.libraryapi.controller;

import io.github.miguelpevidor.libraryapi.controller.dto.CadastroLivroDTO;
import io.github.miguelpevidor.libraryapi.controller.dto.ErroResposta;
import io.github.miguelpevidor.libraryapi.controller.mappers.LivroMapper;
import io.github.miguelpevidor.libraryapi.exceptions.RegistroDuplicadoException;
import io.github.miguelpevidor.libraryapi.model.Livro;
import io.github.miguelpevidor.libraryapi.service.LivroService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;


@RestController
@RequestMapping("livros")
public class LivroController implements GenericController {

    @Autowired
    private LivroService service;

    @Autowired
    private LivroMapper mapper;

    @PostMapping
    public ResponseEntity<Object> salvar(@RequestBody @Valid CadastroLivroDTO dto) {

        Livro livro = mapper.toEntity(dto);
        livro = service.salvar(livro);

        //http://localhost:8080/autores/f235621f-4200-4b8c-824c-1ca568ae4b82
        URI location = gerarHeaderLocation(livro.getId());
        return ResponseEntity.created(location).build();

    }
}
