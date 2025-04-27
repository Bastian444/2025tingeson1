package com.tingeso.backend.services;

import com.tingeso.backend.entities.staffEntity;
import com.tingeso.backend.repositories.staffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class staffService {
    @Autowired
    staffRepository staffRepo;

    public List<staffEntity> getAllStaff() {
        return staffRepo.findAll();
    }

    public staffEntity saveStaff(staffEntity staff) {
        return staffRepo.save(staff);
    }

    public staffEntity getStaffById(Long id) {
        return staffRepo.findById(id).orElse(null);
    }

    // Cambiado de getStaffByRut a getStaffByStaffrut
    public staffEntity getStaffByStaffrut(String staffrut){
        return staffRepo.findByStaffrut(staffrut);
    }

    public staffEntity updateStaff(staffEntity staff) {
        return staffRepo.save(staff);
    }

    public boolean deleteStaff(Long id) throws Exception {
        try {
            staffRepo.deleteById(id);
            return true;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }
}