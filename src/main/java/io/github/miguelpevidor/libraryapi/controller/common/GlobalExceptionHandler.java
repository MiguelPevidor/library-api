package io.github.miguelpevidor.libraryapi.controller.common;

import io.github.miguelpevidor.libraryapi.controller.dto.ErroCampo;
import io.github.miguelpevidor.libraryapi.controller.dto.ErroResposta;
import io.github.miguelpevidor.libraryapi.exceptions.CampoInvalidoException;
import io.github.miguelpevidor.libraryapi.exceptions.OperacaoNaoPermitidaException;
import io.github.miguelpevidor.libraryapi.exceptions.RegistroDuplicadoException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    //Tratamento de erro para quando os erros  de validações que
    //foram lançadas, Exemplo: Titulo vazio, ou muito longo
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ErroResposta handleMethodArgumentNotValidException (MethodArgumentNotValidException e){
        List<FieldError> fieldErrors = e.getFieldErrors();
        List<ErroCampo> listaErros = fieldErrors.stream()
                .map(fe -> new ErroCampo(fe.getField(),fe.getDefaultMessage())).
                collect(Collectors.toList());

        return new ErroResposta(HttpStatus.UNPROCESSABLE_ENTITY.value(), "erro de validação", listaErros);
    }

    //Trata a exception lançada quando um livro tem a data de publicacao maior ou igual
    //a 2020, e não tem preco
    @ExceptionHandler(CampoInvalidoException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ErroResposta handleCampoInvalidoException (CampoInvalidoException e){
        return new ErroResposta(HttpStatus.UNPROCESSABLE_ENTITY.value(),"erro de validação", List.of(new ErroCampo(e.getCampo(),e.getMessage())));
    }

    @ExceptionHandler(RegistroDuplicadoException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErroResposta handleRegistroDuplicadoException(RegistroDuplicadoException e){
        return ErroResposta.conflito(e.getMessage());
    }


    @ExceptionHandler(OperacaoNaoPermitidaException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErroResposta handleOperacaoNaoPermitida(OperacaoNaoPermitidaException e){
        return ErroResposta.respostaPadrao(e.getMessage());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ErroResposta handleAccessDeniedException(AccessDeniedException e){
        return new ErroResposta(HttpStatus.FORBIDDEN.value(), "Acesso não permitido",List.of());
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErroResposta handleErrosNaoTratados(RuntimeException e){
        return new ErroResposta(HttpStatus.INTERNAL_SERVER_ERROR.value()
                ,"Ocorreu um erro inesperado, entre em contato com a administração!"
                ,List.of());
    }


}
