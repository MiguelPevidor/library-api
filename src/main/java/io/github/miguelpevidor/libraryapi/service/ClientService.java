package io.github.miguelpevidor.libraryapi.service;

import io.github.miguelpevidor.libraryapi.model.Client;
import io.github.miguelpevidor.libraryapi.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class ClientService {
    @Autowired
    private ClientRepository repository;

    @Autowired
    private PasswordEncoder encoder;

    public void salvar(Client client){
        String senhaCriptografada = encoder.encode(client.getClientSecret());
        client.setClientSecret(senhaCriptografada);
        repository.save(client);
    }

    public Client obterporClientId(String clientId){
        return repository.findByClientId(clientId);
    }
}
