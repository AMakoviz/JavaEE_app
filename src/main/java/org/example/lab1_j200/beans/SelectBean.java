package org.example.lab1_j200.beans;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import org.example.lab1_j200.repositories.ClientRepository;
import org.example.lab1_j200.repositories.entities.ClientEntity;

import java.util.List;

@Stateless
public class SelectBean implements SelectInterface{
    @EJB
    private ClientRepository clientRepository;


    @Override
    public List<ClientEntity> getAllClients() {
       return clientRepository.readAll();
    }

    @Override
    public ClientEntity getClientByParam(Long id) {
        return clientRepository.getById(id);
    }

    @Override
    public List<ClientEntity> getClientsByRegex(String filter, String type) {
        if ((filter==null || filter.isEmpty()) && (type == null || type.isEmpty()) ) {
            return getAllClients();
        }
        return clientRepository.getByFilterClient(filter, type);
    }
}
