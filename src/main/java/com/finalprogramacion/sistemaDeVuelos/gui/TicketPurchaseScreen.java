package com.finalprogramacion.sistemaDeVuelos.gui;

import javax.swing.*;
import java.awt.*;

public class TicketPurchaseScreen extends JFrame {
    private JLabel flightLabel;
    private JTextField cardNumberField;
    private JTextField cbuField;
    private JButton purchaseButton;
    private JButton backButton;

    public TicketPurchaseScreen() {
        setTitle("Ticket Purchase");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(5, 2));

        flightLabel = new JLabel("Flight Details:");
        add(flightLabel);

        JLabel cardNumberLabel = new JLabel("Card Number:");
        cardNumberField = new JTextField();
        add(cardNumberLabel);
        add(cardNumberField);

        JLabel cbuLabel = new JLabel("CBU:");
        cbuField = new JTextField();
        add(cbuLabel);
        add(cbuField);

        purchaseButton = new JButton("Purchase");
        backButton = new JButton("Back");

        add(purchaseButton);
        add(backButton);

        purchaseButton.addActionListener(e -> {
            // Add ticket purchase logic here
        });

        backButton.addActionListener(e -> {
            // Navigate back to details screen
            new FlightDetailsScreen().setVisible(true);
            dispose();
        });
    }
}
