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
import java.util.Date;
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

    private JButton createBackButton() {
        JButton backButton = new JButton("<-");
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
        registerPanel.add(createBackButton(), gbc); // Añadir el botón "<-"

        // Etiqueta y campo de nombre
        gbc.gridy++;
        gbc.gridwidth = 1;
        JLabel nameLabel = new JLabel("Name:");
        registerPanel.add(nameLabel, gbc);

        JTextField nameField = new JTextField(15);
        gbc.gridx = 1;
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

        // Añadir el botón "<-" en la parte superior
        flightSearchPanel.add(createBackButton(), BorderLayout.NORTH);

        // Panel for search fields
        JPanel searchPanel = new JPanel(new GridLayout(3, 2));

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
        // Agregar el botón "Seleccionar vuelo"
        JButton selectFlightButton = new JButton("Select Flight");
        flightSearchPanel.add(selectFlightButton, BorderLayout.SOUTH);

        // Configurar el ListCellRenderer para mostrar datos adicionales
        flightList.setCellRenderer((list, value, index, isSelected, cellHasFocus) -> {
            JPanel panel = new JPanel(new GridLayout(1, 3));
            JLabel numLabel = new JLabel("Fligh number: " + value.getFlightNum());
            JLabel departureLabel = new JLabel("Departure: " + value.getDepartureDate() + value.getDepartureTime());
            JLabel priceLabel = new JLabel("Price: " + value.getPrice());

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

        selectFlightButton.addActionListener(e -> {
            FlightDTO selectedFlight = flightList.getSelectedValue();
            if (selectedFlight != null) {
                showFlightDetails(selectedFlight);
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

        JButton bookButton = new JButton("Book");
        bookButton.putClientProperty("flightDTO", flightDTO);
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        flightDetailsPanel.add(bookButton, gbc);

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

            submitPaymentButton.addActionListener(paymentEvent -> {
                int numberOfPayments = (Integer) numberOfPaymentsSpinner.getValue();
                String selectedMethod = (String) paymentMethodComboBox.getSelectedItem();

                if ("Credit Card".equals(selectedMethod)) {
                    String cardNumber = cardNumberField.getText();
                    String expiryDate = cardExpiryField.getText();
                    String cvv = cardCVVField.getText();
                    if (cardNumber.isEmpty() || expiryDate.isEmpty() || cvv.isEmpty()) {
                        JOptionPane.showMessageDialog(frame, "All fields are required.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    userDetails.updateCardDetails(expiryDate, cardNumber, cvv);
                    userDetailsController.update(userDetails.getId(), toUserDetailsDTO(userDetails));
                    PaymentDTO paymentDTO = new PaymentDTO();
                    Long paymentNumber;
                    do {
                        paymentNumber = paymentDTO.generatePaymentNumber();
                    } while (paymentController.findByPaymentNumber(paymentNumber).getNumber() != null);
                    PaymentDTO finalPayment= new PaymentDTO(paymentNumber,selectedMethod,numberOfPayments,toUserDTO(currentUser));
                    paymentController.createPayment(finalPayment);
                    reservation.setPayment(dtoToPayment(paymentController.findByPaymentNumber(paymentNumber)));
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
                    Long paymentNumber;
                    do {
                        paymentNumber = paymentDTO.generatePaymentNumber();
                    } while (paymentController.findByPaymentNumber(paymentNumber) != null);
                    PaymentDTO finalPayment= new PaymentDTO(paymentNumber,selectedMethod,numberOfPayments,toUserDTO(currentUser));
                    paymentController.createPayment(finalPayment);
                    reservation.setPayment(dtoToPayment(paymentController.findByPaymentNumber(paymentNumber)));
                }

                try {
                    reservationController.createReservation(toReservationDTO(reservation));
                    Payment payment= dtoToPayment(paymentController.findById(reservation.getPayment().getId()));
                    payment.setReservation(reservation);
                    paymentController.update(payment.getId(), toPaymentDTO(payment));
                    JOptionPane.showMessageDialog(frame, "Flight booked and payment successful");
                    cardLayout.show(mainPanel, "FlightSearch");
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


    private JPanel createUserReservationsPanel() {
        JPanel userReservationsPanel = new JPanel();
        userReservationsPanel.setLayout(new BorderLayout());

        JList<Reservation> reservationList = new JList<>();
        JButton refreshButton = new JButton("Refresh");

        refreshButton.addActionListener(e -> {
            // Implement reservation refresh logic
            UserDetails userDetails = userDetailsController.findByEmail(userEmail);
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
            UserDetails userDetails = userDetailsController.findByEmail(userEmail);
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