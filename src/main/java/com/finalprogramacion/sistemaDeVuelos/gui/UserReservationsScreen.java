package com.finalprogramacion.sistemaDeVuelos.gui;

import javax.swing.*;
import java.awt.*;

public class UserReservationsScreen extends JFrame {
    private JTextArea reservationsArea;
    private JButton backButton;

    public UserReservationsScreen() {
        setTitle("User Reservations");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        reservationsArea = new JTextArea();
        reservationsArea.setEditable(false);
        add(new JScrollPane(reservationsArea), BorderLayout.CENTER);

        backButton = new JButton("Back");
        add(backButton, BorderLayout.SOUTH);

        backButton.addActionListener(e -> {
            // Navigate back to main screen
            new FlightSearchScreen().setVisible(true);
            dispose();
        });
    }

    public void setReservationsDetails(String details) {
        reservationsArea.setText(details);
    }
}
