package io.github.miguelpevidor.libraryapi.validator;

import io.github.miguelpevidor.libraryapi.exceptions.RegistroDuplicadoException;
import io.github.miguelpevidor.libraryapi.model.Autor;
import io.github.miguelpevidor.libraryapi.repository.AutorRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AutorValidator {

    AutorRepository autorRepository;

    public AutorValidator(AutorRepository autorRepository) {
        this.autorRepository = autorRepository;
    }

    // Verifica se existe na base de dados algum autor cadastrado com os campos
    // nome, dataNascimento e nacionalidade igual ao autor passado pelo parametro
    public void validar(Autor autor){
        if(existeAutorCadastrado(autor)){
            throw new RegistroDuplicadoException("Autor já Cadastrado!");
        }
    }

    private boolean existeAutorCadastrado(Autor autor){
        //procura algum registro no banco de dados, com campos iguais do autor
        Optional<Autor> autorEncontrado = autorRepository.findByNomeAndDataNascimentoAndNacionalidade(autor.getNome(), autor.getDataNascimento(), autor.getNacionalidade());

        //Autor não possui id, então é um caso de Cadastro
        if (autor.getId() == null){
            return autorEncontrado.isPresent();
        }

        //Autor possui um id, então é um caso de atualização
        // Verifica se autor encontrado tem o mesmo id do Autor que vai ser atualizado
        // se tiver o mesmo id, eles são os mesmo autor
        return !autor.getId().equals(autorEncontrado.get().getId()) && autorEncontrado.isPresent();
    }
}
