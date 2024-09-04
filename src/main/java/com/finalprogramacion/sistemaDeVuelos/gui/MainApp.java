package com.finalprogramacion.sistemaDeVuelos.gui;

import com.finalprogramacion.sistemaDeVuelos.AppConfig;
import com.finalprogramacion.sistemaDeVuelos.controllers.*;
import com.finalprogramacion.sistemaDeVuelos.models.dtos.*;
import com.finalprogramacion.sistemaDeVuelos.models.entities.*;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import javax.swing.*;
import java.awt.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.finalprogramacion.sistemaDeVuelos.collectors.EntityAndDTOConverter.toFlightDTO;
import static com.finalprogramacion.sistemaDeVuelos.collectors.EntityAndDTOConverter.toUserDTO;

@ComponentScan(basePackages = "com.finalprogramacion.sistemaDeVuelos")
public class MainApp {
    private ApplicationContext context;

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
        initializeContext();
        initializeControllers();
        setupUI();
    }
    private void initializeContext() {
        context = new AnnotationConfigApplicationContext(AppConfig.class);
    }

    private void initializeControllers() {
        userController = context.getBean(UserController.class);
        flightController = context.getBean(FlightController.class);
        reservationController = context.getBean(ReservationController.class);
        paymentController = context.getBean(PaymentController.class);
        userDetailsController = context.getBean(UserDetailsController.class);
    }
    private void setupUI() {
        frame = new JFrame("Flight Reservation System");
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        mainPanel.add(createLoginPanel(), "Login");
        mainPanel.add(createRegisterPanel(), "Register");
        mainPanel.add(createFlightSearchPanel(), "FlightSearch");
        mainPanel.add(createFlightDetailsPanel(), "FlightDetails");
        mainPanel.add(createUserReservationsPanel(), "UserReservations");
        mainPanel.add(createUserPaymentsPanel(), "UserPayments");

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.add(mainPanel);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

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
        JPanel registerPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Etiqueta y campo de nombre
        JLabel nameLabel = new JLabel("Name:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.2;
        registerPanel.add(nameLabel, gbc);

        JTextField nameField = new JTextField(15);
        gbc.gridx = 1;
        gbc.weightx = 0.8;
        registerPanel.add(nameField, gbc);

        // Etiqueta y campo de fecha de nacimiento
        JLabel dateOfBirthLabel = new JLabel("Date of birth: DD/MM/YYYY");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.2;
        registerPanel.add(dateOfBirthLabel, gbc);

        JTextField dateOfBirthField = new JTextField(15);
        gbc.gridx = 1;
        gbc.weightx = 0.8;
        registerPanel.add(dateOfBirthField, gbc);

        // Etiqueta y campo de correo electrónico
        JLabel emailLabel = new JLabel("Email:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.2;
        registerPanel.add(emailLabel, gbc);

        JTextField emailField = new JTextField(15);
        gbc.gridx = 1;
        gbc.weightx = 0.8;
        registerPanel.add(emailField, gbc);

        // Etiqueta y campo de contraseña
        JLabel passwordLabel = new JLabel("Password:");
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0.2;
        registerPanel.add(passwordLabel, gbc);

        JPasswordField passwordField = new JPasswordField(15);
        gbc.gridx = 1;
        gbc.weightx = 0.8;
        registerPanel.add(passwordField, gbc);

        // Etiqueta y campo de teléfono
        JLabel phoneLabel = new JLabel("Phone:");
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.weightx = 0.2;
        registerPanel.add(phoneLabel, gbc);

        JTextField phoneField = new JTextField(15);
        gbc.gridx = 1;
        gbc.weightx = 0.8;
        registerPanel.add(phoneField, gbc);

        // Botón de registro
        JButton registerButton = new JButton("Register");
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        registerPanel.add(registerButton, gbc);

        registerButton.addActionListener(e -> {
            String name = nameField.getText().trim();
            String email = emailField.getText().trim();
            String password = new String(passwordField.getPassword()).trim();
            String phone = phoneField.getText().trim();
            String dateOfBirthText = dateOfBirthField.getText().trim();

            if (name.isEmpty() || email.isEmpty() || password.isEmpty() || phone.isEmpty() || dateOfBirthText.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "All fields are required.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Date dateOfBirth;
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            try {
                dateOfBirth = dateFormat.parse(dateOfBirthText);
            } catch (ParseException ex) {
                JOptionPane.showMessageDialog(frame, "Invalid date format. Please use DD/MM/YYYY.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            UserDetailsDTO createdUserDetails = new UserDetailsDTO(email, password, phone);
            UserDTO createdUser = new UserDTO(name, dateOfBirth, createdUserDetails);

            try {
                if (userDetailsController.findByEmail(createdUserDetails.getEmail()) == null){
                    userController.createUser(createdUser);
                    JOptionPane.showMessageDialog(frame, "Registration successful");
                }
                else {
                    JOptionPane.showMessageDialog(frame, "Registration failed: Email already used", "Error", JOptionPane.ERROR_MESSAGE);
                }
                cardLayout.show(mainPanel, "Login");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Registration failed: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        return registerPanel;
    }

    private JPanel createFlightSearchPanel() {
        JPanel flightSearchPanel = new JPanel();
        flightSearchPanel.setLayout(new BorderLayout());

        // Panel for search fields
        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new GridLayout(3, 2));

        JLabel originLabel = new JLabel("Origin:");
        JComboBox<String> originComboBox = new JComboBox<>();

        JLabel destinationLabel = new JLabel("Destination:");
        JComboBox<String> destinationComboBox = new JComboBox<>();
        JButton searchButton = new JButton("Search");

        searchPanel.add(originLabel);
        searchPanel.add(originComboBox);
        searchPanel.add(destinationLabel);
        searchPanel.add(destinationComboBox);
        searchPanel.add(new JLabel()); // Empty cell
        searchPanel.add(searchButton);

        JList<FlightDTO> flightList = new JList<>();
        flightSearchPanel.add(searchPanel, BorderLayout.NORTH);
        flightSearchPanel.add(new JScrollPane(flightList), BorderLayout.CENTER);

        // Set preferred size for button
        searchButton.setPreferredSize(new Dimension(100, 30));

        // Populate JComboBoxes with airport names
        populateFlightComboBoxes(originComboBox, destinationComboBox);

        searchButton.addActionListener(e -> {
            // Get selected origin and destination
            String origin = (String) originComboBox.getSelectedItem();
            String destination = (String) destinationComboBox.getSelectedItem();

            if (origin != null && destination != null) {
                // Find flights that match both origin and destination
                List<FlightDTO> matchingFlights = flightController.searchFlightsByOriginAndDestination(origin, destination);

                // Display the flights in the JList
                flightList.setListData(matchingFlights.toArray(new FlightDTO[0]));
            } else {
                // Handle case where either origin or destination is not selected
                flightList.setListData(new FlightDTO[0]); // Clear the list
            }
        });

        return flightSearchPanel;
    }

    private void populateFlightComboBoxes(JComboBox<String> originComboBox, JComboBox<String> destinationComboBox) {
        // Obtener la lista de vuelos desde el servicio
        List<FlightDTO> flights = flightController.getAllFlights();

        for (FlightDTO flight : flights) {
            String originFlightInfo = flight.getOrigin().getCity();
            originComboBox.addItem(originFlightInfo);

            String destinationFlightInfo = flight.getDestination().getCity();
            destinationComboBox.addItem(destinationFlightInfo);
        }
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
        JButton bookButton = getBookButton();

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

        flightDetailsPanel.putClientProperty("flightNumValue", flightNumValue);
        flightDetailsPanel.putClientProperty("originValue", originValue);
        flightDetailsPanel.putClientProperty("destinationValue", destinationValue);
        flightDetailsPanel.putClientProperty("departureValue", departureValue);
        flightDetailsPanel.putClientProperty("priceValue", priceValue);
        flightDetailsPanel.putClientProperty("bookButton", bookButton);

        return flightDetailsPanel;
    }

    private JButton getBookButton() {
        JButton bookButton = new JButton("Book");

        bookButton.addActionListener(e -> {
            // Implement flight booking logic
            Flight flight = (Flight) bookButton.getClientProperty("flight");
            UserDetails userDetails = userController.getCurrentUser(userEmail);
            if (userDetails != null) {
                ReservationDTO reservation = new ReservationDTO();
                reservation.setUser(toUserDTO(userDetails.getUser()));
                reservation.setFlight(toFlightDTO(flight));
                reservationController.createReservation(reservation);
                JOptionPane.showMessageDialog(frame, "Flight booked successfully");
            } else {
                JOptionPane.showMessageDialog(frame, "Please log in first");
            }
        });
        return bookButton;
    }

    private void showFlightDetails(FlightDTO flightDTO) {
        // Create or retrieve the panel
        JPanel flightDetailsPanel = createFlightDetailsPanel();

        // Get references to the labels and button
        JLabel flightNumValue = (JLabel) flightDetailsPanel.getClientProperty("flightNumValue");
        JLabel originValue = (JLabel) flightDetailsPanel.getClientProperty("originValue");
        JLabel destinationValue = (JLabel) flightDetailsPanel.getClientProperty("destinationValue");
        JLabel departureValue = (JLabel) flightDetailsPanel.getClientProperty("departureValue");
        JLabel priceValue = (JLabel) flightDetailsPanel.getClientProperty("priceValue");
        JButton bookButton = (JButton) flightDetailsPanel.getClientProperty("bookButton");

        // Update the panel with flight details
        flightNumValue.setText(flightDTO.getFlightNum());
        originValue.setText(flightDTO.getOrigin().getName());
        destinationValue.setText(flightDTO.getDestination().getName());
        departureValue.setText(flightDTO.getDepartureDate().toString());
        priceValue.setText(String.valueOf(flightDTO.getPrice()));

        // Set the flightDTO on the book button
        bookButton.putClientProperty("flightDTO", flightDTO);

        // Add the updated panel to the mainPanel and show it
        mainPanel.add(flightDetailsPanel, "FlightDetails");
        cardLayout.show(mainPanel, "FlightDetails");
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
                List<ReservationDTO> reservations = reservationController.getUserReservations(userDetails.getId());
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
                List<PaymentDTO> payments = paymentController.getUserPayments(userDetails.getId());
                paymentList.setListData(payments.toArray(new Payment[0]));
            } else {
                JOptionPane.showMessageDialog(frame, "Please log in first");
            }
        });

        userPaymentsPanel.add(refreshButton, BorderLayout.NORTH);
        userPaymentsPanel.add(new JScrollPane(paymentList), BorderLayout.CENTER);

        return userPaymentsPanel;
    }





    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainApp::new);
    }
}