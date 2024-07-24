package com.finalprogramacion.sistemaDeVuelos.gui;

import com.finalprogramacion.sistemaDeVuelos.AppConfig;
import com.finalprogramacion.sistemaDeVuelos.controllers.*;
import com.finalprogramacion.sistemaDeVuelos.models.entities.*;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@ComponentScan(basePackages = "com.finalprogramacion.sistemaDeVuelos")
public class MainApp {
    private static ApplicationContext context;

    private JFrame frame;
    private JPanel mainPanel;
    private CardLayout cardLayout;

    // Controllers
    private UserController userController;
    private FlightController flightController;
    private ReservationController reservationController;
    private PaymentController paymentController;
    private UserDetailsController userDetailsController;

    private String userEmail;

    public MainApp() {
        // Initialize Spring context
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

        // Initialize controllers from Spring context
        userController = context.getBean(UserController.class);
        flightController = context.getBean(FlightController.class);
        reservationController = context.getBean(ReservationController.class);
        paymentController = context.getBean(PaymentController.class);
        userDetailsController = context.getBean(UserDetailsController.class);

        // Initialize GUI components
        frame = new JFrame("Flight Reservation System");
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Add all panels to card layout
        mainPanel.add(createLoginPanel(), "Login");
        mainPanel.add(createRegisterPanel(), "Register");
        mainPanel.add(createFlightSearchPanel(), "FlightSearch");
        mainPanel.add(createFlightDetailsPanel(), "FlightDetails");
        mainPanel.add(createUserReservationsPanel(), "UserReservations");
        mainPanel.add(createUserPaymentsPanel(), "UserPayments");

        // Setup frame
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.add(mainPanel);
        frame.setVisible(true);

        // Show login panel
        cardLayout.show(mainPanel, "Login");
    }

    private JPanel createLoginPanel() {
        JPanel loginPanel = new JPanel();
        loginPanel.setLayout(new GridLayout(3, 2));

        JLabel emailLabel = new JLabel("Email:");
        JTextField emailField = new JTextField();
        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField passwordField = new JPasswordField();
        JButton loginButton = new JButton("Login");
        JButton registerButton = new JButton("Register");

        loginButton.addActionListener(e -> {
            // Implement login logic
            String email = emailField.getText();
            String password = new String(passwordField.getPassword());
            UserDetails userDetails = userDetailsController.login(email, password);
            if (userDetails != null) {
                // Go to flight search panel
                cardLayout.show(mainPanel, "FlightSearch");
                this.userEmail = userDetails.getEmail();
            } else {
                JOptionPane.showMessageDialog(frame, "Invalid email or password");
            }
        });

        registerButton.addActionListener(e -> cardLayout.show(mainPanel, "Register"));

        loginPanel.add(emailLabel);
        loginPanel.add(emailField);
        loginPanel.add(passwordLabel);
        loginPanel.add(passwordField);
        loginPanel.add(loginButton);
        loginPanel.add(registerButton);

        return loginPanel;
    }

