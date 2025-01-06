package io.github.miguelpevidor.libraryapi.controller.dto;

import io.github.miguelpevidor.libraryapi.model.GeneroLivro;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import org.hibernate.validator.constraints.ISBN;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record CadastroLivroDTO(
        @ISBN
        @NotBlank(message = "Campo é Obrigatório")
        String isbn,

        @NotBlank(message = "Campo é Obrigatório")
        String titulo,

        @Past(message = "Data de Publicação não pode ser uma data futura")
        @NotNull(message = "Campo é Obrigatório")
        LocalDate dataPublicacao,

        GeneroLivro genero,

        BigDecimal preco,

        @NotNull(message = "Campo é Obrigatório")
        UUID idAutor
) {
}
