package org.example.lab1_j200.beans;

import org.example.lab1_j200.repositories.entities.ClientEntity;

public interface UpdateInterface {

    ClientEntity create(String clientName, String type, String[] ip, String[] mac, String[] model, String[] location);
    ClientEntity update(Long clientId, String clientName, String type, String[] addressId, String[] ip, String[] mac, String[] model, String[] location);
    void delete(Long clientId);
}
