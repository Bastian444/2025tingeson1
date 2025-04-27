package com.tingeso.backend.repositories;

import com.tingeso.backend.entities.staffEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface staffRepository extends JpaRepository<staffEntity, Long> {
    // Cambiado de findByRut a findByStaffrut
    staffEntity findByStaffrut(String staffrut);

    // Eliminado findAll() porque ya lo provee JpaRepository
}