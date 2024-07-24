package com.finalprogramacion.sistemaDeVuelos.controllers;

import com.finalprogramacion.sistemaDeVuelos.models.entities.Payment;
import com.finalprogramacion.sistemaDeVuelos.models.services.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @GetMapping
    public List<Payment> findAll() {
        return paymentService.getAllPayments();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Payment> findById(@PathVariable Long id) {
        Payment payment = paymentService.getPaymentById(id);
        if (payment != null) {
            return ResponseEntity.ok(payment);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/user-reservations")
    public List<Payment> getUserPayments(@PathVariable Long id) {
        return paymentService.getUserPayments(id);
    }

    @PostMapping
    public Payment save(@RequestBody Payment payment) {
        return paymentService.createPayment(payment);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        paymentService.deletePayment(id);
        return ResponseEntity.noContent().build();
    }
}
