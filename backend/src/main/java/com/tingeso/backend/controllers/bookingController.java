package com.tingeso.backend.controllers;

import com.tingeso.backend.entities.bookingEntity;
import com.tingeso.backend.services.bookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
@CrossOrigin("*")
public class bookingController {
    @Autowired
    bookingService BookingService;

    @GetMapping("/")
    public ResponseEntity<List<bookingEntity>> getAllBookings(){
        List<bookingEntity> bookings = BookingService.getAllBookings();
        return ResponseEntity.ok(bookings);
    }

    @GetMapping("/{id}")
    public ResponseEntity<bookingEntity> getBookingById(@PathVariable Long id){
        bookingEntity Booking = BookingService.getBookingById(id);
        return ResponseEntity.ok(Booking);
    }

    @PostMapping("/")
    public ResponseEntity<bookingEntity> addBooking(@RequestBody bookingEntity booking){
        // Establecer status por defecto si es null
        if(booking.getStatus() == null) {
            booking.setStatus("Pendiente"); // O tu valor por defecto
        }
        bookingEntity savedBooking = BookingService.saveBooking(booking);
        return ResponseEntity.ok(savedBooking);
    }

    @PutMapping("/")
    public ResponseEntity<bookingEntity> updateBooking(@RequestBody bookingEntity booking){
        bookingEntity updatedBooking = BookingService.updateBooking(booking);
        return ResponseEntity.ok(updatedBooking);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteBooking(@PathVariable Long id) throws Exception {
        var deletedBooking = BookingService.deleteBooking(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/rut/{rut}")
    public ResponseEntity<List<bookingEntity>> getBookingsByRut(@PathVariable String rut) {
        List<bookingEntity> bookings = BookingService.getBookingsByRut(rut);
        return ResponseEntity.ok(bookings);
    }

    // Opcional: Endpoint para filtrar por status
    @GetMapping("/status/{status}")
    public ResponseEntity<List<bookingEntity>> getBookingsByStatus(@PathVariable String status) {
        List<bookingEntity> bookings = BookingService.getBookingsByStatus(status);
        return ResponseEntity.ok(bookings);
    }
}