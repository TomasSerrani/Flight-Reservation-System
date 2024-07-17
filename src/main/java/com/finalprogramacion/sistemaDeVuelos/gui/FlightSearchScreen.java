package com.finalprogramacion.sistemaDeVuelos.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FlightSearchScreen extends JFrame {
    private JTextField originField;
    private JTextField destinationField;
    private JTextField dateField;
    private JButton searchButton;

    public FlightSearchScreen() {
        setTitle("Flight Search");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(4, 2));

        JLabel originLabel = new JLabel("Origin:");
        originField = new JTextField();

        JLabel destinationLabel = new JLabel("Destination:");
        destinationField = new JTextField();

        JLabel dateLabel = new JLabel("Date:");
        dateField = new JTextField();

        searchButton = new JButton("Search");

        add(originLabel);
        add(originField);
        add(destinationLabel);
        add(destinationField);
        add(dateLabel);
        add(dateField);
        add(searchButton);

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Add flight search logic here
            }
        });
    }
}
