package io.github.miguelpevidor.libraryapi;

import io.github.miguelpevidor.libraryapi.model.Autor;
import io.github.miguelpevidor.libraryapi.repository.AutorRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDate;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {

		SpringApplication.run(Application.class, args);
	}

}
