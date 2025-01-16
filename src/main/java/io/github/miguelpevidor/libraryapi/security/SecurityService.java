package io.github.miguelpevidor.libraryapi.security;

import io.github.miguelpevidor.libraryapi.model.Usuario;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SecurityService {

    //vai retornar o Usuario para ser colocado na propriedade "usuario", de livro e autor
    public Usuario obterUsuarioLogado(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication instanceof CustomAuthentication customAuthentication){
            return customAuthentication.getUsuario();
        }

        return null;
    }
}
