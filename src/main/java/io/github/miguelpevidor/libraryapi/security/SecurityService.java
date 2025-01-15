package io.github.miguelpevidor.libraryapi.security;

import io.github.miguelpevidor.libraryapi.model.Usuario;
import io.github.miguelpevidor.libraryapi.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.neo4j.Neo4jProperties;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class SecurityService {
    @Autowired
    private UsuarioService usuarioService;

    public Usuario obterUsuarioLogado(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String login = userDetails.getUsername();
        return usuarioService.obterPorLogin(login).orElse(null);
    }
}
