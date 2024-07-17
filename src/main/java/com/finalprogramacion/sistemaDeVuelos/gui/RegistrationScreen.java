package com.finalprogramacion.sistemaDeVuelos.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RegistrationScreen extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JTextField nameField;
    private JTextField phoneField;
    private JTextField cardField;
    private JTextField cbuField;
    private JButton registerButton;
    private JButton backButton;

    public RegistrationScreen() {
        setTitle("Register");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(8, 2));

        JLabel usernameLabel = new JLabel("Username:");
        usernameField = new JTextField();

        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField();

        JLabel nameLabel = new JLabel("Name:");
        nameField = new JTextField();

        JLabel phoneLabel = new JLabel("Phone:");
        phoneField = new JTextField();

        JLabel cardLabel = new JLabel("Card Number:");
        cardField = new JTextField();

        JLabel cbuLabel = new JLabel("CBU:");
        cbuField = new JTextField();

        registerButton = new JButton("Register");
        backButton = new JButton("Back");

        add(usernameLabel);
        add(usernameField);
        add(passwordLabel);
        add(passwordField);
        add(nameLabel);
        add(nameField);
        add(phoneLabel);
        add(phoneField);
        add(cardLabel);
        add(cardField);
        add(cbuLabel);
        add(cbuField);
        add(registerButton);
        add(backButton);

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Add registration logic here
            }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new LoginScreen().setVisible(true);
                dispose();
            }
        });
    }
}
