package com.tingeso.backend.services;

import com.tingeso.backend.entities.clientEntity;
import com.tingeso.backend.repositories.clientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class clientService {
    @Autowired
    clientRepository clientRepo;

    public List<clientEntity> getAllClients(){
        return clientRepo.findAll();
    }

    public clientEntity saveClient(clientEntity client){
        return clientRepo.save(client);
    }

    public clientEntity getClientById(Long id){
        return clientRepo.findById(id).orElse(null);
    }

    // Cambiado de getClientByRut a getClientByClientrut
    public clientEntity getClientByClientrut(String clientrut){
        return clientRepo.findByClientrut(clientrut);
    }

    public clientEntity updateClient(clientEntity client){
        return clientRepo.save(client);
    }

    public boolean deleteClientById(Long id) throws Exception {
        try{
            clientRepo.deleteById(id);
            return true;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    public List<clientEntity> getClientsWithMoreThanXVisits(int visits) {
        return clientRepo.findByVisitsGreaterThan(visits);
    }

    public boolean isBirthday(String rut, String bookingTimestamp) {
        // Extrae YYYY-MM-DD del timestamp (ej: "2025-04-30T16:30:00" → "2025-04-30")
        String datePart = bookingTimestamp.substring(0, 10);
        return clientRepo.isClientBirthday(rut, datePart);
    }

}