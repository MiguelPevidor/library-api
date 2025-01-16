package io.github.miguelpevidor.libraryapi.security;

import io.github.miguelpevidor.libraryapi.model.Usuario;
import io.github.miguelpevidor.libraryapi.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {
    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private PasswordEncoder encoder;


    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String login = authentication.getName();
        String senhaDigitada = authentication.getCredentials().toString();

        Optional<Usuario> usuarioEncontrado = usuarioService.obterPorLogin(login);
        if (usuarioEncontrado.isEmpty()){
            getErroUsuarioNaoEncontrado();
        }

        String senhaCriptografada = usuarioEncontrado.get().getPassword();

        boolean senhasBatem = encoder.matches(senhaDigitada,senhaCriptografada);

        if(senhasBatem){
            return new CustomAuthentication(usuarioEncontrado.get());
        }

        throw  getErroUsuarioNaoEncontrado();
    }

    private UsernameNotFoundException getErroUsuarioNaoEncontrado() {
        throw new UsernameNotFoundException("Usuario e/ou senha incorretos!");
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.isAssignableFrom(UsernamePasswordAuthenticationToken.class);
    }
}
