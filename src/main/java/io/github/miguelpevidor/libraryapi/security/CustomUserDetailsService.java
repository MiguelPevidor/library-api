package io.github.miguelpevidor.libraryapi.security;

import io.github.miguelpevidor.libraryapi.model.Usuario;
import io.github.miguelpevidor.libraryapi.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

// Implementa UserDetailsService para carregar um usuário com base no login,
// usando UsuarioService para buscar no banco; lança erro se o usuário não for encontrado.
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UsuarioService service;

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        Optional<Usuario> usuario = service.obterPorLogin(login);
        if(usuario.isEmpty()){
            throw new UsernameNotFoundException("Usuario não encontrado!");
        }

        Usuario user = usuario.get();
        return User
                .builder()
                .username(user.getLogin())
                .password(user.getPassword())
                .roles(user.getRoles().toArray(new String[user.getRoles().size()]))
                .build();
    }
}
