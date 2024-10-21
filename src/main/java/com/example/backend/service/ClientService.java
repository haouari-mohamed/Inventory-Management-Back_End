package com.example.backend.service;

import com.example.backend.model.Client;
import com.example.backend.repository.ClientRepository;
import org.hibernate.query.sql.internal.ParameterRecognizerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientService {
    @Autowired
    private ClientRepository clientRepository;

    public List<Client> getAllClients(){
        return clientRepository.findAll();
    }
    public Client getClientById(Long id){
        return clientRepository.findById(id).orElseThrow();
    }
    public Client createClient(Client client){
        return clientRepository.save(client);
    }
    public Client updateClient(Long id ,Client client){
        Client client1=getClientById(id);
        client1.setNom_client(client.getNom_client());
        client1.setId_client(client.getId_client());
        return clientRepository.save(client1);
    }
    public void deleteClient(Long id){
        clientRepository.deleteById(id);
    }
}
