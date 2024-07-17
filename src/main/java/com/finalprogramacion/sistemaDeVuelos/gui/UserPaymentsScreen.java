package com.finalprogramacion.sistemaDeVuelos.gui;

import javax.swing.*;
import java.awt.*;

public class UserPaymentsScreen extends JFrame {
    private JTextArea paymentsArea;
    private JButton backButton;

    public UserPaymentsScreen() {
        setTitle("User Payments");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        paymentsArea = new JTextArea();
        paymentsArea.setEditable(false);
        add(new JScrollPane(paymentsArea), BorderLayout.CENTER);

        backButton = new JButton("Back");
        add(backButton, BorderLayout.SOUTH);

        backButton.addActionListener(e -> {
            // Navigate back to main screen
            new FlightSearchScreen().setVisible(true);
            dispose();
        });
    }

    public void setPaymentsDetails(String details) {
        paymentsArea.setText(details);
    }
}
