package com.finalprogramacion.sistemaDeVuelos.gui;

import javax.swing.*;
import java.awt.*;

public class FlightDetailsScreen extends JFrame {
    private JTextArea detailsArea;
    private JButton backButton;

    public FlightDetailsScreen() {
        setTitle("Flight Details");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        detailsArea = new JTextArea();
        detailsArea.setEditable(false);
        add(new JScrollPane(detailsArea), BorderLayout.CENTER);

        backButton = new JButton("Back");
        add(backButton, BorderLayout.SOUTH);

        backButton.addActionListener(e -> {
            // Navigate back to search screen
            new FlightSearchScreen().setVisible(true);
            dispose();
        });
    }

    public void setFlightDetails(String details) {
        detailsArea.setText(details);
    }
}
