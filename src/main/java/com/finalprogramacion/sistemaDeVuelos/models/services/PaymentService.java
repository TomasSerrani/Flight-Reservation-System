package com.finalprogramacion.sistemaDeVuelos.models.services;

import com.finalprogramacion.sistemaDeVuelos.models.entities.Payment;
import com.finalprogramacion.sistemaDeVuelos.models.entities.User;
import com.finalprogramacion.sistemaDeVuelos.models.services.repositories.PaymentRepository;
import com.finalprogramacion.sistemaDeVuelos.models.services.repositories.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private UserRepository userRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public Payment processPayment(Payment payment){
        if(payment.getId() == null){
          return createNewPayment(payment);
        }
        return updateExistingPayment(payment);
    }

    @Transactional
    public Payment createNewPayment (Payment payment){
        return paymentRepository.save(payment);
    }

    @Transactional
    public Payment updateExistingPayment (Payment payment){
        return paymentRepository.saveAndFlush(payment);
    }

    @Transactional(readOnly= true)
    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    @Transactional(readOnly= true)
    public Payment getPaymentById(Long id) {
        Optional<Payment> payment = paymentRepository.findById(id);
        return payment.orElse(null);
    }

    @Transactional
    public Payment updatePayment(Long id, Payment paymentDetails) {
        Optional<Payment> payment = paymentRepository.findById(id);
        if (payment.isPresent()) {
            Payment existingPayment = payment.get();
            existingPayment.setNumber(paymentDetails.getNumber());
            existingPayment.setAmountOfPayments(paymentDetails.getAmountOfPayments());
            existingPayment.setType(paymentDetails.getType());
            existingPayment.setReservation(paymentDetails.getReservation());

            return paymentRepository.save(existingPayment);
        }
            return null;
    }

    @Transactional
    public boolean deletePayment(Long id) {
        if (paymentRepository.existsById(id)) {
            paymentRepository.deleteById(id);
            return true;
        }
            return false;
    }

    @Transactional(readOnly= true)
    public List<Payment> getUserPayments(Long id){
        Optional<User> user= userRepository.findById(id);
        return user.get().getPayments();
    }

    @Transactional(readOnly= true)
    public Payment getByPaymentNumber(Long paymentNumber){
        return paymentRepository.findByNumber(paymentNumber);
    }

    public List<Payment> getPaymentsForReservation(Long reservationId) {
        List<Payment> payments = paymentRepository.findByReservationId(reservationId);
        return new ArrayList<>(payments);
    }
}
