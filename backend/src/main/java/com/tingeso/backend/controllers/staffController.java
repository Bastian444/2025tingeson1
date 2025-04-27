package com.tingeso.backend.controllers;

import com.tingeso.backend.entities.staffEntity;
import com.tingeso.backend.services.staffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/staff")
@CrossOrigin("*")
public class staffController {
    @Autowired
    private staffService staffService;

    @GetMapping("/")
    public ResponseEntity<List<staffEntity>> getAllStaff() {
        List<staffEntity> staffs = staffService.getAllStaff();
        return ResponseEntity.ok(staffs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<staffEntity> getStaffById(@PathVariable Long id) {
        staffEntity staff = staffService.getStaffById(id);
        return ResponseEntity.ok(staff);
    }

    @PostMapping("/")
    public ResponseEntity<staffEntity> saveStaff(@RequestBody staffEntity staff) {
        staffEntity savedStaff = staffService.saveStaff(staff);
        return ResponseEntity.ok(savedStaff);
    }

    @PutMapping("/")
    public ResponseEntity<staffEntity> updateStaff(@RequestBody staffEntity staff) {
        staffEntity updatedStaff = staffService.updateStaff(staff);
        return ResponseEntity.ok(updatedStaff);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteStaff(@PathVariable Long id) throws Exception {
        staffService.deleteStaff(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody staffEntity staff) {
        // Cambiado de getStaffByRut a getStaffByStaffrut
        staffEntity foundStaff = staffService.getStaffByStaffrut(staff.getStaffrut());

        if (foundStaff == null || !foundStaff.getStaffpassword().equals(staff.getStaffpassword())) {
            return ResponseEntity.status(401).body("Credenciales incorrectas");
        }

        return ResponseEntity.ok(foundStaff);
    }
}