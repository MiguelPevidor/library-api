package io.github.miguelpevidor.libraryapi.exceptions;


import lombok.Getter;

@Getter
public class CampoInvalidoException extends RuntimeException {

    private String campo;

    public CampoInvalidoException(String message, String campo) {
        super(message);
        this.campo = campo;
    }
}
