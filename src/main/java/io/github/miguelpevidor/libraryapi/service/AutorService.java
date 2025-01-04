package io.github.miguelpevidor.libraryapi.service;

import io.github.miguelpevidor.libraryapi.exceptions.OperacaoNaoPermitidaException;
import io.github.miguelpevidor.libraryapi.model.Autor;
import io.github.miguelpevidor.libraryapi.repository.AutorRepository;
import io.github.miguelpevidor.libraryapi.repository.LivroRepository;
import io.github.miguelpevidor.libraryapi.validator.AutorValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AutorService {

    @Autowired
    private AutorRepository repository;
    @Autowired
    private LivroRepository livroRepository;
    @Autowired
    private AutorValidator validator;

    public Autor salvar(Autor autor){
        validator.validar(autor);
        return repository.save(autor);
    }

    public void atualizar(Autor autor) {
        validator.validar(autor);
        repository.save(autor);
    }

    public Optional<Autor> obterAutorPorId(UUID id) {
        return repository.findById(id);
    }

    public void excluirAutor(Autor autor){
        if(possuilivros(autor)){
            throw new OperacaoNaoPermitidaException(
                    "Não é permitido excluir um autor com livros cadastrados");
        }
        repository.delete(autor);
    }

    public List<Autor> pesquisar(String nome, String nacionalidade) {
        if(nome != null && nacionalidade != null){
            return repository.findByNomeAndNacionalidade(nome,nacionalidade);
        }

        if(nome != null){
            return repository.findByNome(nome);
        }

        if(nacionalidade != null) {
            return repository.findByNacionalidade(nacionalidade);
        }

        return repository.findAll();
    }

    // Esse metodo faz a mesma coisa que o metodo pesquisar, só que ao inves do uso de if, e diferentes metodos do
    // repository, utilizamos uma pesquisa no repository somente, mas usando um objeto Example combinado
    // com um ExampleMatcher. Essa abordagem permite criar consultas dinâmicas que ignoram valores nulos
    // e têm configurações avançadas, como case sensitive e correspondência parcial.
    public List<Autor> pesquisarPorExample(String nome, String nacionalidade) {
        Autor autor = new Autor();
        autor.setNome(nome);
        autor.setNacionalidade(nacionalidade);

        // Configura um ExampleMatcher para personalizar o comportamento da consulta:
        ExampleMatcher matcher = ExampleMatcher
                .matching()
                .withIgnorePaths("id","dataCadastro","dataNascimento")
                .withIgnoreNullValues()
                .withIgnoreCase()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);

        Example<Autor> AutorExample = Example.of(autor, matcher);
        return repository.findAll(AutorExample);
    }

    private boolean possuilivros(Autor autor){
        return livroRepository.existsByAutor(autor);
    }

}
