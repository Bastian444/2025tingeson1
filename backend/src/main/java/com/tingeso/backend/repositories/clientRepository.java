package com.tingeso.backend.repositories;

import com.tingeso.backend.entities.clientEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.List;

@Repository
public interface clientRepository extends JpaRepository<clientEntity, Long> {
    // Cambiado de findByRut a findByClientrut
    clientEntity findByClientrut(String clientrut);

    // Eliminado findAll() porque ya lo provee JpaRepository
    List<clientEntity> findByVisitsGreaterThan(int visits);

    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END " +
            "FROM clientEntity c " +
            "WHERE c.clientrut = :rut " +
            "AND SUBSTRING(c.birthdate, 6, 5) = SUBSTRING(:date, 6, 5)")
    boolean isClientBirthday(
            @Param("rut") String rut,
            @Param("date") String date);






}