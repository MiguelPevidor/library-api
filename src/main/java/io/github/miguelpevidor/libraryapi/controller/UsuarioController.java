package io.github.miguelpevidor.libraryapi.controller;

import io.github.miguelpevidor.libraryapi.controller.dto.UsuarioDTO;
import io.github.miguelpevidor.libraryapi.controller.mappers.UsuarioMapper;
import io.github.miguelpevidor.libraryapi.model.Usuario;
import io.github.miguelpevidor.libraryapi.repository.UsuarioRepository;
import io.github.miguelpevidor.libraryapi.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Optional;
import java.util.UUID;

@RestController
@PreAuthorize("hasRole('GERENTE')")
@RequestMapping("usuarios")
public class UsuarioController implements GenericController{

    @Autowired
    private UsuarioService service;

    @Autowired
    private UsuarioMapper mapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void cadastrar(@RequestBody @Valid UsuarioDTO dto){
        Usuario usuario = service.cadastrar(mapper.toEntity(dto));
    }

    @GetMapping
    public ResponseEntity<Object> obterPorLogin(@RequestParam String login){
        Optional<Usuario> usuario = service.obterPorLogin(login);
        if(usuario.isPresent()){
            return ResponseEntity.ok(usuario.get());
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping()
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
    public ResponseEntity<Void> excluir(@PathVariable UUID id){
        Optional<Usuario> usuario = service.obterPorId(id);

        if(usuario.isPresent()){
            service.deletar(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
