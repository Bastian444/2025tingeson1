package com.tingeso.backend.services;

import com.tingeso.backend.entities.kartEntity;
import com.tingeso.backend.repositories.kartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.ArrayList;

@Service
public class kartService {
    @Autowired
    kartRepository kartRepo;

    public ArrayList<kartEntity> getAllKarts() {
        return (ArrayList<kartEntity>) kartRepo.findAll();
    }

    public kartEntity saveKart(kartEntity kart) {
        return kartRepo.save(kart);
    }

    public kartEntity getKartById(Long id) {
        return kartRepo.findById(id).get();
    }

    public kartEntity updateKart(kartEntity kart) {
        return kartRepo.save(kart);
    }

    public boolean deleteKartById(Long id) throws Exception{
        try{
            kartRepo.deleteById(id);
            return true;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

}
