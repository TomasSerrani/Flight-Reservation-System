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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

import static com.finalprogramacion.sistemaDeVuelos.collectors.EntityAndDTOConverter.*;

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
        mainPanel.add(createMainMenuPanel(), "MainMenu"); // Add the new main menu panel
        mainPanel.add(createFlightSearchPanel(), "FlightSearch");
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
                // Go to main menu panel
                cardLayout.show(mainPanel, "MainMenu");
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

    private JPanel createMainMenuPanel() {
        JPanel mainMenuPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel welcomeLabel = new JLabel("Welcome to the Flight Reservation System");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 18));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        mainMenuPanel.add(welcomeLabel, gbc);

        JButton searchFlightsButton = new JButton("Search Flights");
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        mainMenuPanel.add(searchFlightsButton, gbc);

        JButton viewReservationsButton = new JButton("View My Reservations");
        gbc.gridx = 1;
        mainMenuPanel.add(viewReservationsButton, gbc);

        JButton viewPaymentsButton = new JButton("View My Payments");
        gbc.gridx = 0;
        gbc.gridy = 2;
        mainMenuPanel.add(viewPaymentsButton, gbc);

        JButton logoutButton = new JButton("Logout");
        gbc.gridx = 1;
        mainMenuPanel.add(logoutButton, gbc);

        searchFlightsButton.addActionListener(e -> cardLayout.show(mainPanel, "FlightSearch"));
        viewReservationsButton.addActionListener(e -> cardLayout.show(mainPanel, "UserReservations"));
        viewPaymentsButton.addActionListener(e -> cardLayout.show(mainPanel, "UserPayments"));
        logoutButton.addActionListener(e -> {
            this.userEmail = null;
            cardLayout.show(mainPanel, "Login");
        });

        return mainMenuPanel;
    }

    private JButton createBackButton() {
        JButton backButton = new JButton("<- Back");
        backButton.addActionListener(e -> cardLayout.show(mainPanel, "Login"));
        return backButton;
    }

    private JPanel createRegisterPanel() {
        JPanel registerPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Botón de retroceso
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.WEST;
        registerPanel.add(createBackButton(), gbc);

        // Etiqueta y campo de nombre
        gbc.gridy++;
        gbc.gridwidth = 1;
        JLabel nameLabel = new JLabel("Name:");
        registerPanel.add(nameLabel, gbc);

        JTextField nameField = new JTextField(15);
        gbc.gridx = 1;
        registerPanel.add(nameField, gbc);

        // Etiqueta y campo de fecha de nacimiento
        gbc.gridx = 0; // Vuelve a la columna 0
        gbc.gridy++; // Incrementa la fila
        JLabel dateOfBirthLabel = new JLabel("Date of birth: DD/MM/YYYY");
        registerPanel.add(dateOfBirthLabel, gbc);

        JTextField dateOfBirthField = new JTextField(15);
        gbc.gridx = 1;
        registerPanel.add(dateOfBirthField, gbc);

        // Etiqueta y campo de correo electrónico
        gbc.gridx = 0; // Vuelve a la columna 0
        gbc.gridy++; // Incrementa la fila
        JLabel emailLabel = new JLabel("Email:");
        registerPanel.add(emailLabel, gbc);

        JTextField emailField = new JTextField(15);
        gbc.gridx = 1;
        registerPanel.add(emailField, gbc);

        // Etiqueta y campo de contraseña
        gbc.gridx = 0; // Vuelve a la columna 0
        gbc.gridy++; // Incrementa la fila
        JLabel passwordLabel = new JLabel("Password:");
        registerPanel.add(passwordLabel, gbc);

        JPasswordField passwordField = new JPasswordField(15);
        gbc.gridx = 1;
        registerPanel.add(passwordField, gbc);

        // Etiqueta y campo de teléfono
        gbc.gridx = 0; // Vuelve a la columna 0
        gbc.gridy++; // Incrementa la fila
        JLabel phoneLabel = new JLabel("Phone:");
        registerPanel.add(phoneLabel, gbc);

        JTextField phoneField = new JTextField(15);
        gbc.gridx = 1;
        registerPanel.add(phoneField, gbc);

        // Botón de registro
        JButton registerButton = new JButton("Register");
        gbc.gridx = 0; // Vuelve a la columna 0
        gbc.gridy++; // Incrementa la fila
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        registerPanel.add(registerButton, gbc);

        registerButton.addActionListener(e -> {
            // Validación y registro de usuario
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
        JPanel flightSearchPanel = new JPanel(new BorderLayout());
        flightSearchPanel.setBackground(Color.LIGHT_GRAY); // Fondo suave

        // Panel para los campos de búsqueda de vuelos (origen, destino, y botón buscar)
        JPanel searchPanel = new JPanel(new GridLayout(4, 2, 10, 10)); // Espaciado entre componentes

        JLabel originLabel = new JLabel("Origin:");
        originLabel.setFont(new Font("Segoe UI", Font.BOLD, 14)); // Fuente más grande y negrita
        JComboBox<String> originComboBox = new JComboBox<>();

        JLabel destinationLabel = new JLabel("Destination:");
        destinationLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        JComboBox<String> destinationComboBox = new JComboBox<>();

        JButton searchButton = new JButton("Search");
        searchButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        searchButton.setBackground(new Color(70, 130, 180)); // Color de fondo del botón
        searchButton.setForeground(Color.WHITE); // Color de texto del botón
        searchButton.setFocusPainted(false);

        searchPanel.add(originLabel);
        searchPanel.add(originComboBox);
        searchPanel.add(destinationLabel);
        searchPanel.add(destinationComboBox);
        searchPanel.add(new JLabel()); // Empty cell para mantener la estructura
        searchPanel.add(searchButton);

        // Agregar el panel de búsqueda al centro
        flightSearchPanel.add(searchPanel, BorderLayout.NORTH);

        // Lista para mostrar los resultados de la búsqueda de vuelos
        JList<FlightDTO> flightList = new JList<>();
        JScrollPane flightScrollPane = new JScrollPane(flightList);
        flightScrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Margen en el JScrollPane
        flightSearchPanel.add(flightScrollPane, BorderLayout.CENTER);

        // Configurar el botón de búsqueda
        searchButton.setPreferredSize(new Dimension(100, 40));

        // Populate JComboBoxes with airport names
        populateFlightComboBoxes(originComboBox, destinationComboBox);

        searchButton.addActionListener(e -> {
            String origin = (String) originComboBox.getSelectedItem();
            String destination = (String) destinationComboBox.getSelectedItem();

            if (origin != null && destination != null) {
                List<FlightDTO> matchingFlights = flightController.searchFlightsByOriginAndDestination(origin, destination);
                flightList.setListData(matchingFlights.toArray(new FlightDTO[0]));
            } else {
                flightList.setListData(new FlightDTO[0]); // Clear the list
            }
        });

        // Configurar el ListCellRenderer para mostrar detalles adicionales del vuelo
        flightList.setCellRenderer((list, value, index, isSelected, cellHasFocus) -> {
            JPanel panel = new JPanel(new GridLayout(1, 3));
            panel.setBorder(BorderFactory.createLineBorder(Color.GRAY)); // Borde para las filas
            JLabel numLabel = new JLabel("Flight number: " + value.getFlightNum());
            JLabel departureLabel = new JLabel("Departure: " + value.getDepartureDate() + " " + value.getDepartureTime());
            JLabel priceLabel = new JLabel("Price: " + value.getPrice());

            panel.add(numLabel);
            panel.add(departureLabel);
            panel.add(priceLabel);

            if (isSelected) {
                panel.setBackground(list.getSelectionBackground());
                panel.setForeground(list.getSelectionForeground());
            } else {
                panel.setBackground(list.getBackground());
                panel.setForeground(list.getForeground());
            }

            return panel;
        });

        // Panel inferior para botones (Main Menu y Select Flight)
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER)); // Usar FlowLayout para centrar botones

        // Botón "Main Menu"
        JButton mainMenuButton = new JButton("Main Menu");
        mainMenuButton.addActionListener(e -> cardLayout.show(mainPanel, "MainMenu"));
        bottomPanel.add(mainMenuButton);

        // Botón "Seleccionar vuelo"
        JButton selectFlightButton = new JButton("Select Flight");
        selectFlightButton.addActionListener(e -> {
            FlightDTO selectedFlight = flightList.getSelectedValue();
            if (selectedFlight != null) {
                showFlightDetails(selectedFlight);
            }
        });
        bottomPanel.add(selectFlightButton);

        // Añadir el panel inferior al flightSearchPanel
        flightSearchPanel.add(bottomPanel, BorderLayout.SOUTH);

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

    private void showFlightDetails(FlightDTO flightDTO) {
        // Crear un nuevo panel de detalles de vuelo
        JPanel flightDetailsPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Etiquetas para los detalles del vuelo
        JLabel flightNumLabel = new JLabel("Flight Number:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        flightDetailsPanel.add(flightNumLabel, gbc);

        JLabel flightNumValue = new JLabel(flightDTO.getFlightNum());
        gbc.gridx = 1;
        flightDetailsPanel.add(flightNumValue, gbc);

        JLabel originLabel = new JLabel("Origin:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        flightDetailsPanel.add(originLabel, gbc);

        JLabel originValue = new JLabel(flightDTO.getOrigin().getName());
        gbc.gridx = 1;
        flightDetailsPanel.add(originValue, gbc);

        JLabel destinationLabel = new JLabel("Destination:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        flightDetailsPanel.add(destinationLabel, gbc);

        JLabel destinationValue = new JLabel(flightDTO.getDestination().getName());
        gbc.gridx = 1;
        flightDetailsPanel.add(destinationValue, gbc);

        JLabel departureLabel = new JLabel("Departure:");
        gbc.gridx = 0;
        gbc.gridy = 3;
        flightDetailsPanel.add(departureLabel, gbc);

        JLabel departureValue = new JLabel(flightDTO.getDepartureDate().toString() + " " + flightDTO.getDepartureTime().toString());
        gbc.gridx = 1;
        flightDetailsPanel.add(departureValue, gbc);

        JLabel priceLabel = new JLabel("Price:");
        gbc.gridx = 0;
        gbc.gridy = 4;
        flightDetailsPanel.add(priceLabel, gbc);

        JLabel priceValue = new JLabel(String.valueOf(flightDTO.getPrice()));
        gbc.gridx = 1;
        flightDetailsPanel.add(priceValue, gbc);

        // Botón de reserva
        JButton bookButton = new JButton("Book");
        bookButton.putClientProperty("flightDTO", flightDTO);
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        flightDetailsPanel.add(bookButton, gbc);

        // Acción del botón de reserva (similar al código anterior)
        bookButton.addActionListener(e -> {
            FlightDTO selectedFlightDTO = (FlightDTO) bookButton.getClientProperty("flightDTO");
            UserDetails userDetails = userDetailsController.findByEmail(userEmail);
            User currentUser = userController.getCurrentUser(userDetails.getId());
            Reservation reservation = new Reservation();
            reservation.setUser(currentUser);
            reservation.setFlight(dtoToFlight(selectedFlightDTO));

            // Crear un panel para el pago
            JPanel paymentPanel = new JPanel(new GridBagLayout());
            GridBagConstraints paymentGbc = new GridBagConstraints();
            paymentGbc.insets = new Insets(5, 5, 5, 5);
            paymentGbc.fill = GridBagConstraints.HORIZONTAL;

            JLabel paymentMethodLabel = new JLabel("Select Payment Method:");
            paymentGbc.gridx = 0;
            paymentGbc.gridy = 0;
            paymentGbc.gridwidth = 2;
            paymentPanel.add(paymentMethodLabel, paymentGbc);

            String[] paymentMethods = {"Credit Card", "Bank Transfer"};
            JComboBox<String> paymentMethodComboBox = new JComboBox<>(paymentMethods);
            paymentGbc.gridx = 0;
            paymentGbc.gridy = 1;
            paymentGbc.gridwidth = 2;
            paymentPanel.add(paymentMethodComboBox, paymentGbc);

            // Agregar campo para seleccionar la cantidad de pagos
            JLabel numberOfPaymentsLabel = new JLabel("Number of Payments:");
            paymentGbc.gridy = 2;
            paymentGbc.gridx = 0;
            paymentGbc.gridwidth = 1;
            paymentPanel.add(numberOfPaymentsLabel, paymentGbc);

            JSpinner numberOfPaymentsSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 12, 1));
            paymentGbc.gridx = 1;
            paymentPanel.add(numberOfPaymentsSpinner, paymentGbc);

            // Panel para campos de tarjeta de crédito
            JPanel creditCardPanel = new JPanel(new GridBagLayout());
            GridBagConstraints cardGbc = new GridBagConstraints();
            cardGbc.insets = new Insets(5, 5, 5, 5);
            cardGbc.fill = GridBagConstraints.HORIZONTAL;

            JLabel cardNumberLabel = new JLabel("Card Number:");
            cardGbc.gridx = 0;
            cardGbc.gridy = 0;
            creditCardPanel.add(cardNumberLabel, cardGbc);

            JTextField cardNumberField = new JTextField(16);
            cardGbc.gridx = 1;
            creditCardPanel.add(cardNumberField, cardGbc);

            JLabel cardExpiryLabel = new JLabel("Expiry Date:");
            cardGbc.gridx = 0;
            cardGbc.gridy = 1;
            creditCardPanel.add(cardExpiryLabel, cardGbc);

            JTextField cardExpiryField = new JTextField(5);
            cardGbc.gridx = 1;
            creditCardPanel.add(cardExpiryField, cardGbc);

            JLabel cardCVVLabel = new JLabel("CVV:");
            cardGbc.gridx = 0;
            cardGbc.gridy = 2;
            creditCardPanel.add(cardCVVLabel, cardGbc);

            JTextField cardCVVField = new JTextField(3);
            cardGbc.gridx = 1;
            creditCardPanel.add(cardCVVField, cardGbc);

            // Panel para campos de transferencia bancaria
            JPanel bankTransferPanel = new JPanel(new GridBagLayout());
            GridBagConstraints bankGbc = new GridBagConstraints();
            bankGbc.insets = new Insets(5, 5, 5, 5);
            bankGbc.fill = GridBagConstraints.HORIZONTAL;

            JLabel bankAccountLabel = new JLabel("Bank Account Number:");
            bankGbc.gridx = 0;
            bankGbc.gridy = 0;
            bankTransferPanel.add(bankAccountLabel, bankGbc);

            JTextField bankAccountField = new JTextField(20);
            bankGbc.gridx = 1;
            bankTransferPanel.add(bankAccountField, bankGbc);

            JLabel bankNameLabel = new JLabel("Bank Name:");
            bankGbc.gridx = 0;
            bankGbc.gridy = 1;
            bankTransferPanel.add(bankNameLabel, bankGbc);

            JTextField bankNameField = new JTextField(20);
            bankGbc.gridx = 1;
            bankTransferPanel.add(bankNameField, bankGbc);

            // Mostrar paneles de pago basado en selección
            paymentMethodComboBox.addActionListener(event -> {
                String selectedMethod = (String) paymentMethodComboBox.getSelectedItem();
                creditCardPanel.setVisible("Credit Card".equals(selectedMethod));
                bankTransferPanel.setVisible("Bank Transfer".equals(selectedMethod));
            });

            // Agregar los paneles de entrada de pago
            paymentGbc.gridy = 3;
            paymentGbc.gridwidth = 2;
            paymentPanel.add(creditCardPanel, paymentGbc);
            paymentPanel.add(bankTransferPanel, paymentGbc);

            // Agregar botón de envío de pago
            JButton submitPaymentButton = new JButton("Submit Payment");
            paymentGbc.gridy = 4;
            paymentGbc.gridwidth = 2;
            paymentGbc.anchor = GridBagConstraints.CENTER;
            paymentPanel.add(submitPaymentButton, paymentGbc);
            Payment finalPayment= new Payment();

            submitPaymentButton.addActionListener(paymentEvent -> {
                int numberOfPayments = (Integer) numberOfPaymentsSpinner.getValue();
                String selectedMethod = (String) paymentMethodComboBox.getSelectedItem();
                java.sql.Date currentDate= new java.sql.Date(System.currentTimeMillis());
                reservation.setDate(currentDate);
                Long reservationNumber= reservation.generateReservationNumber();
                if(reservationController.findByReservationNumber(reservationNumber) != null){
                    while (reservationController.findByReservationNumber(reservationNumber) != null) {
                        reservationNumber = reservation.generateReservationNumber();
                    }
                }
                reservation.setNumber(reservationNumber);
                if ("Credit Card".equals(selectedMethod)) {
                    String cardNumber = cardNumberField.getText();
                    String expiryDate = cardExpiryField.getText();
                    String cvv = cardCVVField.getText();

                    // Validate that all fields are filled
                    if (cardNumber.isEmpty() || expiryDate.isEmpty() || cvv.isEmpty()) {
                        JOptionPane.showMessageDialog(frame, "All fields are required.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    // Validate card number format (16 digits for Visa/MasterCard)
                    if (!cardNumber.matches("\\d{16}")) {
                        JOptionPane.showMessageDialog(frame, "Invalid card number. Must be 16 digits.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    // Validate card number using Luhn's algorithm
                    if (!isValidCardNumber(cardNumber)) {
                        JOptionPane.showMessageDialog(frame, "Invalid card number. Failed Luhn check.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    // Validate expiry date format (MM/YY)
                    if (!expiryDate.matches("(0[1-9]|1[0-2])/\\d{2}")) {
                        JOptionPane.showMessageDialog(frame, "Invalid expiry date. Must be in MM/YY format.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    // Validate that the expiry date is not in the past
                    if (isExpired(expiryDate)) {
                        JOptionPane.showMessageDialog(frame, "Card expired. Please use a valid card.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    // Validate CVV format (3 digits for Visa/MasterCard)
                    if (!cvv.matches("\\d{3}")) {
                        JOptionPane.showMessageDialog(frame, "Invalid CVV. Must be 3 digits.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    // Update user card details after passing all validations
                    userDetails.updateCardDetails(expiryDate, cardNumber, cvv);
                    userDetailsController.update(userDetails.getId(), toUserDetailsDTO(userDetails));

                    PaymentDTO paymentDTO = new PaymentDTO();
                    Long paymentNumber= paymentDTO.generatePaymentNumber();
                    if(paymentController.findByPaymentNumber(paymentNumber) != null){
                        while (paymentController.findByPaymentNumber(paymentNumber) != null) {
                            paymentNumber = paymentDTO.generatePaymentNumber();
                        }
                    }
                    finalPayment.setNumber(paymentNumber);
                    finalPayment.setUser(currentUser);
                    finalPayment.setType(selectedMethod);
                    finalPayment.setAmountOfPayments(numberOfPayments);

                } else if ("Bank Transfer".equals(selectedMethod)) {
                    String bankAccount = bankAccountField.getText();
                    String bankName = bankNameField.getText();
                    if (bankName.isEmpty() || bankAccount.isEmpty()) {
                        JOptionPane.showMessageDialog(frame, "All fields are required.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    userDetails.updateBankDetails(bankName, bankAccount);
                    userDetailsController.update(userDetails.getId(), toUserDetailsDTO(userDetails));
                    PaymentDTO paymentDTO = new PaymentDTO();
                    Long paymentNumber= paymentDTO.generatePaymentNumber();
                    if(paymentController.findByPaymentNumber(paymentNumber) != null){
                        while (paymentController.findByPaymentNumber(paymentNumber) != null) {
                            paymentNumber = paymentDTO.generatePaymentNumber();
                        }
                    }
                    finalPayment.setNumber(paymentNumber);
                    finalPayment.setUser(currentUser);
                    finalPayment.setType(selectedMethod);
                    finalPayment.setAmountOfPayments(numberOfPayments);

                }

                try {
                    // Step 1: Set reservation state and associate payment
                    reservation.setState("Booked");
                    reservation.setPayment(finalPayment);

                    // Step 2: Create the reservation and find payment
                    reservationController.createReservation(toReservationDTO(reservation));
                    PaymentDTO payment1 = toPaymentDTO(paymentController.findByPaymentNumber(finalPayment.getNumber()));

                    // Step 3: Set the reservation reference in the payment
                    payment1.setReservation(toReservationDTO(reservationController.findByReservationNumber(reservation.getNumber())));

                    // Step 4: Update payment and reservation objects
                    paymentController.update(payment1.getId(), payment1);
                    reservation.setPayment(dtoToPayment(payment1));
                    reservationController.update(reservationController.findByReservationNumber(reservation.getNumber()).getId(), toReservationDTO(reservation));

                    JOptionPane.showMessageDialog(frame, "Flight booked and payment successful");
                    cardLayout.show(mainPanel, "MainMenu");

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame, "Failed to process payment: " + ex.getMessage());
                }
            });

            // Inicialmente mostrar solo el panel de tarjeta de crédito
            creditCardPanel.setVisible(true);
            bankTransferPanel.setVisible(false);

            // Añadir el panel de pago al panel principal
            mainPanel.add(paymentPanel, "Payment");
            cardLayout.show(mainPanel, "Payment");

            // Aquí añadimos el botón de retroceso
            JButton backButton = new JButton("<- Back");
            paymentGbc.gridy = 5;
            paymentGbc.gridwidth = 2;
            paymentGbc.anchor = GridBagConstraints.CENTER;
            paymentPanel.add(backButton, paymentGbc);

            backButton.addActionListener(event -> cardLayout.show(mainPanel, "FlightDetails"));
        });

        JButton backButton = new JButton("<- Back");
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        flightDetailsPanel.add(backButton, gbc);

        backButton.addActionListener(e -> cardLayout.show(mainPanel, "FlightSearch"));

        mainPanel.add(flightDetailsPanel, "FlightDetails");
        cardLayout.show(mainPanel, "FlightDetails");
    }
    private boolean isValidCardNumber(String cardNumber) {
        int sum = 0;
        boolean alternate = false;
        for (int i = cardNumber.length() - 1; i >= 0; i--) {
            int n = Integer.parseInt(cardNumber.substring(i, i + 1));
            if (alternate) {
                n *= 2;
                if (n > 9) {
                    n = (n % 10) + 1;
                }
            }
            sum += n;
            alternate = !alternate;
        }
        return (sum % 10 == 0);
    }

    private boolean isExpired(String expiryDate) {
        String[] parts = expiryDate.split("/");
        int expMonth = Integer.parseInt(parts[0]);
        int expYear = Integer.parseInt("20" + parts[1]);  // Assuming "YY" is in the format of 20YY

        // Get current month and year
        Calendar now = Calendar.getInstance();
        int currentYear = now.get(Calendar.YEAR);
        int currentMonth = now.get(Calendar.MONTH) + 1;  // January is 0, so add 1

        // Check if the card is expired
        if (expYear < currentYear || (expYear == currentYear && expMonth < currentMonth)) {
            return true;
        }
        return false;
    }

    private JPanel createUserReservationsPanel() {
        JPanel userReservationsPanel = new JPanel(new BorderLayout());

        DefaultListModel<ReservationDTO> reservationListModel = new DefaultListModel<>();
        JList<ReservationDTO> reservationList = new JList<>(reservationListModel);

        // Fetch user details and load reservations
        try {
            UserDetails userDetails = userDetailsController.findByEmail(userEmail);
            if (userDetails != null) {
                loadUserReservations(userDetails.getId(), reservationListModel, "Date (Closest)", true);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(frame, "Error loading reservations: " + ex.getMessage());
            ex.printStackTrace();
        }

        // Add sorting options
        JLabel sortByLabel = new JLabel("Sort by:");
        String[] sortingOptions = {"Date (Closest)", "Date (Furthest)", "Passengers (Most)", "Passengers (Least)"};
        JComboBox<String> sortingComboBox = new JComboBox<>(sortingOptions);

        // Add refresh and main menu buttons
        JButton refreshButton = new JButton("Refresh");
        JButton backToMainMenuButton = new JButton("Main Menu");
        backToMainMenuButton.addActionListener(e -> cardLayout.show(mainPanel, "MainMenu"));

        // New buttons for modify and delete
        JButton modifyButton = new JButton("Modify");
        JButton deleteButton = new JButton("Delete");

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.add(sortByLabel);
        buttonPanel.add(sortingComboBox);
        buttonPanel.add(refreshButton);
        buttonPanel.add(modifyButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(backToMainMenuButton);

        // Refresh button action
        refreshButton.addActionListener(e -> {
            try {
                UserDetails userDetails1 = userDetailsController.findByEmail(userEmail);
                if (userDetails1 != null) {
                    // Load reservations with selected sorting option
                    String selectedSortOption = (String) sortingComboBox.getSelectedItem();
                    boolean ascending = !selectedSortOption.equals("Date (Furthest)");  // Ascendente para "Date (Closest)"
                    loadUserReservations(userDetails1.getId(), reservationListModel, selectedSortOption, ascending);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Error loading reservations: " + ex.getMessage());
                ex.printStackTrace();
            }
        });

        // Modify button action
        modifyButton.addActionListener(e -> {
            ReservationDTO selectedReservation = reservationList.getSelectedValue();
            if (selectedReservation != null) {
                modifyReservation(selectedReservation);
            } else {
                JOptionPane.showMessageDialog(frame, "Please select a reservation to modify.");
            }
        });

        // Delete button action
        deleteButton.addActionListener(e -> {
            ReservationDTO selectedReservation = reservationList.getSelectedValue();
            if (selectedReservation != null) {
                deleteReservation(selectedReservation);
                reservationListModel.removeElement(selectedReservation);
            } else {
                JOptionPane.showMessageDialog(frame, "Please select a reservation to delete.");
            }
        });

        // Set custom cell renderer to divide each reservation into its own container
        reservationList.setCellRenderer((list, value, index, isSelected, cellHasFocus) -> {
            JPanel reservationPanel = new JPanel(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(5, 5, 5, 5);  // Padding
            gbc.fill = GridBagConstraints.HORIZONTAL;

            // Set border around each reservation to visually separate them
            reservationPanel.setBorder(BorderFactory.createTitledBorder("Reservation Details"));

            // Display Reservation Number
            gbc.gridx = 0;
            gbc.gridy = 0;
            reservationPanel.add(new JLabel("Reservation Number:"), gbc);

            gbc.gridx = 1;
            reservationPanel.add(new JLabel(String.valueOf(value.getNumber())), gbc);

            // Display Flight Number (if available)
            gbc.gridx = 0;
            gbc.gridy = 1;
            reservationPanel.add(new JLabel("Flight Number:"), gbc);

            if (value.getFlight() != null) {
                gbc.gridx = 1;
                reservationPanel.add(new JLabel(value.getFlight().getFlightNum()), gbc);

                // Display Passenger Count
                gbc.gridx = 0;
                gbc.gridy = 2;
                reservationPanel.add(new JLabel("Passenger Count:"), gbc);

                gbc.gridx = 1;
                reservationPanel.add(new JLabel(String.valueOf(value.getFlight().getPassengerCount())), gbc); // Asegúrate de que FlightDTO tenga este método
            } else {
                gbc.gridx = 1;
                reservationPanel.add(new JLabel("N/A"), gbc);
            }

            // Display Reservation Date
            gbc.gridx = 0;
            gbc.gridy = 3;
            reservationPanel.add(new JLabel("Reservation Date:"), gbc);

            gbc.gridx = 1;
            reservationPanel.add(new JLabel(value.getDate().toString()), gbc);

            // Display Reservation State
            gbc.gridx = 0;
            gbc.gridy = 4;
            reservationPanel.add(new JLabel("Reservation State:"), gbc);

            gbc.gridx = 1;
            reservationPanel.add(new JLabel(value.getState()), gbc);

            // Highlight selected reservation
            if (isSelected) {
                reservationPanel.setBackground(list.getSelectionBackground());
                reservationPanel.setForeground(list.getSelectionForeground());
            } else {
                reservationPanel.setBackground(list.getBackground());
                reservationPanel.setForeground(list.getForeground());
            }

            return reservationPanel;
        });

        userReservationsPanel.add(buttonPanel, BorderLayout.NORTH);
        userReservationsPanel.add(new JScrollPane(reservationList), BorderLayout.CENTER);

        return userReservationsPanel;
    }

    private void modifyReservation(ReservationDTO reservation) {
        JPanel modifyPanel = new JPanel(new GridLayout(0, 2));

        JTextField newDateField = new JTextField(reservation.getDate().toString());
        JTextField newPassengerCountField = new JTextField(String.valueOf(reservation.getFlight().getPassengerCount()));

        modifyPanel.add(new JLabel("New Date:"));
        modifyPanel.add(newDateField);
        modifyPanel.add(new JLabel("New Passenger Count:"));
        modifyPanel.add(newPassengerCountField);

        int result = JOptionPane.showConfirmDialog(frame, modifyPanel, "Modify Reservation", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                Date newDate = new SimpleDateFormat("yyyy-MM-dd").parse(newDateField.getText().trim());
                int newPassengerCount = Integer.parseInt(newPassengerCountField.getText().trim());

                reservationController.updateReservation(reservation.getNumber(), newDate, newPassengerCount); // Cambiado de modify a update
                JOptionPane.showMessageDialog(frame, "Reservation modified successfully.");
            } catch (ParseException e) {
                JOptionPane.showMessageDialog(frame, "Invalid date format. Please use yyyy-MM-dd.");
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(frame, "Invalid number of passengers. Please enter a valid number.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Error modifying reservation: " + ex.getMessage());
            }
        }
    }

    // Método para eliminar reservas
    private void deleteReservation(ReservationDTO reservation) {
        int confirmation = JOptionPane.showConfirmDialog(frame, "Are you sure you want to delete this reservation?", "Delete Confirmation", JOptionPane.YES_NO_OPTION);
        if (confirmation == JOptionPane.YES_OPTION) {
            try {
                reservationController.deleteReservation(reservation.getNumber());
                JOptionPane.showMessageDialog(frame, "Reservation deleted successfully.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Error deleting reservation: " + ex.getMessage());
            }
        }
    }

    private void loadUserReservations(Long userId, DefaultListModel<ReservationDTO> reservationListModel, String sortBy, boolean ascending) {
        SwingWorker<List<ReservationDTO>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<ReservationDTO> doInBackground() throws Exception {
                List<ReservationDTO> reservations = reservationController.getUserReservations(userId);

                // Sort reservations based on the selected criteria
                switch (sortBy) {
                    case "Date (Closest)":
                        reservations.sort(Comparator.comparing(ReservationDTO::getDate));
                        break;
                    case "Date (Furthest)":
                        reservations.sort(Comparator.comparing(ReservationDTO::getDate).reversed());
                        break;
                    case "Passengers (Most)":
                        reservations.sort(Comparator.comparingInt(reservation -> {
                            if (reservation.getFlight() != null) {
                                return reservation.getFlight().getPassengerCount(); // Asegúrate de que FlightDTO tenga este método
                            } else {
                                return 0; // Si no hay vuelo, usar 0
                            }
                        }));
                        break;
                    case "Passengers (Least)":
                        reservations.sort(Comparator.comparingInt(reservation -> {
                            if (reservation.getFlight() != null) {
                                return reservation.getFlight().getPassengerCount(); // Asegúrate de que FlightDTO tenga este método
                            } else {
                                return 0; // Si no hay vuelo, usar 0
                            }
                        }));
                        break;
                    default:
                        break;
                }

                if (!ascending) {
                    Collections.reverse(reservations); // Si no es ascendente, invertir el orden
                }

                return reservations;
            }

            @Override
            protected void done() {
                try {
                    List<ReservationDTO> reservations = get();
                    reservationListModel.clear();
                    for (ReservationDTO reservation : reservations) {
                        reservationListModel.addElement(reservation);
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(frame, "Error loading reservations: " + e.getMessage());
                }
            }
        };
        worker.execute();  // Start the background task
    }

    private JPanel createUserPaymentsPanel() {
        JPanel userPaymentsPanel = new JPanel(new BorderLayout());

        DefaultListModel<PaymentDTO> paymentListModel = new DefaultListModel<>();
        JList<PaymentDTO> paymentList = new JList<>(paymentListModel);

        // Fetch user details and load payments
        try {
            UserDetails userDetails = userDetailsController.findByEmail(userEmail);
            if (userDetails != null) {
                loadUserPayments(userDetails.getId(), paymentListModel);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(frame, "Error loading payments: " + ex.getMessage());
            ex.printStackTrace();
        }

        // Add refresh and main menu buttons
        JButton refreshButton = new JButton("Refresh");
        JButton backToMainMenuButton = new JButton("Main Menu");
        backToMainMenuButton.addActionListener(e -> cardLayout.show(mainPanel, "MainMenu"));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.add(refreshButton);
        buttonPanel.add(backToMainMenuButton);

        refreshButton.addActionListener(e -> {
            try {
                UserDetails userDetails1 = userDetailsController.findByEmail(userEmail);
                if (userDetails1 != null) {
                    loadUserPayments(userDetails1.getId(), paymentListModel);
                } else {
                    JOptionPane.showMessageDialog(frame, "Please log in first");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Error loading payments: " + ex.getMessage());
                ex.printStackTrace();
            }
        });

        // Set custom cell renderer to divide each payment into its own container
        paymentList.setCellRenderer((list, value, index, isSelected, cellHasFocus) -> {
            JPanel paymentPanel = new JPanel(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(5, 5, 5, 5);  // Padding
            gbc.fill = GridBagConstraints.HORIZONTAL;

            // Set border around each payment to visually separate them
            paymentPanel.setBorder(BorderFactory.createTitledBorder("Payment Details"));

            // Display Payment Number
            gbc.gridx = 0;
            gbc.gridy = 0;
            paymentPanel.add(new JLabel("Payment Number:"), gbc);

            gbc.gridx = 1;
            paymentPanel.add(new JLabel(String.valueOf(value.getNumber())), gbc);

            // Display Payment Type
            gbc.gridx = 0;
            gbc.gridy = 1;
            paymentPanel.add(new JLabel("Payment Type:"), gbc);

            gbc.gridx = 1;
            paymentPanel.add(new JLabel(value.getType()), gbc);

            // Display Payment Amount
            gbc.gridx = 0;
            gbc.gridy = 2;
            paymentPanel.add(new JLabel("Amount of Payments:"), gbc);

            gbc.gridx = 1;
            paymentPanel.add(new JLabel(String.valueOf(value.getAmountOfPayments())), gbc);

            // Highlight selected payment
            if (isSelected) {
                paymentPanel.setBackground(list.getSelectionBackground());
                paymentPanel.setForeground(list.getSelectionForeground());
            } else {
                paymentPanel.setBackground(list.getBackground());
                paymentPanel.setForeground(list.getForeground());
            }

            return paymentPanel;
        });

        userPaymentsPanel.add(buttonPanel, BorderLayout.NORTH);
        userPaymentsPanel.add(new JScrollPane(paymentList), BorderLayout.CENTER);

        return userPaymentsPanel;
    }

    private void loadUserPayments(Long userId, DefaultListModel<PaymentDTO> paymentListModel) {
        SwingWorker<List<PaymentDTO>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<PaymentDTO> doInBackground() throws Exception {
                return paymentController.getUserPayments(userId);
            }

            @Override
            protected void done() {
                try {
                    List<PaymentDTO> payments = get();
                    paymentListModel.clear();
                    for (PaymentDTO payment : payments) {
                        paymentListModel.addElement(payment);
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(frame, "Error loading payments: " + e.getMessage());
                }
            }
        };
        worker.execute();  // Start the background task
    }

    public class SeatSelectionPanel extends JPanel {
        private JButton[][] seatButtons;
        private String selectedSeat;
        private FlightDTO flightDTO;

        public SeatSelectionPanel(FlightDTO flightDTO) {
            this.flightDTO = flightDTO;
            initializeUI();
        }

        private void initializeUI() {
            setLayout(new BorderLayout());

            // Crear el panel de asientos
            JPanel seatGridPanel = new JPanel(new GridLayout(6, 6, 10, 10)); // Por ejemplo, 6 filas y 6 columnas
            seatButtons = new JButton[6][6];

            List<String> reservedSeats = flightDTO.getReservedSeats(); // Obtener los asientos reservados

            for (int row = 0; row < 6; row++) {
                for (int col = 0; col < 6; col++) {
                    String seatLabel = (char) ('A' + row) + Integer.toString(col + 1); // Ejemplo: "A1", "B2"
                    seatButtons[row][col] = new JButton(seatLabel);

                    // Comprobar si el asiento está reservado
                    if (reservedSeats.contains(seatLabel)) {
                        seatButtons[row][col].setEnabled(false);
                        seatButtons[row][col].setBackground(Color.RED); // Rojo para asientos ocupados
                    } else {
                        seatButtons[row][col].setBackground(Color.GREEN); // Verde para asientos disponibles
                        seatButtons[row][col].addActionListener(new SeatSelectionListener(seatLabel));
                    }

                    seatGridPanel.add(seatButtons[row][col]);
                }
            }

            // Botón para confirmar selección
            JButton confirmButton = new JButton("Confirmar Selección");
            confirmButton.addActionListener(e -> confirmSeatSelection());

            // Añadir el panel de asientos y el botón de confirmación
            add(seatGridPanel, BorderLayout.CENTER);
            add(confirmButton, BorderLayout.SOUTH);
        }

        private class SeatSelectionListener implements ActionListener {
            private String seatLabel;

            public SeatSelectionListener(String seatLabel) {
                this.seatLabel = seatLabel;
            }

            @Override
            public void actionPerformed(ActionEvent e) {
                selectedSeat = seatLabel; // Guardar el asiento seleccionado
                JOptionPane.showMessageDialog(SeatSelectionPanel.this, "Has seleccionado el asiento: " + selectedSeat);
            }
        }

        private void confirmSeatSelection() {
            if (selectedSeat != null) {
                // Reservar el asiento y proceder al siguiente paso (por ejemplo, proceso de pago)
                flightDTO.reserveSeat(selectedSeat);
                JOptionPane.showMessageDialog(this, "Asiento " + selectedSeat + " reservado. Proceder al pago.");
                // Aquí se puede continuar al panel de pago, según la lógica de tu aplicación
            } else {
                JOptionPane.showMessageDialog(this, "Por favor selecciona un asiento.");
            }
        }
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainApp::new);
    }
}