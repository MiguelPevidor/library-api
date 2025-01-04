package io.github.miguelpevidor.libraryapi.controller.common;

import io.github.miguelpevidor.libraryapi.controller.dto.ErroCampo;
import io.github.miguelpevidor.libraryapi.controller.dto.ErroResposta;
import io.github.miguelpevidor.libraryapi.exceptions.RegistroDuplicadoException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ErroResposta handleMethodArgumentNotValidException (MethodArgumentNotValidException e){
        List<FieldError> fieldErrors = e.getFieldErrors();
        List<ErroCampo> listaErros = fieldErrors.stream()
                .map(fe -> new ErroCampo(fe.getField(),fe.getDefaultMessage())).
                collect(Collectors.toList());

        return new ErroResposta(HttpStatus.UNPROCESSABLE_ENTITY.value(), "erro de validação", listaErros);
    }

    @ExceptionHandler(RegistroDuplicadoException.class)
    public ResponseEntity<ErroResposta> handleRegistroDuplicadoException(RegistroDuplicadoException e){
        ErroResposta erroDto = ErroResposta.conflito(e.getMessage());
        return ResponseEntity.status(erroDto.status()).body(erroDto);
    }
}
