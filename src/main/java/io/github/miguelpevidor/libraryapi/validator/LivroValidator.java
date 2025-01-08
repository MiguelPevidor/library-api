package io.github.miguelpevidor.libraryapi.validator;

import io.github.miguelpevidor.libraryapi.exceptions.CampoInvalidoException;
import io.github.miguelpevidor.libraryapi.exceptions.RegistroDuplicadoException;
import io.github.miguelpevidor.libraryapi.model.Livro;
import io.github.miguelpevidor.libraryapi.repository.LivroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class LivroValidator {

    private final static int ANO_EXIGENCIA_PRECO = 2020;
    @Autowired
    private LivroRepository repository;

    public void validar(Livro livro){
        if(existeLivroComISBN(livro)){
            throw new RegistroDuplicadoException("ISBN Duplicado");
        }

        if(validarAnoEPreco(livro)){
            throw new CampoInvalidoException("preco","Livros publicados apartir de 2020 precisam do preco");
        }

    }

    private boolean existeLivroComISBN(Livro livro){
        Optional<Livro> livroEncontrado = repository.findByIsbn(livro.getIsbn());

        //é um caso de cadastro, o livro ainda não foi cadastrado
        if(livro.getId() == null){
            return livroEncontrado.isPresent();
        }

        //caso de atualização, porque o livro passado possui um id
        return !livro.getId().equals(livroEncontrado.get().getId());
    }

    private boolean validarAnoEPreco(Livro livro){
        int ano = livro.getDataPublicacao().getYear();

        return ano >= ANO_EXIGENCIA_PRECO && livro.getPreco() == null;
    }
}
