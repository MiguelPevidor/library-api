package io.github.miguelpevidor.libraryapi.controller.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record UsuarioDTO(
        @NotBlank(message = "Campo obrigatorio")
        String login,
        @Email @NotBlank(message = "Campo obrigatorio")
        String email,
        @NotBlank(message = "Campo Obrigatorio")
        String password,
        List<String> roles){
}
