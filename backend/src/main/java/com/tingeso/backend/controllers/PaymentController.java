package com.tingeso.backend.controllers;

import com.tingeso.backend.entities.PaymentEntity;
import com.tingeso.backend.services.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.Map;
import java.util.List;

@RestController
@RequestMapping("/api/payment")
@CrossOrigin("*")
public class PaymentController {

    @Autowired
    PaymentService paymentService;

    @GetMapping("/")
    public ResponseEntity<List<PaymentEntity>> getAllPayments() {
        List<PaymentEntity> payments = paymentService.getAllPayments();
        return ResponseEntity.ok(payments);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentEntity> getPaymentById(@PathVariable Long id) {
        PaymentEntity payment = paymentService.getPaymentById(id);
        return ResponseEntity.ok(payment);
    }

    @PostMapping("/")
    public ResponseEntity<PaymentEntity> addPayment(@RequestBody PaymentEntity payment) {
        PaymentEntity addedPayment = paymentService.savePayment(payment);
        return ResponseEntity.ok(addedPayment);
    }

    @PutMapping("/")
    public ResponseEntity<PaymentEntity> updatePayment(@RequestBody PaymentEntity payment) {
        PaymentEntity updatedPayment = paymentService.updatePayment(payment);
        return ResponseEntity.ok(updatedPayment);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<PaymentEntity> deletePayment(@PathVariable Long id) throws Exception {
        var deletedPayment = paymentService.deletePayment(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/create-from-booking/{bookingId}")
    public ResponseEntity<PaymentEntity> createPaymentFromBooking(@PathVariable Long bookingId) {
        PaymentEntity payment = paymentService.createPaymentFromBooking(bookingId);
        return ResponseEntity.ok(payment);
    }

    @GetMapping("/by-booking/{idBooking}")
    public ResponseEntity<PaymentEntity> getPaymentByBookingId(@PathVariable Long idBooking) {
        return paymentService.findPaymentByBookingId(idBooking)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

}
