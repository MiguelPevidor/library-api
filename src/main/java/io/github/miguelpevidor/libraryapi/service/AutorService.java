package io.github.miguelpevidor.libraryapi.service;

import io.github.miguelpevidor.libraryapi.model.Autor;
import io.github.miguelpevidor.libraryapi.repository.AutorRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AutorService {

    private AutorRepository repository;

    public AutorService(AutorRepository repository){
        this.repository = repository;
    }

    public Autor salvar(Autor autor){
        if(autor.getId() == null) {
            throw new IllegalArgumentException("Id do autor ausente! Para Atualizar um autor, ele dever existir na base.");
        }
        return repository.save(autor);
    }

    public void atualizar(Autor autor) {
        repository.save(autor);
    }

    public Optional<Autor> obterAutorPorId(UUID id) {
        return repository.findById(id);
    }

    public void excluirAutor(UUID idAutor){
        repository.deleteById(idAutor);
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

}
