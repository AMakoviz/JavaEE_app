package org.example.lab1_j200.repositories;

import jakarta.ejb.Singleton;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.example.lab1_j200.repositories.entities.AddressEntity;
import org.example.lab1_j200.repositories.entities.ClientEntity;

import java.util.List;

@Singleton
public class ClientRepository {

    @PersistenceContext
    private EntityManager em;

    //@Transactional
    public List<ClientEntity> readAll() {
        return em.createQuery("SELECT distinct client FROM ClientEntity client left join fetch client.addressEntities " +
                        "order by client.id asc ",
                ClientEntity.class).getResultList();
    }

    //@Transactional
    public ClientEntity getById(Long id) {
        return (ClientEntity) em.createQuery("select distinct client from ClientEntity client left join fetch " +
                        "client.addressEntities where client.id = :id order by client.id asc ", ClientEntity.class)
                .setParameter("id", id)
                .getSingleResult();
    }
    public AddressEntity getByIdAddress(Long id) {
        return (AddressEntity) em.createQuery("select distinct ad from AddressEntity ad where ad.id = :id order by ad.id asc")
                .setParameter("id", id)
                .getSingleResult();
    }




    //@Transactional


    public List<ClientEntity> getByFilterClient(String filter, String type) {
        if (filter == null) filter = "";
        String pattern = "%" + filter.toUpperCase() + "%";
        String query;
        if (type.equals("") || type.isEmpty()){
            query = "SELECT DISTINCT client FROM ClientEntity client LEFT JOIN FETCH client.addressEntities address "+
               "where (UPPER(client.clientName) LIKE :pattern OR UPPER(address.address) LIKE :pattern) " +
            "order by client.id asc";
            return em.createQuery(query, ClientEntity.class)
                    .setParameter("pattern",pattern)
                    .getResultList();
        }

        query = "SELECT DISTINCT client FROM ClientEntity client LEFT JOIN FETCH client.addressEntities address WHERE "+
                "client.type = :type " + "AND (UPPER(client.clientName) LIKE :pattern OR UPPER(address.address) LIKE :pattern) " +
                "order by client.id asc";

        return em.createQuery(query, ClientEntity.class)
                .setParameter("pattern",pattern)
                .setParameter("type", type)
                .getResultList();
    }

    //@Transactional
    public ClientEntity create(ClientEntity client) {
        em.persist(client);
        em.flush();
        return client;
    }

    //@Transactional
    public ClientEntity update(ClientEntity client) {
        ClientEntity clientEntity = em.merge(client);
        em.flush();
        return clientEntity;
    }

    // @Transactional
    public void delete(ClientEntity client) {
        em.remove(client);
    }
    public void deleteAddress(AddressEntity addressEntity) {
        em.remove(addressEntity);
    }



}
