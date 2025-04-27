package com.tingeso.backend.controllers;

import com.tingeso.backend.entities.clientEntity;
import com.tingeso.backend.services.clientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clients")
@CrossOrigin("*")
public class clientController {
    @Autowired
    clientService service;

    @GetMapping("/")
    public ResponseEntity<List<clientEntity>> findAll() {
        List<clientEntity> clients = service.getAllClients();
        return ResponseEntity.ok(clients);
    }

    @GetMapping("/{id}")
    public ResponseEntity<clientEntity> findById(@PathVariable Long id) {
        clientEntity client = service.getClientById(id);
        return ResponseEntity.ok(client);
    }

    @PostMapping("/")
    public ResponseEntity<clientEntity> saveClient(@RequestBody clientEntity client) {
        clientEntity savedClient = service.saveClient(client);
        return ResponseEntity.ok(savedClient);
    }

    @PutMapping("/")
    public ResponseEntity<clientEntity> updateClient(@RequestBody clientEntity client) {
        clientEntity clientUpdated = service.updateClient(client);
        return ResponseEntity.ok(clientUpdated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<clientEntity> deleteClient(@PathVariable Long id) throws Exception {
        var isDeleted = service.deleteClientById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody clientEntity client) {
        clientEntity foundClient = service.getClientByClientrut(client.getClientrut());

        if (foundClient == null || !foundClient.getClientpassword().equals(client.getClientpassword())) {
            return ResponseEntity.status(401).body("Credenciales incorrectas");
        }

        return ResponseEntity.ok(foundClient);
    }

    // Endpoint adicional para buscar por RUT (puede ser útil)
    @GetMapping("/rut/{rut}")
    public ResponseEntity<clientEntity> findByRut(@PathVariable String rut) {
        clientEntity client = service.getClientByClientrut(rut);
        return ResponseEntity.ok(client);
    }
}