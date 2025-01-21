package io.github.miguelpevidor.libraryapi.controller;

import io.github.miguelpevidor.libraryapi.controller.dto.ClientDTO;
import io.github.miguelpevidor.libraryapi.controller.mappers.ClientMapper;
import io.github.miguelpevidor.libraryapi.model.Client;
import io.github.miguelpevidor.libraryapi.service.ClientService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("clients")
public class ClientController {

    @Autowired
    private ClientService service;

    @Autowired
    private ClientMapper mapper;

    @PostMapping
    @PreAuthorize("hasRole('GERENTE')")
    @ResponseStatus(HttpStatus.CREATED)
    public void salvar(@RequestBody @Valid ClientDTO dto){
        service.salvar(mapper.toEntity(dto));
    }
}
