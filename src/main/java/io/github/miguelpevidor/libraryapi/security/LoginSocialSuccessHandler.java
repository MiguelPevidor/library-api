package io.github.miguelpevidor.libraryapi.security;

import io.github.miguelpevidor.libraryapi.model.Usuario;
import io.github.miguelpevidor.libraryapi.service.UsuarioService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Component
public class LoginSocialSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    private static final String SENHA_PADRAO = "321";

    @Autowired
    private UsuarioService usuarioService;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication) throws ServletException, IOException {

        OAuth2AuthenticationToken oAuth2AuthenticationToken = (OAuth2AuthenticationToken) authentication;
        OAuth2User oAuth2User = oAuth2AuthenticationToken.getPrincipal();

        String email = oAuth2User.getAttribute("email");

        Usuario usuario = usuarioService.obterPorEmail(email);

        if (usuario == null){
            usuario = salvarUsuarioNaBase(email);

        }

        authentication = new CustomAuthentication(usuario);
        super.onAuthenticationSuccess(request,response,authentication);
    }

    private Usuario salvarUsuarioNaBase(String email) {
        Usuario usuario;
        usuario = new Usuario();
        usuario.setEmail(email);
        usuario.setLogin(tratarLoginApartirDoEmail(email));
        usuario.setPassword(SENHA_PADRAO);
        usuario.setRoles(List.of("OPERADOR"));

        usuarioService.cadastrar(usuario);
        return usuario;
    }

    private String tratarLoginApartirDoEmail(String email) {
        return email.substring(0, email.indexOf("@"));
    }
}
