package io.github.miguelpevidor.libraryapi.service;


import io.github.miguelpevidor.libraryapi.model.GeneroLivro;
import io.github.miguelpevidor.libraryapi.model.Livro;
import io.github.miguelpevidor.libraryapi.repository.AutorRepository;
import io.github.miguelpevidor.libraryapi.repository.LivroRepository;
import io.github.miguelpevidor.libraryapi.validator.LivroValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

import static io.github.miguelpevidor.libraryapi.repository.specs.LivroSpecs.*;

@Service
public class LivroService {

    @Autowired
    private LivroRepository repository;

    @Autowired
    private AutorRepository autorRepository;

    @Autowired
    private LivroValidator validator;

    public Livro salvar(Livro livro){
        validator.validar(livro);
        return repository.save(livro);
    }

    public Optional<Livro> obterLivroPorId(UUID id) {
        return repository.findById(id);
    }

    public void excluitPorId(UUID id){
        repository.deleteById(id);
    }

    public Page<Livro> pesquisar(String isbn, String titulo, String nomeAutor, GeneroLivro genero, Integer anoPublicacao, int pagina, int tamanhoPagina){

        Specification<Livro> specs = Specification.where((root, query, cb) -> cb.conjunction() );

        if(isbn != null){
            // query = query and isbn = :isbn
            specs = specs.and(isbnEqual(isbn));
        }
        if(titulo != null){
            specs = specs.and(tituloLike(titulo));
        }
        if(genero != null){
            specs = specs.and(generoEqual(genero));
        }
        if(anoPublicacao != null){
            specs = specs.and(anoPublicacaoEqual(anoPublicacao));
        }
        if(nomeAutor != null){
            specs = specs.and(nomeAutorLike(nomeAutor));
        }

        Pageable pageRequest = PageRequest.of(pagina,tamanhoPagina);

        return repository.findAll(specs,pageRequest);
    }

    public void atualizar(Livro livro) {
        if(livro.getId() == null || livro.getAutor() == null){
            throw new IllegalArgumentException("Para atualizar o livro é necessario que ele ja esteja salvo");
        }
        validator.validar(livro);
        repository.save(livro);
    }
}
