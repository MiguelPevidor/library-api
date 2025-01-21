package io.github.miguelpevidor.libraryapi.controller;

import io.github.miguelpevidor.libraryapi.controller.dto.UsuarioDTO;
import io.github.miguelpevidor.libraryapi.controller.mappers.UsuarioMapper;
import io.github.miguelpevidor.libraryapi.model.Usuario;
import io.github.miguelpevidor.libraryapi.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("usuarios")
@Tag(name = "Usuarios")
public class UsuarioController implements GenericController{

    @Autowired
    private UsuarioService service;

    @Autowired
    private UsuarioMapper mapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Cadastrar", description = "Cadastrar novo Usuario")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Cadastrado com sucesso"),
            @ApiResponse(responseCode = "422", description = "Erro de Validação"),
    })
    public void cadastrar(@RequestBody @Valid UsuarioDTO dto){
        Usuario usuario = mapper.toEntity(dto);
        service.cadastrar(usuario);
    }

    @GetMapping
    @PreAuthorize("hasRole('GERENTE')")
    @Operation(summary = "Obter Usuario", description = "Retorna um Usuario existente apartir do Login")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuario encontrado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Usuario não encontrado")
    })
    public ResponseEntity<Object> obterPorLogin(@RequestParam String login){
        Optional<Usuario> usuario = service.obterPorLogin(login);
        if(usuario.isPresent()){
            return ResponseEntity.ok(usuario.get());
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping()
    @PreAuthorize("hasRole('GERENTE')")
    @Operation(summary = "Atualizar", description = "Atualiza um Usuario existente")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Atualizado Com sucesso"),
            @ApiResponse(responseCode = "404", description = "Usuario não encontrado"),
            @ApiResponse(responseCode = "422", description = "Erro de Validação")
    })
    public ResponseEntity<Void> atualizar(@RequestBody @Valid UsuarioDTO dto){
        Usuario usuario = mapper.toEntity(dto);
        Optional<Usuario> usuarioEncontrado = service.obterPorLogin(usuario.getLogin());

        if(usuarioEncontrado.isPresent()){
            service.atualizar(usuarioEncontrado.get());
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();

    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasRole('GERENTE')")
    @Operation(summary = "Deletar", description = "Deleta um Usuario existente")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Deletado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Usuario não encontrado"),
    })
    public ResponseEntity<Void> excluir(@PathVariable UUID id){
        Optional<Usuario> usuario = service.obterPorId(id);

        if(usuario.isPresent()){
            service.deletar(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
