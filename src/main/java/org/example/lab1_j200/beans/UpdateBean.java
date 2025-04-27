package org.example.lab1_j200.beans;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import org.example.lab1_j200.repositories.ClientRepository;
import org.example.lab1_j200.repositories.entities.AddressEntity;
import org.example.lab1_j200.repositories.entities.ClientEntity;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Stateless
public class UpdateBean implements UpdateInterface{

    @EJB
    private ClientRepository clientRepository;

    @Override
    public ClientEntity create(String clientName, String type, String[] ip, String[] mac, String[] model, String[] location) {
        if (checkClientName(clientName) && checkType(type) && checkIp(ip) && checkMac(mac) && checkModel(model) && checkLocation(location)){
            ClientEntity clientEntity = new ClientEntity();
            clientEntity.setClientName(clientName);
            clientEntity.setType(type);
            clientEntity.setAdded(Instant.now());
            Set<AddressEntity> addresses = new HashSet<>();
            addresses = setAddress(clientEntity, ip, mac, model, location);
           if (addresses.isEmpty()){
               return null;
           }
            clientEntity.setAddresses(addresses);
            return clientRepository.create(clientEntity);
        }
        return null;
    }

    @Override
    public ClientEntity update(Long clientId, String clientName, String type, String[] addressId, String[] ip, String[] mac, String[] model, String[] location) {
        if (clientRepository.getById(clientId)!=null){
            ClientEntity clientEntity = clientRepository.getById(clientId);
            if (checkClientName(clientName) && !clientEntity.getClientName().equals(clientName)){
                clientEntity.setClientName(clientName);
            }
            if (checkType(type) && !clientEntity.getType().equals(type)){
                clientEntity.setType(type);
            }
            Set<AddressEntity> addresses = new HashSet<>();
            addresses.addAll(clientEntity.getAddresses());
            if (addressId!=null){ //Так как в массиве есть айди, значит происходило редактирование старых адресов
                if (checkAddress(ip, mac, model, location) && checkIp(ip) && checkMac(mac) && checkModel(model) && checkLocation(location)) {
                    for (int i = 0; i < addressId.length; i++) {
                       AddressEntity addressEntity = clientRepository.getByIdAddress(Long.parseLong(addressId[i]));
                        if (!addressEntity.getIpAddress().equals(ip[i])){
                            addressEntity.setIpAddress(ip[i]);
                        }
                        if (!addressEntity.getMacAddress().equals(mac[i])){
                            addressEntity.setMacAddress(mac[i]);
                        }
                        if (!addressEntity.getModel().equals(model[i])){
                            addressEntity.setModel(model[i]);
                        }
                        if (!addressEntity.getAddress().equals(location[i])){
                            addressEntity.setAddress(location[i]);
                        }
                    }
                }
            } else if (checkAddress(ip, mac, model, location) && checkIp(ip) && checkMac(mac) && checkModel(model) && checkLocation(location)){
                addresses.addAll(setAddress(clientEntity, ip, mac, model, location));
                clientEntity.setAddresses(addresses);
            } else return null;
            return clientRepository.update(clientEntity);
        }
        return null;
    }

    @Override
    public void delete(Long addressId) {
        AddressEntity addressEntity =clientRepository.getByIdAddress(addressId);
        ClientEntity clientEntity = addressEntity.getClient();
        if (clientEntity!=null) {
            if (clientEntity.getAddresses().size()==1){
                clientRepository.delete(clientEntity);
            } else {
                clientRepository.deleteAddress(addressEntity);
            }
        }
    }

    private  Set<AddressEntity> setAddress(ClientEntity clientEntity, String[] ip, String[] mac, String[] model, String[] location) {
        Set<AddressEntity> addresses = new HashSet<>();
        if (checkAddress(ip, mac, model, location)) {
            for (int i = 0; i < ip.length; i++) {
                AddressEntity addressEntity = new AddressEntity();
                addressEntity.setIpAddress(ip[i]);
                addressEntity.setMacAddress(mac[i]);
                addressEntity.setModel(model[i]);
                addressEntity.setAddress(location[i]);
                addressEntity.setClient(clientEntity);
                addresses.add(addressEntity);
            }
        }  else return null;
        return addresses;
    }
    private boolean checkClientName(String clientName) {
        String regex = "^[А-Яа-яЁё\\-,. ]+$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(clientName);
        if (clientName == null || clientName.equals("")) {
            return false;
        } else if (clientName.length() >100) {
            return false;
        } else if (!matcher.matches()) {
            return false;
        } else return true;
    }
    private boolean checkIp(String[] ip) {
        String regex = "[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}.[0-9]{1,3}";
        Pattern pattern = Pattern.compile(regex);
        for (String s : ip) {
            Matcher matcher = pattern.matcher(s);
            if (s.trim() == null || s.trim().isEmpty()) {
                return false;
            }
            if (s.length() >25) {
                return false;
            }
            if (!matcher.matches()) {
                return false;
            }
        }
        return true;
    }
    private boolean checkMac(String[] mac) {
        String regex = "([A-F0-9]{2}-){5}[A-F0-9]{2}";
        Pattern pattern = Pattern.compile(regex);
        for (String s : mac) {
            Matcher matcher = pattern.matcher(s);
            if (s.trim() == null || s.trim().isEmpty()) {
                return false;
            }
            if (s.length() > 20) {
                return false;
            }
            if (!matcher.matches()) {
                return false;
            }
        }
        return true;
    }

    private boolean checkModel(String[] model) {
        for (String s : model) {
            if (s.trim() == null || s.trim().isEmpty()) {
                return false;}
            if (s.length() >100) {
                return false;
            }
        }
        return true;
    }
    private boolean checkLocation(String[] location) {
        for (String s : location) {
            if (s.trim() == null || s.trim().isEmpty()) {
                return false;
            }
            if (s.length() >200) {
                return false;
            }
        }
        return true;
    }
    private boolean checkAddress(String[] ip, String[] mac, String[] model, String[] location) {
        return ip.length == mac.length && ip.length == model.length && ip.length == location.length;
    }
    private boolean checkType(String type) {
        if (type == null || type.trim().isEmpty()) {
            return false;
        }
        if (!type.equals("Юридическое лицо") && !type.equals("Физическое лицо")) {
            return false;
        }
        return true;
    }

}
