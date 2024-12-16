package io.github.miguelpevidor.libraryapi.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table
@Data
public class Livro {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(length = 20,nullable = false)
    private String isbn;

    @Column(length = 150, nullable = false)
    private String titulo;

    @Column( name = "dat_publicacao", nullable = false)
    private LocalDate dataPublicacao;

    @Column(length = 30, nullable = false)
    private GeneroLivro genero;

    @Column(precision = 18, scale = 2)
    private double preco;

    @ManyToOne
    @JoinColumn(name = "id_autor")
    private Autor autor;
}
