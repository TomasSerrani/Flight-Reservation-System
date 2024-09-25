package com.finalprogramacion.sistemaDeVuelos.controllers;

import com.finalprogramacion.sistemaDeVuelos.collectors.EntityAndDTOConverter;
import com.finalprogramacion.sistemaDeVuelos.models.dtos.PaymentDTO;
import com.finalprogramacion.sistemaDeVuelos.models.entities.Payment;
import com.finalprogramacion.sistemaDeVuelos.models.services.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static com.finalprogramacion.sistemaDeVuelos.collectors.EntityAndDTOConverter.*;

@RestController
@RequestMapping("/payments")
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
    public PaymentDTO findById(@PathVariable Long id) {
        return toPaymentDTO(paymentService.getPaymentById(id));
    }

    @GetMapping("/user-payments/{id}")
    public List<PaymentDTO> getUserPayments(@PathVariable Long id) {
        return paymentService.getUserPayments(id).stream()
                .map(EntityAndDTOConverter::toPaymentDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    @PutMapping("/{id}")
    public PaymentDTO update(@PathVariable Long id, @RequestBody PaymentDTO paymentDTO) {
        Payment existingPayment = paymentService.getPaymentById(id);
        if (existingPayment != null) {
            Payment updatedPayment = dtoToPayment(paymentDTO);
            updatedPayment.setId(id);
            paymentService.updatePayment(id, updatedPayment);
            return toPaymentDTO(updatedPayment);
        }
        return null;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        if (paymentService.getPaymentById(id) != null) {
            paymentService.deletePayment(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/payment-number/{paymentNumber}")
    public Payment findByPaymentNumber(@PathVariable Long paymentNumber) {
        return paymentService.getByPaymentNumber(paymentNumber);
    }

    public List<PaymentDTO> getPaymentsForReservation(Long reservationId) {
        List<Payment> payments = paymentService.getPaymentsForReservation(reservationId);
        return payments.stream()
                .map(EntityAndDTOConverter::toPaymentDTO) // Convert to PaymentDTO
                .collect(Collectors.toList());
    }
}
