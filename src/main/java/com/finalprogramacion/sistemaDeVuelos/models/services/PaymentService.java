package com.finalprogramacion.sistemaDeVuelos.models.services;

import com.finalprogramacion.sistemaDeVuelos.models.entities.Payment;
import com.finalprogramacion.sistemaDeVuelos.models.entities.Reservation;
import com.finalprogramacion.sistemaDeVuelos.models.entities.User;
import com.finalprogramacion.sistemaDeVuelos.models.services.repositories.PaymentRepository;
import com.finalprogramacion.sistemaDeVuelos.models.services.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private UserRepository userRepository;

    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    public Payment getPaymentById(Long id) {
        Optional<Payment> payment = paymentRepository.findById(id);
        return payment.orElse(null);
    }

    public Payment createPayment(Payment payment) {
        return paymentRepository.save(payment);
    }

    public Payment updatePayment(Long id, Payment paymentDetails) {
        Optional<Payment> payment = paymentRepository.findById(id);
        if (payment.isPresent()) {
            Payment existingPayment = payment.get();
            existingPayment.setNumber(paymentDetails.getNumber());
            existingPayment.setAmountOfPayments(paymentDetails.getAmountOfPayments());
            existingPayment.setType(existingPayment.getType());

            return paymentRepository.save(existingPayment);
        }
            return null;
    }

    public boolean deletePayment(Long id) {
        if (paymentRepository.existsById(id)) {
            paymentRepository.deleteById(id);
            return true;
        }
            return false;
    }

    public List<Payment> getUserPayments(Long id){
        User user= userRepository.getReferenceById(id);
        return user.getPayments();
    }

    public Payment getByPaymentNumber(Long paymentNumber){
        return paymentRepository.findByNumber(paymentNumber);
    }
}
