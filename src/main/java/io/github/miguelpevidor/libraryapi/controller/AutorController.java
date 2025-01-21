package io.github.miguelpevidor.libraryapi.controller;

import io.github.miguelpevidor.libraryapi.controller.dto.AutorDTO;
import io.github.miguelpevidor.libraryapi.controller.dto.ErroResposta;
import io.github.miguelpevidor.libraryapi.controller.mappers.AutorMapper;
import io.github.miguelpevidor.libraryapi.exceptions.OperacaoNaoPermitidaException;
import io.github.miguelpevidor.libraryapi.exceptions.RegistroDuplicadoException;
import io.github.miguelpevidor.libraryapi.model.Autor;
import io.github.miguelpevidor.libraryapi.service.AutorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@PreAuthorize("hasRole('GERENTE')")
@RequestMapping("autores")
@Tag(name = "Autores")
// http://localhost:8080/autores

public class AutorController implements GenericController{

    @Autowired
    private AutorService service;

    @Autowired
    private AutorMapper mapper;

    @PostMapping
    @Operation(summary = "Salvar", description = "Cadastrar novo autor")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Cadastrado com sucesso"),
            @ApiResponse(responseCode = "422", description = "Erro de Validação"),
            @ApiResponse(responseCode = "409", description = "Autor já cadastrado")
    })
    public ResponseEntity<Void> salvar(@RequestBody @Valid AutorDTO dto){
            Autor autor = mapper.toEntity(dto);
            service.salvar(autor);

            //http://localhost:8080/autores/f235621f-4200-4b8c-824c-1ca568ae4b82
            URI location = gerarHeaderLocation(autor.getId());
            return ResponseEntity.created(location).build();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('OPERADOR','GERENTE')")
    @Operation(summary = "Obter Detalhes", description = "Retorna os dados do autor pelo ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Autor encontrado"),
            @ApiResponse(responseCode = "404", description = "Autor não encontrado")
    })
    public ResponseEntity<AutorDTO> obterDetahes(@PathVariable String id){
        UUID idAutor = UUID.fromString(id);
        Optional<Autor> autorOptional = service.obterAutorPorId(idAutor);

        Optional<AutorDTO> autorDTO = service.obterAutorPorId(idAutor)
                .map(mapper::toDTO);

        return autorDTO.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());

    }

    @GetMapping()
    @PreAuthorize("hasAnyRole('OPERADOR','GERENTE')")
    @Operation(summary = "Pesquisar", description = "Realiza pesquisa de Autores por parametros")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Sucesso")
    })
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

    @DeleteMapping("{id}")
    @Operation(summary = "Deletar", description = "Deleta um autor existente")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Deletado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Autor não encontrado"),
            @ApiResponse(responseCode = "400", description = "Autor possui livros cadastrados")
    })
    public ResponseEntity<Void> excluirAutor(@PathVariable String id){

        UUID idAutor = UUID.fromString(id);
        Optional<Autor> autorOptional = service.obterAutorPorId(idAutor);
        if (autorOptional.isPresent()) {
            service.excluirAutor(autorOptional.get());
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }


    @PutMapping("{id}")
    @Operation(summary = "Atualizar", description = "Atualiza um autor existente")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Atualizado Com sucesso"),
            @ApiResponse(responseCode = "404", description = "Autor não encontrado"),
            @ApiResponse(responseCode = "409", description = "Autor já Cadastrado"),
            @ApiResponse(responseCode = "422", description = "Erro de Validação")
    })
    public ResponseEntity<Void> atualizar(@PathVariable String id, @RequestBody @Valid AutorDTO dto){

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
