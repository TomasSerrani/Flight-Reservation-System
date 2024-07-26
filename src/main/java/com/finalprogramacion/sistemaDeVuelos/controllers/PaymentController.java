package com.finalprogramacion.sistemaDeVuelos.controllers;

import com.finalprogramacion.sistemaDeVuelos.collectors.EntityAndDTOConverter;
import com.finalprogramacion.sistemaDeVuelos.models.dtos.PaymentDTO;
import com.finalprogramacion.sistemaDeVuelos.models.entities.Payment;
import com.finalprogramacion.sistemaDeVuelos.models.services.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static com.finalprogramacion.sistemaDeVuelos.collectors.EntityAndDTOConverter.*;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @GetMapping
    public List<PaymentDTO> findAll() {
        return paymentService.getAllPayments().stream()
                .map(EntityAndDTOConverter::toPaymentDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentDTO> findById(@PathVariable Long id) {
        PaymentDTO payment = toPaymentDTO(paymentService.getPaymentById(id));
        if (id != null) {
            return ResponseEntity.ok(payment);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/user-payments")
    public List<PaymentDTO> getUserPayments(@PathVariable Long id) {
        return paymentService.getUserPayments(id).stream()
                .map(EntityAndDTOConverter::toPaymentDTO)
                .collect(Collectors.toList());
    }

    @PostMapping
    public ResponseEntity<PaymentDTO> save(@RequestBody PaymentDTO paymentDTO) {
        Payment savedPayment =dtoToPayment(paymentDTO);
        paymentService.createPayment(savedPayment);
        return ResponseEntity.status(HttpStatus.CREATED).body(paymentDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        if (paymentService.getPaymentById(id) != null) {
            paymentService.deletePayment(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.noContent().build();
    }
}
