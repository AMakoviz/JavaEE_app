package org.example.lab1_j200.beans;

import org.example.lab1_j200.repositories.entities.ClientEntity;

public interface UpdateInterface {

    ClientEntity create(ClientEntity client);
    ClientEntity update(ClientEntity client);
    void delete(Long clientId);
}
