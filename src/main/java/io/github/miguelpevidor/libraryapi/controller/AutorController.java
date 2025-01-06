package io.github.miguelpevidor.libraryapi.controller;

import io.github.miguelpevidor.libraryapi.controller.dto.AutorDTO;
import io.github.miguelpevidor.libraryapi.controller.dto.ErroResposta;
import io.github.miguelpevidor.libraryapi.controller.mappers.AutorMapper;
import io.github.miguelpevidor.libraryapi.exceptions.OperacaoNaoPermitidaException;
import io.github.miguelpevidor.libraryapi.exceptions.RegistroDuplicadoException;
import io.github.miguelpevidor.libraryapi.model.Autor;
import io.github.miguelpevidor.libraryapi.service.AutorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("autores")
// http://localhost:8080/autores

public class AutorController implements GenericController{

    @Autowired
    private AutorService service;

    @Autowired
    private AutorMapper mapper;

    @PostMapping
    public ResponseEntity<Object> salvar(@RequestBody @Valid AutorDTO dto){
            Autor autor = mapper.toEntity(dto);
            service.salvar(autor);

            //http://localhost:8080/autores/f235621f-4200-4b8c-824c-1ca568ae4b82
            URI location = gerarHeaderLocation(autor.getId());
            return ResponseEntity.created(location).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<AutorDTO> obterDetahes(@PathVariable String id){
        UUID idAutor = UUID.fromString(id);
        Optional<Autor> autorOptional = service.obterAutorPorId(idAutor);

        return service.obterAutorPorId(idAutor)
                .map(autor -> {
                    AutorDTO dto = mapper.toDTO(autor);
                    return ResponseEntity.ok(dto);
        }).orElseGet(()->{
            return ResponseEntity.notFound().build();
        });

    }

    @DeleteMapping("{id}")
    public ResponseEntity<Object> excluirAutor(@PathVariable String id){

        UUID idAutor = UUID.fromString(id);
        Optional<Autor> autorOptional = service.obterAutorPorId(idAutor);
        if (autorOptional.isPresent()) {
            service.excluirAutor(autorOptional.get());
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping()
    public ResponseEntity<List<AutorDTO>> pesquisarAutores(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false)String nacionalidade){

        List<Autor> resultado = service.pesquisarPorExample(nome, nacionalidade);
        List<AutorDTO> lista = resultado
                .stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(lista);
    }

    @PutMapping("{id}")
    public ResponseEntity<Object> atualizar(@PathVariable String id, @RequestBody @Valid AutorDTO dto){

        UUID idAutor = UUID.fromString(id);
        Optional<Autor> autorOptional = service.obterAutorPorId(idAutor);

        //Autor não foi achado
        if(autorOptional.isEmpty()){
            return ResponseEntity.notFound().build();
        }

        Autor autor = autorOptional.get();
        autor.setNome(dto.nome());
        autor.setNacionalidade(dto.nacionalidade());
        autor.setDataNascimento(dto.dataNascimento());

        service.atualizar(autor);

        return ResponseEntity.noContent().build();
    }
}