    private JPanel createRegisterPanel() {
        JPanel registerPanel = new JPanel();
        registerPanel.setLayout(new GridLayout(6, 2));

        JLabel nameLabel = new JLabel("Name:");
        JTextField nameField = new JTextField();
        JLabel dateOfBirthLabel = new JLabel("Date of birth: DD/MM/YYYY");
        JTextField dateOfBirthField = new JTextField();
        JLabel emailLabel = new JLabel("Email:");
        JTextField emailField = new JTextField();
        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField passwordField = new JPasswordField();
        JLabel phoneLabel = new JLabel("Phone:");
        JTextField phoneField = new JTextField();
        JButton registerButton = new JButton("Register");

        registerButton.addActionListener(e -> {
            // Implement registration logic
            String name = nameField.getText();
            String email = emailField.getText();
            String password = new String(passwordField.getPassword());
            String phone = phoneField.getText();
            Date dateOfBirth;
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

            try {
                dateOfBirth = dateFormat.parse(dateOfBirthField.getText());
            } catch (ParseException ex) {
                JOptionPane.showMessageDialog(frame, "Invalid date format. Please use DD/MM/YYYY.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            UserDetails createdUserDetails = new UserDetails(email, password, phone);
            User createdUser = new User(name, dateOfBirth, createdUserDetails);

            try {
                userController.createUser(createdUser);
                JOptionPane.showMessageDialog(frame, "Registration successful");
                cardLayout.show(mainPanel, "Login");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Registration failed: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        registerPanel.add(nameLabel);
        registerPanel.add(nameField);
        registerPanel.add(dateOfBirthLabel);
        registerPanel.add(dateOfBirthField);
        registerPanel.add(emailLabel);
        registerPanel.add(emailField);
        registerPanel.add(passwordLabel);
        registerPanel.add(passwordField);
        registerPanel.add(phoneLabel);
        registerPanel.add(phoneField);
        registerPanel.add(new JLabel()); // Empty cell
        registerPanel.add(registerButton);

        return registerPanel;
    }

    private JPanel createFlightSearchPanel() {
        JPanel flightSearchPanel = new JPanel();
        flightSearchPanel.setLayout(new BorderLayout());

        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new GridLayout(3, 2));

        JLabel originLabel = new JLabel("Origin:");
        JTextField originField = new JTextField();
        JLabel destinationLabel = new JLabel("Destination:");
        JTextField destinationField = new JTextField();
        JButton searchButton = new JButton("Search");

        searchPanel.add(originLabel);
        searchPanel.add(originField);
        searchPanel.add(destinationLabel);
        searchPanel.add(destinationField);
        searchPanel.add(new JLabel());
        searchPanel.add(searchButton);

        JList<Flight> flightList = new JList<>();
        flightSearchPanel.add(searchPanel, BorderLayout.NORTH);
        flightSearchPanel.add(new JScrollPane(flightList), BorderLayout.CENTER);

        searchButton.addActionListener(e -> {
            // Implement flight search logic
            String origin = originField.getText();
            String destination = destinationField.getText();
            List<Flight> flightsByOrigins = flightController.searchFlightsByOrigin(origin);
            List<Flight> flightsByDestinies = flightController.searchFlightsByDestination(destination);
            flightList.setListData(flightsByOrigins.toArray(new Flight[0]));
            flightList.setListData(flightsByDestinies.toArray(new Flight[0]));
        });

        flightList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                Flight selectedFlight = flightList.getSelectedValue();
                if (selectedFlight != null) {
                    // Show flight details
                    showFlightDetails(selectedFlight);
                }
            }
        });

        return flightSearchPanel;
    }

    private JPanel createFlightDetailsPanel() {
        JPanel flightDetailsPanel = new JPanel();
        flightDetailsPanel.setLayout(new GridLayout(6, 2));

        JLabel flightNumLabel = new JLabel("Flight Number:");
        JLabel flightNumValue = new JLabel();
        JLabel originLabel = new JLabel("Origin:");
        JLabel originValue = new JLabel();
        JLabel destinationLabel = new JLabel("Destination:");
        JLabel destinationValue = new JLabel();
        JLabel departureLabel = new JLabel("Departure:");
        JLabel departureValue = new JLabel();
        JLabel priceLabel = new JLabel("Price:");
        JLabel priceValue = new JLabel();
        JButton bookButton = new JButton("Book");

        bookButton.addActionListener(e -> {
            // Implement flight booking logic
            Flight flight = (Flight) bookButton.getClientProperty("flight");
            UserDetails userDetails = userController.getCurrentUser(userEmail);
            if (userDetails != null) {
                Reservation reservation = new Reservation();
                reservation.setUser(userDetails.getUser());
                reservation.setFlight(flight);
                reservationController.createReservation(reservation);
                JOptionPane.showMessageDialog(frame, "Flight booked successfully");
            } else {
                JOptionPane.showMessageDialog(frame, "Please log in first");
            }
        });

        flightDetailsPanel.add(flightNumLabel);
        flightDetailsPanel.add(flightNumValue);
        flightDetailsPanel.add(originLabel);
        flightDetailsPanel.add(originValue);
        flightDetailsPanel.add(destinationLabel);
        flightDetailsPanel.add(destinationValue);
        flightDetailsPanel.add(departureLabel);
        flightDetailsPanel.add(departureValue);
        flightDetailsPanel.add(priceLabel);
        flightDetailsPanel.add(priceValue);
        flightDetailsPanel.add(new JLabel());
        flightDetailsPanel.add(bookButton);

        return flightDetailsPanel;
    }

    private JPanel createUserReservationsPanel() {
        JPanel userReservationsPanel = new JPanel();
        userReservationsPanel.setLayout(new BorderLayout());

        JList<Reservation> reservationList = new JList<>();
        JButton refreshButton = new JButton("Refresh");

        refreshButton.addActionListener(e -> {
            // Implement reservation refresh logic
            UserDetails userDetails = userController.getCurrentUser(userEmail);
            if (userDetails != null) {
                List<Reservation> reservations = reservationController.getUserReservations(userDetails.getId());
                reservationList.setListData(reservations.toArray(new Reservation[0]));
            } else {
                JOptionPane.showMessageDialog(frame, "Please log in first");
            }
        });

        userReservationsPanel.add(refreshButton, BorderLayout.NORTH);
        userReservationsPanel.add(new JScrollPane(reservationList), BorderLayout.CENTER);

        return userReservationsPanel;
    }

    private JPanel createUserPaymentsPanel() {
        JPanel userPaymentsPanel = new JPanel();
        userPaymentsPanel.setLayout(new BorderLayout());

        JList<Payment> paymentList = new JList<>();
        JButton refreshButton = new JButton("Refresh");

        refreshButton.addActionListener(e -> {
            // Implement payment refresh logic
            UserDetails userDetails = userController.getCurrentUser(userEmail);
            if (userDetails != null) {
                List<Payment> payments = paymentController.getUserPayments(userDetails.getId());
                paymentList.setListData(payments.toArray(new Payment[0]));
            } else {
                JOptionPane.showMessageDialog(frame, "Please log in first");
            }
        });

        userPaymentsPanel.add(refreshButton, BorderLayout.NORTH);
        userPaymentsPanel.add(new JScrollPane(paymentList), BorderLayout.CENTER);

        return userPaymentsPanel;
    }

    private void showFlightDetails(Flight flight) {
        JLabel flightNumValue = new JLabel(flight.getFlightNum());
        JLabel originValue = new JLabel(flight.getOrigin().getName());
        JLabel destinationValue = new JLabel(flight.getDestination().getName());
        JLabel departureValue = new JLabel(flight.getDepartureDate().toString());
        JLabel priceValue = new JLabel(String.valueOf(flight.getPrice()));
        JButton bookButton = new JButton("Book");

        bookButton.putClientProperty("flight", flight);

        JPanel flightDetailsPanel = createFlightDetailsPanel();
        flightDetailsPanel.add(flightNumValue);
        flightDetailsPanel.add(originValue);
        flightDetailsPanel.add(destinationValue);
        flightDetailsPanel.add(departureValue);
        flightDetailsPanel.add(priceValue);
        flightDetailsPanel.add(bookButton);

        mainPanel.add(flightDetailsPanel, "FlightDetails");
        cardLayout.show(mainPanel, "FlightDetails");
    }

    public static void main(String[] args) {
        context = new AnnotationConfigApplicationContext(AppConfig.class);
        SwingUtilities.invokeLater(MainApp::new);
    }
}