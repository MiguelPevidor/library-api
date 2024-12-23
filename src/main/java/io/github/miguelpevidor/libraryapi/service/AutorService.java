package io.github.miguelpevidor.libraryapi.service;

import io.github.miguelpevidor.libraryapi.exceptions.OperacaoNaoPermitidaException;
import io.github.miguelpevidor.libraryapi.model.Autor;
import io.github.miguelpevidor.libraryapi.repository.AutorRepository;
import io.github.miguelpevidor.libraryapi.repository.LivroRepository;
import io.github.miguelpevidor.libraryapi.validator.AutorValidator;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AutorService {

    private AutorRepository repository;
    private LivroRepository livroRepository;
    private AutorValidator validator;



    public AutorService(AutorRepository repository,AutorValidator validator,LivroRepository livroRepository){
        this.repository = repository;
        this.livroRepository = livroRepository;
        this.validator = validator;
    }

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

    private boolean possuilivros(Autor autor){
        return livroRepository.existsByAutor(autor);
    }

}
