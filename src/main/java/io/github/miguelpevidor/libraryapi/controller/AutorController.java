package io.github.miguelpevidor.libraryapi.controller;

import io.github.miguelpevidor.libraryapi.controller.dto.AutorDTO;
import io.github.miguelpevidor.libraryapi.model.Autor;
import io.github.miguelpevidor.libraryapi.service.AutorService;
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

public class AutorController {

    private AutorService service;

    public AutorController(AutorService service){
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Void> salvar(@RequestBody AutorDTO dto){
        Autor autor = dto.mapearParaAutor();
        service.salvar(autor);

        //http://localhost:8080/autores/f235621f-4200-4b8c-824c-1ca568ae4b82
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(autor.getId())
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<AutorDTO> obterDetahes(@PathVariable String id){
        UUID idAutor = UUID.fromString(id);
        Optional<Autor> autorOptional = service.obterAutorPorId(idAutor);
        if(autorOptional.isPresent()){
            Autor autor = autorOptional.get();
            AutorDTO dto = new AutorDTO(autor.getId(),autor.getNome(),autor.getDataNascimento(),autor.getNacionalidade());
            return ResponseEntity.ok(dto);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> excluirAutor(@PathVariable String id){
        UUID idAutor = UUID.fromString(id);
        Optional<Autor> autorOptional = service.obterAutorPorId(idAutor);
        if(autorOptional.isPresent()){
            service.excluirAutor(idAutor);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping()
    public ResponseEntity<List<AutorDTO>> pesquisarAutores(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false)String nacionalidade){
        List<Autor> resultado = service.pesquisar(nome, nacionalidade);
        List<AutorDTO> lista = resultado.stream().map(autor -> new AutorDTO(
                                                        autor.getId(),
                                                        autor.getNome(),
                                                        autor.getDataNascimento(),
                                                        autor.getNacionalidade()
                                                    )).collect(Collectors.toList());
        return ResponseEntity.ok(lista);
    }

    @PutMapping("{id}")
    public ResponseEntity<Void> atualizar(@PathVariable String id, @RequestBody AutorDTO dto){
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
