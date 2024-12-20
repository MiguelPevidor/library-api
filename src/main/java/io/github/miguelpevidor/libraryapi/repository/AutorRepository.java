package io.github.miguelpevidor.libraryapi.repository;

import io.github.miguelpevidor.libraryapi.model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AutorRepository extends JpaRepository<Autor, UUID> {
}
