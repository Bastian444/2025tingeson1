package com.tingeso.backend.repositories;

import com.tingeso.backend.entities.bookingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface bookingRepository extends JpaRepository<bookingEntity, Long> {
    List<bookingEntity> findByDate(String date);
    List<bookingEntity> findByRutWhoMadeBooking(String rutWhoMadeBooking);

    // Nuevo método para buscar por status
    List<bookingEntity> findByStatus(String status);

    // Método para buscar por rut y status
    List<bookingEntity> findByRutWhoMadeBookingAndStatus(String rutWhoMadeBooking, String status);

    // Método para buscar por fecha y status
    List<bookingEntity> findByDateAndStatus(String date, String status);
}