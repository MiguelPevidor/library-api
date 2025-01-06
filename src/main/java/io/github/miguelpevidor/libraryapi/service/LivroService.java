package io.github.miguelpevidor.libraryapi.service;

import io.github.miguelpevidor.libraryapi.controller.dto.CadastroLivroDTO;
import io.github.miguelpevidor.libraryapi.model.Autor;
import io.github.miguelpevidor.libraryapi.model.Livro;
import io.github.miguelpevidor.libraryapi.repository.AutorRepository;
import io.github.miguelpevidor.libraryapi.repository.LivroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class LivroService {

    @Autowired
    private LivroRepository repository;

    @Autowired
    private AutorRepository autorRepository;

    public Livro salvar(Livro livro){
        return repository.save(livro);
    }

}
