package io.github.miguelpevidor.libraryapi.controller.dto;

import io.github.miguelpevidor.libraryapi.model.Autor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.UUID;

public record AutorDTO(
        UUID id,
        @NotBlank(message = "Campo é Obrigatório")
        @Size(max = 100, message = "tamanho maior que o permitido")
        String nome,

        @NotNull(message = "Campo é Obrigatório")
        @Past(message = "Data de nascimento não pode ser um data futura")
        LocalDate dataNascimento,

        @NotBlank(message = "Campo é Obrigatório")
        @Size(max = 50, message = "tamanho maior que o permitido")
        String nacionalidade
) {

}
