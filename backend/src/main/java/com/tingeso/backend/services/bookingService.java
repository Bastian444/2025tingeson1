package com.tingeso.backend.services;

import com.tingeso.backend.entities.bookingEntity;
import com.tingeso.backend.repositories.bookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class bookingService {
    @Autowired
    bookingRepository bookingRepo;

    public List<bookingEntity> getAllBookings() {
        return bookingRepo.findAll();
    }

    public bookingEntity saveBooking(bookingEntity booking) {
        if(booking.getStatus() == null) {
            booking.setStatus("Pendiente");
        }
        return bookingRepo.save(booking);
    }

    public bookingEntity getBookingById(Long id) {
        return bookingRepo.findById(id).orElse(null);
    }

    public List<bookingEntity> getBookingsByRut(String rut) {
        return bookingRepo.findByRutWhoMadeBooking(rut);
    }

    // Nuevo método para buscar por status
    public List<bookingEntity> getBookingsByStatus(String status) {
        return bookingRepo.findByStatus(status);
    }

    public bookingEntity updateBooking(bookingEntity booking) {
        return bookingRepo.save(booking);
    }

    public boolean deleteBooking(Long id) throws Exception {
        try {
            bookingRepo.deleteById(id);
            return true;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }
}