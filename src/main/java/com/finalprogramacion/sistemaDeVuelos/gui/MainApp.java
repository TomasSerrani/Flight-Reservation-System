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
        mainPanel.add(createMainMenuPanel(), "MainMenu");
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
        JPanel loginPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Email
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.3;
        loginPanel.add(new JLabel("Email:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        JTextField emailField = new JTextField(15);
        loginPanel.add(emailField, gbc);

        // Password
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.3;
        loginPanel.add(new JLabel("Password:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        JPasswordField passwordField = new JPasswordField(15);
        loginPanel.add(passwordField, gbc);

        // Login Button
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton loginButton = new JButton("Login");
        loginPanel.add(loginButton, gbc);

        // Register Button
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton registerButton = new JButton("Register");
        loginPanel.add(registerButton, gbc);

        // Login Logic
        loginButton.addActionListener(e -> {
            String email = emailField.getText();
            String password = new String(passwordField.getPassword());
            UserDetails userDetails = userDetailsController.login(email, password);
            if (userDetails != null) {
                cardLayout.show(mainPanel, "MainMenu");
                this.userEmail = userDetails.getEmail();
            } else {
                JOptionPane.showMessageDialog(frame, "Invalid email or password");
            }
        });

        // Go to Register Panel
        registerButton.addActionListener(e -> cardLayout.show(mainPanel, "Register"));

        return loginPanel;
    }


    private JPanel createRegisterPanel() {
        JPanel registerPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Back Button
        JButton backButton = new JButton("Back");
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.WEST;
        registerPanel.add(backButton, gbc);

        // Name
        gbc.gridy++;
        gbc.gridwidth = 1;
        JLabel nameLabel = new JLabel("Name:");
        registerPanel.add(nameLabel, gbc);

        gbc.gridx = 1;
        JTextField nameField = new JTextField(15);
        registerPanel.add(nameField, gbc);

        // Date of Birth
        gbc.gridx = 0;
        gbc.gridy++;
        JLabel dateOfBirthLabel = new JLabel("Date of birth: DD/MM/YYYY");
        registerPanel.add(dateOfBirthLabel, gbc);

        gbc.gridx = 1;
        JTextField dateOfBirthField = new JTextField(15);
        registerPanel.add(dateOfBirthField, gbc);

        // Email
        gbc.gridx = 0;
        gbc.gridy++;
        JLabel emailLabel = new JLabel("Email:");
        registerPanel.add(emailLabel, gbc);

        gbc.gridx = 1;
        JTextField emailField = new JTextField(15);
        registerPanel.add(emailField, gbc);

        // Password
        gbc.gridx = 0;
        gbc.gridy++;
        JLabel passwordLabel = new JLabel("Password:");
        registerPanel.add(passwordLabel, gbc);

        gbc.gridx = 1;
        JPasswordField passwordField = new JPasswordField(15);
        registerPanel.add(passwordField, gbc);

        // Phone
        gbc.gridx = 0;
        gbc.gridy++;
        JLabel phoneLabel = new JLabel("Phone:");
        registerPanel.add(phoneLabel, gbc);

        gbc.gridx = 1;
        JTextField phoneField = new JTextField(15);
        registerPanel.add(phoneField, gbc);

        // Register Button
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton registerButton = new JButton("Register");
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

            try {
                Date dateOfBirth = new SimpleDateFormat("dd/MM/yyyy").parse(dateOfBirthText);
                UserDetailsDTO createdUserDetails = new UserDetailsDTO(email, password, phone);
                UserDTO createdUser = new UserDTO(name, dateOfBirth, createdUserDetails);

                if (userDetailsController.findByEmail(createdUserDetails.getEmail()) == null) {
                    userController.createUser(createdUser);
                    JOptionPane.showMessageDialog(frame, "Registration successful");
                    cardLayout.show(mainPanel, "Login");
                } else {
                    JOptionPane.showMessageDialog(frame, "Email already used", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (ParseException ex) {
                JOptionPane.showMessageDialog(frame, "Invalid date format. Use DD/MM/YYYY.", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Registration failed: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Back Button Action
        backButton.addActionListener(e -> cardLayout.show(mainPanel, "Login"));

        return registerPanel;
    }


    private JPanel createMainMenuPanel() {
        JPanel mainMenuPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Margen entre botones
        gbc.fill = GridBagConstraints.HORIZONTAL; // Los botones llenarán el espacio horizontalmente

        // Crear y estilizar los botones
        JButton searchFlightsButton = new JButton("Search Flights");
        styleButton(searchFlightsButton);
        searchFlightsButton.addActionListener(e -> cardLayout.show(mainPanel, "FlightSearch"));

        JButton viewReservationsButton = new JButton("View Reservations");
        styleButton(viewReservationsButton);
        viewReservationsButton.addActionListener(e -> cardLayout.show(mainPanel, "UserReservations"));

        JButton viewPaymentsButton = new JButton("View Payments");
        styleButton(viewPaymentsButton);
        viewPaymentsButton.addActionListener(e -> cardLayout.show(mainPanel, "UserPayments"));

        // Añadir los botones al panel
        gbc.gridx = 0;
        gbc.gridy = 0;
        mainMenuPanel.add(searchFlightsButton, gbc);

        gbc.gridy = 1;
        mainMenuPanel.add(viewReservationsButton, gbc);

        gbc.gridy = 2;
        mainMenuPanel.add(viewPaymentsButton, gbc);

        return mainMenuPanel;
    }

    private void styleButton(JButton button) {
        // Estilizar los botones para que sean más pequeños y visualmente atractivos
        button.setPreferredSize(new Dimension(150, 40)); // Tamaño personalizado
        button.setFocusPainted(false); // Quitar borde al hacer clic
        button.setBackground(new Color(7, 7, 7)); // Color de fondo
        button.setForeground(Color.WHITE); // Color del texto
        button.setFont(new Font("Arial", Font.BOLD, 14)); // Fuente personalizada
        button.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15)); // Añadir márgenes internos
        button.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Cambiar el cursor al pasar sobre el botón
    }

    // Método para crear el botón de retroceso
    private JButton createBackButton(String previousPanel) {
        JButton backButton = new JButton("<-");
        backButton.addActionListener(e -> {
            // Cambia al panel anterior
            cardLayout.show(mainPanel, previousPanel);
        });
        return backButton;
    }

    // Método para crear el panel de búsqueda de vuelos
    private JPanel createFlightSearchPanel() {
        JPanel flightSearchPanel = new JPanel(new BorderLayout());

        // Crear panel para los botones de navegación
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton backButtonToMainMenu = new JButton("Main Menu");
        backButtonToMainMenu.addActionListener(e -> cardLayout.show(mainPanel, "MainMenu"));
        buttonPanel.add(backButtonToMainMenu);

        // Crear panel para los campos de búsqueda
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

        // Configurar la JList para mostrar los vuelos
        JList<FlightDTO> flightList = new JList<>();
        JScrollPane flightListScrollPane = new JScrollPane(flightList);

        // Crear panel para el botón de selección de vuelo
        JPanel selectFlightPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton selectFlightButton = new JButton("Select Flight");
        selectFlightPanel.add(selectFlightButton);

        // Añadir los componentes al panel principal
        flightSearchPanel.add(buttonPanel, BorderLayout.NORTH);
        flightSearchPanel.add(searchPanel, BorderLayout.CENTER);
        flightSearchPanel.add(flightListScrollPane, BorderLayout.EAST);
        flightSearchPanel.add(selectFlightPanel, BorderLayout.SOUTH);

        searchButton.addActionListener(e -> {
            // Obtener origen y destino seleccionados
            String origin = (String) originComboBox.getSelectedItem();
            String destination = (String) destinationComboBox.getSelectedItem();

            if (origin != null && destination != null) {
                // Buscar vuelos
                List<FlightDTO> matchingFlights = flightController.searchFlightsByOriginAndDestination(origin, destination);

                // Mostrar los vuelos en la JList
                flightList.setListData(matchingFlights.toArray(new FlightDTO[0]));
            } else {
                // Limpiar la lista si no hay selección válida
                flightList.setListData(new FlightDTO[0]);
            }
        });

        selectFlightButton.addActionListener(e -> {
            FlightDTO selectedFlight = flightList.getSelectedValue();
            if (selectedFlight != null) {
                JPanel flightDetailsPanel = showFlightDetails(selectedFlight);
                cardLayout.show(mainPanel, "FlightDetails");
                mainPanel.remove(flightDetailsPanel);
                mainPanel.add(flightDetailsPanel, "FlightDetails");
            } else {
                JOptionPane.showMessageDialog(flightSearchPanel, "Please select a flight.");
            }
        });

        populateFlightComboBoxes(originComboBox, destinationComboBox);

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

    private JPanel showFlightDetails(FlightDTO flightDTO) {
        // Crear un nuevo panel de detalles de vuelo
        JPanel flightDetailsPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Crear panel para los botones (volver atrás y menú principal)
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        // Botón para volver atrás (por ejemplo, volver a la lista de vuelos)
        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> cardLayout.show(mainPanel, "FlightSearch"));

        // Botón para regresar al menú principal
        JButton backButtonToMainMenu = new JButton("Main Menu");
        backButtonToMainMenu.addActionListener(e -> cardLayout.show(mainPanel, "MainMenu"));

        // Añadir botones al panel de botones
        buttonPanel.add(backButton);
        buttonPanel.add(backButtonToMainMenu);

        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        flightDetailsPanel.add(buttonPanel, gbc);

        // Mostrar el panel de detalles en el CardLayout
        mainPanel.add(flightDetailsPanel, "FlightDetails");
        cardLayout.show(mainPanel, "FlightDetails");

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
                    // Aquí puedes agregar la lógica para procesar el pago con tarjeta
                    // Ejemplo: procesarPagoConTarjeta(cardNumber, expiryDate, cvv, numberOfPayments);
                } else if ("Bank Transfer".equals(selectedMethod)) {
                    String bankAccount = bankAccountField.getText();
                    String bankName = bankNameField.getText();
                    // Aquí puedes agregar la lógica para procesar el pago por transferencia bancaria
                    // Ejemplo: procesarPagoConTransferencia(bankAccount, bankName, numberOfPayments);
                }

                try {
                    reservationController.createReservation(toReservationDTO(reservation));
                    JOptionPane.showMessageDialog(frame, "Flight booked and payment successful");
                    cardLayout.show(mainPanel, "FlightSearch"); // Volver a la búsqueda de vuelos después del pago
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

        backButton.addActionListener(e -> cardLayout.show(mainPanel, "FlightSearch"));

        mainPanel.add(flightDetailsPanel, "FlightDetails");
        cardLayout.show(mainPanel, "FlightDetails");
        return flightDetailsPanel;
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