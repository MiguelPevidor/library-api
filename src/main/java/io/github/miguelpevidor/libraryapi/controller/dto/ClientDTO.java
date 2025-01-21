package io.github.miguelpevidor.libraryapi.controller.dto;

import jakarta.validation.constraints.NotBlank;

public record ClientDTO (
        @NotBlank(message = "Campo Obrigatorio")
        String clientId,
        @NotBlank(message = "Campo Obrigatorio")
        String clientSecret,
        @NotBlank(message = "Campo Obrigatorio")
        String redirectedURI,
        String scope
){

}
