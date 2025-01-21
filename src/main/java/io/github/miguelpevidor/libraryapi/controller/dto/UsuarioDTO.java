package io.github.miguelpevidor.libraryapi.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

@Schema(name = "Usuario")
public record UsuarioDTO(
        @NotBlank(message = "Campo obrigatorio")
        String login,
        @Email @NotBlank(message = "Campo obrigatorio")
        String email,
        @NotBlank(message = "Campo Obrigatorio")
        String password,
        List<String> roles){
}
