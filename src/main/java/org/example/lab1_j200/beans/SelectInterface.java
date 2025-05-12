package org.example.lab1_j200.beans;

import org.example.lab1_j200.repositories.entities.AddressEntity;
import org.example.lab1_j200.repositories.entities.ClientEntity;

import java.util.List;

public interface SelectInterface {

    List<ClientEntity> getAllClients();
    ClientEntity getClientByParam(Long id);
    List<ClientEntity> getClientsByRegex(String filter, String type);
    AddressEntity getAddressById(Long id);


}
