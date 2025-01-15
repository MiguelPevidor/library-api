package io.github.miguelpevidor.libraryapi.service;

import io.github.miguelpevidor.libraryapi.model.Usuario;
import io.github.miguelpevidor.libraryapi.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository repository;

    @Autowired
    private PasswordEncoder encoder;

    public Usuario cadastrar(Usuario usuario){
        String senha = usuario.getPassword();
        usuario.setPassword(encoder.encode(senha));
        return repository.save(usuario);
    }

    public Optional<Usuario> obterPorLogin(String login){
        return repository.findByLogin(login);
    }

    public Optional<Usuario> obterPorId(UUID id){
        return repository.findById(id);
    }

    public void deletar(UUID id){
        repository.deleteById(id);
    }

    public void atualizar(Usuario usuario){
        repository.save(usuario);
    }


}
