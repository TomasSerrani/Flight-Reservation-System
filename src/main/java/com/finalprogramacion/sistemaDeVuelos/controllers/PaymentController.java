package com.finalprogramacion.sistemaDeVuelos.controllers;

import com.finalprogramacion.sistemaDeVuelos.collectors.EntityAndDTOConverter;
import com.finalprogramacion.sistemaDeVuelos.models.dtos.PaymentDTO;
import com.finalprogramacion.sistemaDeVuelos.models.entities.Payment;
import com.finalprogramacion.sistemaDeVuelos.models.services.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

    @GetMapping("/user-payments")
    public List<PaymentDTO> getUserPayments(@PathVariable Long id) {
        return paymentService.getUserPayments(id).stream()
                .map(EntityAndDTOConverter::toPaymentDTO)
                .collect(Collectors.toList());
    }

    @PostMapping
    public PaymentDTO createPayment(PaymentDTO paymentDTO) {
        Payment savedPayment =dtoToPayment(paymentDTO);
        paymentService.createPayment(savedPayment);
        return paymentDTO;
    }
    @PutMapping("/{id}")
    public PaymentDTO update(@PathVariable Long id, @RequestBody PaymentDTO payment) {
        Payment existingPayment = paymentService.getPaymentById(id);
        if (existingPayment != null) {
            Payment updatedPayment = EntityAndDTOConverter.dtoToPayment(payment);
            updatedPayment.setId(id);
            paymentService.updatePayment(id,updatedPayment);
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

    @GetMapping ("/{number}")
    public PaymentDTO findByPaymentNumber(Long paymentNumber){
        return toPaymentDTO(paymentService.getByPaymentNumber(paymentNumber));
    }
}
