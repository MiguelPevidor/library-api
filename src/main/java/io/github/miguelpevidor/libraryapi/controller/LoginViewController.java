package io.github.miguelpevidor.libraryapi.controller;

import io.github.miguelpevidor.libraryapi.security.CustomAuthentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class LoginViewController {

    @GetMapping("/login")
    public String paginaLogin(){
        return "login";
    }

    @GetMapping("/")
    @ResponseBody
    public String paginaHome(Authentication authentication){
        if (authentication.getPrincipal() instanceof OAuth2User oAuth2User) {
            // Obtém o nome do usuário a partir dos atributos do OAuth2User
            String nome = oAuth2User.getAttribute("username");
            return "Olá " + (nome != null ? nome : "Usuário");
        }
        return "Olá " + authentication.getName();
    }
}
