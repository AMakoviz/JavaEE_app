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
public class UpdateBean implements UpdateInterface {

    @EJB
    private ClientRepository clientRepository;
    @EJB
    private SelectInterface selectBean;

    @Override
    public ClientEntity create(ClientEntity client) {
        if (checkClientName(client.getClientName()) && checkType(client.getType())) {
            client.setAdded(Instant.now());
            Set<AddressEntity> addresses = client.getAddresses();
           for (AddressEntity address : addresses) {
               if (checkIp(address.getIpAddress()) && checkMac(address.getMacAddress()) &&
               checkModel(address.getModel()) && checkLocation(address.getAddress())) {
                   return clientRepository.create(client);
               } else return null;
           }
        }
        return null;
    }

    @Override
    public ClientEntity update(ClientEntity clientFromServlet) {
        System.out.println(clientFromServlet);

        ClientEntity clientFromBD = selectBean.getClientByParam(clientFromServlet.getId());
        if (clientFromBD != null) {
            clientFromServlet.setAdded(clientFromBD.getAdded());
            Set <AddressEntity> addressFromDB = clientFromBD.getAddresses();
            Set <AddressEntity> addressFromServlet = clientFromServlet.getAddresses();
            if (checkClientName(clientFromServlet.getClientName()) && checkType(clientFromServlet.getType())) {
                long idAddress = 0L;

                for (AddressEntity address : addressFromServlet) {
                    System.out.println(address.getIpAddress());
                    if (checkIp(address.getIpAddress()) && checkMac(address.getMacAddress())) {

                        if (address.getId()==null || address.getId().equals("")){
                            idAddress = 0L; //если добавили новый адрес
                        } else idAddress = address.getId();
                    } else return null;
                }
                if (idAddress == 0L) {
                    addressFromServlet.addAll(addressFromDB);
                } else {
                    for (AddressEntity address : addressFromDB) {
                        if (address.getId() != idAddress) {
                            addressFromServlet.add(address);
                        }
                    }
                }

                clientFromServlet.setAddresses(addressFromServlet);
                return clientRepository.update(clientFromServlet);

            } else return null;
        } return null;
    }

    @Override
    public void delete(Long addressId) {
        AddressEntity addressEntity = selectBean.getAddressById(addressId);
        ClientEntity clientEntity = addressEntity.getClient();
        if (clientEntity != null) {
            if (clientEntity.getAddresses().size() == 1) {
                clientRepository.delete(clientEntity);
            } else if (clientEntity.getAddresses().size() > 1) {
                clientRepository.deleteAddress(addressEntity);
            }
        }
    }

//    private Set<AddressEntity> setAddress(ClientEntity clientEntity) {
//        Set<AddressEntity> addressesFromServlet = clientEntity.getAddresses();
//        AddressEntity address = new AddressEntity();
//        for (AddressEntity addressFromServlet : addressesFromServlet) {
//            if (checkIp(addressFromServlet.getIpAddress())){
//                address.setIpAddress(addressFromServlet.getIpAddress());
//            } else return null;
//            if (checkMac(addressFromServlet.getMacAddress())){
//                address.setMacAddress(addressFromServlet.getMacAddress());
//            } else return null;
//            if (checkModel(addressFromServlet.getModel())){
//                address.setModel(addressFromServlet.getModel());
//            } else return null;
//            if (checkLocation(addressFromServlet.getAddress())){
//                address.setAddress(addressFromServlet.getAddress());
//            } else return null;
//        }
//        address.setClient(clientEntity);
//
//
//        return address;
//    }

    private boolean checkClientName(String clientName) {
        String regex = "^[А-Яа-яЁё\\-,. ]+$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(clientName);
        if (clientName == null || clientName.equals("")) {
            return false;
        } else if (clientName.length() > 100) {
            return false;
        } else if (!matcher.matches()) {
            return false;
        } else return true;
    }

    private boolean checkIp(String ip) {
        String regex = "[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}.[0-9]{1,3}";
        Pattern pattern = Pattern.compile(regex);

        Matcher matcher = pattern.matcher(ip);
        if (ip == null || ip.trim().isEmpty()) {
            return false;
        }
        if (ip.length() > 25) {
            return false;
        }
        if (!matcher.matches()) {
            return false;
        }
        return true;
    }

    private boolean checkMac(String mac) {
        String regex = "([A-F0-9]{2}-){5}[A-F0-9]{2}";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(mac);
        if (mac == null || mac.trim().isEmpty()) {
            return false;
        }
        if (mac.length() > 20) {
            return false;
        }
        if (!matcher.matches()) {
            return false;
        }
        return true;
    }

    private boolean checkModel(String model) {
        if (model == null || model.trim().isEmpty()) {
            return false;
        }
        if (model.length() > 100) {
            return false;
        }

        return true;
    }

    private boolean checkLocation(String location) {
        if (location == null || location.trim().isEmpty()) {
            return false;
        }
        if (location.length() > 200) {
            return false;
        }

        return true;
    }

//    private boolean checkAddress(String ip, String mac, String model, String location) {
//        return ip.length == mac.length && ip.length == model.length && ip.length == location.length;
//    }

    private boolean checkType(String type) {
        if (type == null || type.trim().isEmpty()) {
            return false;
        }
        if (!type.equals("Юридическое лицо") && !type.equals("Физическое лицо")) {
            return false;
        }
        return true;
    }

    private void errorMessage(String clientName, String type, String ip, String mac, String model, String location) {
        if (!checkClientName(clientName)) {
            System.out.println("Error at Client name");
        }
        if (!checkType(type)) {
            System.out.println("Error at type");
        }
        if (!checkIp(ip)) {
            System.out.println("Error at ip");
        }
        if (!checkMac(mac)) {
            System.out.println("Error at mac");
        }
        if (!checkModel(model)) {
            System.out.println("Error at model");
        }
        if (!checkLocation(location)) {
            System.out.println("Error at location");
        }
    }

}
