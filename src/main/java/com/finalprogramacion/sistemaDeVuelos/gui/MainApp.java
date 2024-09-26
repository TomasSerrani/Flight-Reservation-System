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
import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;

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
    private PassengerController passengerController;

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
        JPanel loginPanel = new JPanel(new BorderLayout()); // Use BorderLayout for better structure
        loginPanel.setBackground(Color.LIGHT_GRAY); // Soft background color

        // Create a panel for the input fields and buttons
        JPanel inputPanel = new JPanel(new GridBagLayout()); // Use GridBagLayout for better control
        GridBagConstraints gbc = new GridBagConstraints();
        inputPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Add margins
        inputPanel.setBackground(Color.LIGHT_GRAY);

        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        JTextField emailField = new JTextField(15); // Set fixed width for text field

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        JPasswordField passwordField = new JPasswordField(15);

        JButton loginButton = new JButton("Login");
        loginButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        loginButton.setBackground(new Color(70, 130, 180));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);

        JButton registerButton = new JButton("Register");
        registerButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        registerButton.setBackground(new Color(70, 130, 180));
        registerButton.setForeground(Color.WHITE);
        registerButton.setFocusPainted(false);

        // Smaller "Forgot Password" button and centered
        JButton forgotPasswordButton = new JButton("Forgot Password");
        forgotPasswordButton.setFont(new Font("Segoe UI", Font.BOLD, 12));  // Slightly smaller font
        forgotPasswordButton.setBackground(new Color(255, 165, 0)); // Orange color
        forgotPasswordButton.setForeground(Color.WHITE);
        forgotPasswordButton.setFocusPainted(false);
        forgotPasswordButton.setPreferredSize(new Dimension(140, 30)); // Slightly smaller size

        // Add components to the input panel using GridBagLayout
        gbc.insets = new Insets(10, 10, 10, 10);  // Spacing between components
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        inputPanel.add(emailLabel, gbc);

        gbc.gridx = 1;
        inputPanel.add(emailField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        inputPanel.add(passwordLabel, gbc);

        gbc.gridx = 1;
        inputPanel.add(passwordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        inputPanel.add(loginButton, gbc);

        gbc.gridy = 3;
        inputPanel.add(registerButton, gbc);

        // Center the "Forgot Password" button with a smaller size
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER; // Centering the button
        inputPanel.add(forgotPasswordButton, gbc);

        // Add the input panel to the center of the login panel
        loginPanel.add(inputPanel, BorderLayout.CENTER);

        // Logic for buttons
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

        registerButton.addActionListener(e -> cardLayout.show(mainPanel, "Register"));

        forgotPasswordButton.addActionListener(e -> {
            String email = JOptionPane.showInputDialog(frame, "Enter your registered email:", "Reset Password", JOptionPane.PLAIN_MESSAGE);

            if (email != null && !email.isEmpty()) {
                String resetResult = requestPasswordRecovery(email);
                JOptionPane.showMessageDialog(frame, resetResult, "Password Reset", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(frame, "Email cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        return loginPanel;
    }


    private JPanel createMainMenuPanel() {
        JPanel mainMenuPanel = new JPanel(new BorderLayout()); // Use BorderLayout for better structure
        mainMenuPanel.setBackground(Color.LIGHT_GRAY); // Soft background color

        // Title label
        JLabel welcomeLabel = new JLabel("Flight Reservation System", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        welcomeLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0)); // Add margin around the label
        mainMenuPanel.add(welcomeLabel, BorderLayout.NORTH);

        // Panel for the buttons
        JPanel buttonPanel = new JPanel(new GridLayout(4, 1, 10, 10)); // 4 buttons with spacing
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40)); // Margins around the panel
        buttonPanel.setBackground(Color.LIGHT_GRAY);

        JButton searchFlightsButton = new JButton("Search Flights");
        searchFlightsButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        searchFlightsButton.setBackground(new Color(70, 130, 180));
        searchFlightsButton.setForeground(Color.WHITE);
        searchFlightsButton.setFocusPainted(false);

        JButton viewReservationsButton = new JButton("My Reservations");
        viewReservationsButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        viewReservationsButton.setBackground(new Color(70, 130, 180));
        viewReservationsButton.setForeground(Color.WHITE);
        viewReservationsButton.setFocusPainted(false);

        JButton viewPaymentsButton = new JButton("My Payments");
        viewPaymentsButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        viewPaymentsButton.setBackground(new Color(70, 130, 180));
        viewPaymentsButton.setForeground(Color.WHITE);
        viewPaymentsButton.setFocusPainted(false);

        JButton logoutButton = new JButton("Logout");
        logoutButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        logoutButton.setBackground(new Color(255, 69, 0)); // Orange-red color for logout
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setFocusPainted(false);

        // Add buttons to the panel
        buttonPanel.add(searchFlightsButton);
        buttonPanel.add(viewReservationsButton);
        buttonPanel.add(viewPaymentsButton);
        buttonPanel.add(logoutButton);

        // Add button panel to the center of the main menu panel
        mainMenuPanel.add(buttonPanel, BorderLayout.CENTER);

        // Logic for buttons
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
        JButton backButton = new JButton("Main Menu");
        backButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        backButton.setBackground(new Color(70, 130, 180));
        backButton.setForeground(Color.WHITE);
        backButton.setFocusPainted(false);
            backButton.addActionListener(e -> cardLayout.show(mainPanel, "MainMenu"));
            return backButton;
    }

    private JButton createBackToRegisterButton() {
        JButton backButton = new JButton("<-Back");
        backButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        backButton.setBackground(new Color(70, 130, 180));
        backButton.setForeground(Color.WHITE);
        backButton.setFocusPainted(false);
        backButton.addActionListener(e -> cardLayout.show(mainPanel, "MainMenu"));
        return backButton;
    }

    private JPanel createRegisterPanel() {
        JPanel registerPanel = new JPanel(new GridBagLayout());
        registerPanel.setBackground(Color.LIGHT_GRAY); // Soft background color
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);  // More generous padding for a cleaner look
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Back button to return to the login screen
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.WEST;
        JButton backButton = new JButton("Back");  // Reemplazamos el método con el botón directamente
        backButton.setBackground(new Color(70, 130, 180));
        backButton.setForeground(Color.WHITE);
        backButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        backButton.addActionListener(e -> cardLayout.show(mainPanel, "Login"));  // Cambiamos a la pantalla de Login
        registerPanel.add(backButton, gbc);

        // Name label and field
        gbc.gridy++;
        gbc.gridwidth = 1;
        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        registerPanel.add(nameLabel, gbc);

        JTextField nameField = new JTextField(15);
        gbc.gridx = 1;
        registerPanel.add(nameField, gbc);

        // Date of birth label and field
        gbc.gridx = 0;
        gbc.gridy++;
        JLabel dateOfBirthLabel = new JLabel("Date of birth: DD/MM/YYYY");
        dateOfBirthLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        registerPanel.add(dateOfBirthLabel, gbc);

        JTextField dateOfBirthField = new JTextField(15);
        gbc.gridx = 1;
        registerPanel.add(dateOfBirthField, gbc);

        // Email label and field
        gbc.gridx = 0;
        gbc.gridy++;
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        registerPanel.add(emailLabel, gbc);

        JTextField emailField = new JTextField(15);
        gbc.gridx = 1;
        registerPanel.add(emailField, gbc);

        // Password label and field
        gbc.gridx = 0;
        gbc.gridy++;
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        registerPanel.add(passwordLabel, gbc);

        JPasswordField passwordField = new JPasswordField(15);
        gbc.gridx = 1;
        registerPanel.add(passwordField, gbc);

        // Phone label and field
        gbc.gridx = 0;
        gbc.gridy++;
        JLabel phoneLabel = new JLabel("Phone:");
        phoneLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        registerPanel.add(phoneLabel, gbc);

        JTextField phoneField = new JTextField(15);
        gbc.gridx = 1;
        registerPanel.add(phoneField, gbc);

        // Register button (smaller size and centered)
        JButton registerButton = new JButton("Register");
        registerButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        registerButton.setBackground(new Color(70, 130, 180));
        registerButton.setForeground(Color.WHITE);
        registerButton.setFocusPainted(false);
        registerButton.setPreferredSize(new Dimension(140, 35)); // Set a fixed size for the button

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE; // Center the register button
        registerPanel.add(registerButton, gbc);

        // Action listener for registration logic
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

            if (validateEmail(email) == null) {
                JOptionPane.showMessageDialog(frame, "Invalid email", "Error", JOptionPane.ERROR_MESSAGE);
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
                if (userDetailsController.findByEmail(createdUserDetails.getEmail()) == null) {
                    userController.createUser(createdUser);
                    JOptionPane.showMessageDialog(frame, "Registration successful");
                } else {
                    JOptionPane.showMessageDialog(frame, "Registration failed: Email already used", "Error", JOptionPane.ERROR_MESSAGE);
                }
                cardLayout.show(mainPanel, "Login");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Registration failed: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        return registerPanel;
    }


    public String requestPasswordRecovery(String email) {
        // Check if the email exists in the system
        UserDetails userDetails = userDetailsController.findByEmail(email);
        if (userDetails == null) {
            return "Email not found. Please enter a registered email.";
        }

        // Generate a unique token (you can use UUID for this)
        String token = UUID.randomUUID().toString();

        // Store the token in the user's details or in a separate password reset table
        // Optionally set an expiration time for the token (e.g., valid for 24 hours)
        userDetails.setPasswordResetToken(token);
        userDetails.setPasswordResetExpiry(new Date(System.currentTimeMillis() + 86400000)); // 24 hours expiry
        userDetailsController.update(userDetails.getId(), toUserDetailsDTO(userDetails));

        // Send a password reset email
        String resetLink = "https://yourapp.com/reset-password?token=" + token;
        sendPasswordResetEmail(userDetails.getEmail(), resetLink);

        return "Password recovery email sent. Please check your inbox.";
    }


    private void sendPasswordResetEmail(String recipientEmail, String resetLink) {
        // Set up the SMTP server properties
        String host = "smtp.gmail.com"; // SMTP server (for Gmail)
        String port = "587";  // SMTP port (TLS)
        String username = "sistemreservation@gmail.com"; // Replace with your email
        String password = "khargwilgowwsemy"; // Replace with your app password or actual password

        // Set up email properties
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", port);
        props.put("mail.smtp.ssl.trust", host);

        // Authenticate the email session
        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            // Create a new email message
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username)); // From email
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail)); // To email
            message.setSubject("Password Reset Request"); // Email subject

            // Email body content
            String emailContent = "<h3>Password Reset Request</h3>"
                    + "<p>You requested to reset your password. Click the link below to reset it:</p>"
                    + "<a href='" + resetLink + "'>Reset Password</a>"
                    + "<p>If you did not request this, please ignore this email.</p>";

            message.setContent(emailContent, "text/html"); // HTML content

            // Send the email
            Transport.send(message);

            System.out.println("Password reset email sent successfully.");

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public String resetPassword(String token, String newPassword) {
        // Find the user with the given reset token
        UserDetails userDetails = userDetailsController.findByPasswordResetToken(token);
        if (userDetails == null) {
            return "Invalid or expired token.";
        }

        // Check if the token has expired
        if (userDetails.getPasswordResetExpiry().before(new Date())) {
            return "The reset token has expired. Please request a new password reset.";
        }

        // Validate the new password (you can add more validation logic here)
        if (newPassword == null || newPassword.isEmpty()) {
            return "Password cannot be empty.";
        }

        // Update the user's password and clear the reset token
        userDetails.setPassword(newPassword);  // Hash the password before saving it
        userDetails.setPasswordResetToken(null);  // Clear the token
        userDetails.setPasswordResetExpiry(null);  // Clear the expiry
        userDetailsController.update(userDetails.getId(), toUserDetailsDTO(userDetails));

        return "Your password has been successfully reset.";
    }

    public String validateEmail(String email) {
        // Check if email is empty
        if (email == null || email.isEmpty()) {
            return "Email cannot be empty.";
        }

        // Check if email format is valid using regex
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        if (!email.matches(emailRegex)) {
            return "Invalid email format.";
        }

        // Check if email is already registered (using a user details controller or service)
        UserDetails existingUser = userDetailsController.findByEmail(email);
        if (existingUser != null) {
            return "This email is already registered. Please use a different email.";
        }

        // If all validations pass, return null to indicate success
        return null;
    }


    private JPanel createFlightSearchPanel() {
        JPanel flightSearchPanel = new JPanel(new BorderLayout());
        flightSearchPanel.setBackground(Color.LIGHT_GRAY); // Soft background color

        // Panel for the search fields (origin, destination, departure date, passengers, and search button)
        JPanel searchPanel = new JPanel(new GridLayout(5, 2, 10, 10)); // Adjusted for new components

        JLabel originLabel = new JLabel("Origin:");
        originLabel.setFont(new Font("Segoe UI", Font.BOLD, 14)); // Bigger bold font
        JComboBox<String> originComboBox = new JComboBox<>();

        JLabel destinationLabel = new JLabel("Destination:");
        destinationLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        JComboBox<String> destinationComboBox = new JComboBox<>();

        // New components for departure date and number of passengers
        JLabel departureDateLabel = new JLabel("Departure Date:");
        departureDateLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        JTextField departureDateField = new JTextField();  // Alternatively, you could use a JDatePicker if available
        departureDateField.setToolTipText("Format: YYYY-MM-DD");

        JLabel passengersLabel = new JLabel("Passengers:");
        passengersLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        JSpinner passengersSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 10, 1));  // Min: 1, Max: 10 passengers

        JButton searchButton = new JButton("Search");
        searchButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        searchButton.setBackground(new Color(70, 130, 180)); // Background color for the button
        searchButton.setForeground(Color.WHITE); // Text color for the button
        searchButton.setFocusPainted(false);

        // Add components to the search panel
        searchPanel.add(originLabel);
        searchPanel.add(originComboBox);
        searchPanel.add(destinationLabel);
        searchPanel.add(destinationComboBox);
        searchPanel.add(departureDateLabel);
        searchPanel.add(departureDateField);
        searchPanel.add(passengersLabel);
        searchPanel.add(passengersSpinner);
        searchPanel.add(new JLabel()); // Empty cell to maintain layout
        searchPanel.add(searchButton);

        // Add the search panel to the north region of flightSearchPanel
        flightSearchPanel.add(searchPanel, BorderLayout.NORTH);

        // List to show flight search results
        JList<FlightDTO> flightList = new JList<>();
        JScrollPane flightScrollPane = new JScrollPane(flightList);
        flightScrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Add margin to JScrollPane
        flightSearchPanel.add(flightScrollPane, BorderLayout.CENTER);

        // Populate JComboBoxes with airport names
        populateFlightComboBoxes(originComboBox, destinationComboBox);

        // Search button action logic
        searchButton.addActionListener(e -> {
            String origin = (String) originComboBox.getSelectedItem();
            String destination = (String) destinationComboBox.getSelectedItem();
            String departureDate = departureDateField.getText();  // Get the departure date
            int passengers = (Integer) passengersSpinner.getValue();  // Get the number of passengers

            if (origin != null && destination != null && !departureDate.isEmpty()) {
                // Search for flights with additional parameters: origin, destination, departure date, and passengers
                List<FlightDTO> matchingFlights = flightController.searchFlights(origin, destination, departureDate, passengers);
                flightList.setListData(matchingFlights.toArray(new FlightDTO[0]));
            } else {
                flightList.setListData(new FlightDTO[0]); // Clear the list if any search criteria are missing
            }
        });

        // Configure the ListCellRenderer to show additional flight details
        flightList.setCellRenderer((list, value, index, isSelected, cellHasFocus) -> {
            JPanel panel = new JPanel(new GridLayout(1, 3));
            panel.setBorder(BorderFactory.createLineBorder(Color.GRAY)); // Add border to rows
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

        // Bottom panel for Main Menu and Select Flight buttons
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER)); // Use FlowLayout to center buttons

        // "Main Menu" button
        JButton mainMenuButton = new JButton("Main Menu");
        mainMenuButton.setBackground(new Color(70, 130, 180)); // Fondo azul
        mainMenuButton.setForeground(Color.WHITE); // Letras blancas
        mainMenuButton.addActionListener(e -> cardLayout.show(mainPanel, "MainMenu"));
        bottomPanel.add(mainMenuButton);

// "Select Flight" button
        JButton selectFlightButton = new JButton("Select Flight");
        selectFlightButton.setBackground(new Color(70, 130, 180)); // Fondo azul
        selectFlightButton.setForeground(Color.WHITE); // Letras blancas
        selectFlightButton.addActionListener(e -> {
            FlightDTO selectedFlight = flightList.getSelectedValue();
            if (selectedFlight != null) {
                showFlightDetails(selectedFlight);
            }
        });
        bottomPanel.add(selectFlightButton);

        // Add the bottom panel to flightSearchPanel
        flightSearchPanel.add(bottomPanel, BorderLayout.SOUTH);

        return flightSearchPanel;
    }

    // Modified method to accommodate the new search criteria (departureDate and passengers)
    private void populateFlightComboBoxes(JComboBox<String> originComboBox, JComboBox<String> destinationComboBox) {
        // Get the list of flights from the service
        List<FlightDTO> flights = flightController.getAllFlights();

        for (FlightDTO flight : flights) {
            String originFlightInfo = flight.getOrigin().getCity();
            if (((DefaultComboBoxModel<String>) originComboBox.getModel()).getIndexOf(originFlightInfo) == -1) {
                originComboBox.addItem(originFlightInfo);  // Avoid duplicate entries
            }

            String destinationFlightInfo = flight.getDestination().getCity();
            if (((DefaultComboBoxModel<String>) destinationComboBox.getModel()).getIndexOf(destinationFlightInfo) == -1) {
                destinationComboBox.addItem(destinationFlightInfo);  // Avoid duplicate entries
            }
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

        String departureDate = (flightDTO.getDepartureDate() != null) ? flightDTO.getDepartureDate().toString() : "Fecha no disponible";
        String departureTime = (flightDTO.getDepartureTime() != null) ? flightDTO.getDepartureTime().toString() : "Hora no disponible";

        // Mostrar la fecha y hora
        JLabel departureValue = new JLabel(departureDate + " " + departureTime);
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
        bookButton.setBackground(new Color(70, 130, 180));
        bookButton.setForeground(Color.WHITE);
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
            submitPaymentButton.setBackground(new Color(70, 130, 180));
            submitPaymentButton.setForeground(Color.WHITE);
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
                    PassengerDTO passengerDTO= new PassengerDTO();
                    passengerDTO.setUser(toUserDTO(currentUser));
                    passengerDTO.setEmail(userEmail);
                    passengerDTO.setName(currentUser.getName());
                    passengerDTO.setFlight(flightDTO);
                    passengerController.save(passengerDTO);

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
            backButton.setBackground(new Color(70, 130, 180));
            backButton.setForeground(Color.WHITE);
            paymentGbc.gridy = 5;
            paymentGbc.gridwidth = 2;
            paymentGbc.anchor = GridBagConstraints.CENTER;
            paymentPanel.add(backButton, paymentGbc);

            backButton.addActionListener(event -> cardLayout.show(mainPanel, "FlightDetails"));
        });

        JButton backButton = new JButton("<- Back");
        backButton.setBackground(new Color(70, 130, 180));
        backButton.setForeground(Color.WHITE);
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
        userReservationsPanel.setBackground(Color.LIGHT_GRAY); // Soft background

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

        // Sorting options and buttons panel
        JPanel buttonPanel = new JPanel(new GridBagLayout());
        buttonPanel.setBackground(Color.LIGHT_GRAY); // Set background color for the button panel
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);  // Generous padding for better spacing
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Sorting label and combo box
        JLabel sortByLabel = new JLabel("Sort by:");
        sortByLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        gbc.gridx = 0;
        gbc.gridy = 0;
        buttonPanel.add(sortByLabel, gbc);

        String[] sortingOptions = {"Date (Closest)", "Passengers (Most)", "Passengers (Least)"};
        JComboBox<String> sortingComboBox = new JComboBox<>(sortingOptions);
        sortingComboBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        gbc.gridx = 1;
        buttonPanel.add(sortingComboBox, gbc);

        // Refresh button
        JButton refreshButton = new JButton("Refresh");
        refreshButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        refreshButton.setBackground(new Color(70, 130, 180));  // Consistent color
        refreshButton.setForeground(Color.WHITE);
        refreshButton.setFocusPainted(false);
        gbc.gridx = 2;
        buttonPanel.add(refreshButton, gbc);

        // Modify button
        JButton modifyButton = new JButton("Modify");
        modifyButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        modifyButton.setBackground(new Color(70, 130, 180));
        modifyButton.setForeground(Color.WHITE);
        modifyButton.setFocusPainted(false);
        gbc.gridx = 3;
        buttonPanel.add(modifyButton, gbc);

        // Delete button
        JButton deleteButton = new JButton("Delete");
        deleteButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        deleteButton.setBackground(new Color(255, 69, 0)); // Red for delete
        deleteButton.setForeground(Color.WHITE);
        deleteButton.setFocusPainted(false);
        gbc.gridx = 4;
        buttonPanel.add(deleteButton, gbc);

        // Check-in button
        JButton checkInButton = new JButton("Check-in");
        checkInButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        checkInButton.setBackground(new Color(34, 139, 34)); // Green for check-in
        checkInButton.setForeground(Color.WHITE);
        checkInButton.setFocusPainted(false);
        gbc.gridx = 5;
        buttonPanel.add(checkInButton, gbc);

        // Main Menu button (for returning to main menu)
        JButton mainMenuButton = new JButton("Main Menu");
        mainMenuButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        mainMenuButton.setBackground(new Color(70, 130, 180));
        mainMenuButton.setForeground(Color.WHITE);
        mainMenuButton.setFocusPainted(false);
        gbc.gridx = 6;
        buttonPanel.add(mainMenuButton, gbc);

        // Add button panel to the top of the userReservationsPanel
        userReservationsPanel.add(buttonPanel, BorderLayout.NORTH);

        // Check-in button action
        checkInButton.addActionListener(e -> {
            ReservationDTO selectedReservation = reservationList.getSelectedValue(); // Obtener la reserva seleccionada
            if (selectedReservation != null) {
                performCheckIn(selectedReservation);  // Llamar al método performCheckIn
            } else {
                JOptionPane.showMessageDialog(frame, "Please select a reservation for Check-in.");
            }
        });

        // Refresh button action
        refreshButton.addActionListener(e -> {
            try {
                UserDetails userDetails1 = userDetailsController.findByEmail(userEmail);
                if (userDetails1 != null) {
                    String selectedSortOption = (String) sortingComboBox.getSelectedItem();
                    boolean ascending = !selectedSortOption.equals("Date (Furthest)");  // Ascending for "Date (Closest)"
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

        // Main Menu button action
        mainMenuButton.addActionListener(e -> {
            cardLayout.show(mainPanel, "MainMenu"); // Cambiar al panel del menú principal
        });

        // Custom renderer for reservation list
        reservationList.setCellRenderer((list, value, index, isSelected, cellHasFocus) -> {
            JPanel reservationPanel = new JPanel(new GridBagLayout());
            GridBagConstraints cellGbc = new GridBagConstraints();
            cellGbc.insets = new Insets(5, 5, 5, 5);  // Padding inside each reservation panel
            cellGbc.fill = GridBagConstraints.HORIZONTAL;
            reservationPanel.setBorder(BorderFactory.createTitledBorder("Reservation Details"));

            // Display Reservation Number
            cellGbc.gridx = 0;
            cellGbc.gridy = 0;
            reservationPanel.add(new JLabel("Reservation Number:"), cellGbc);

            cellGbc.gridx = 1;
            reservationPanel.add(new JLabel(String.valueOf(value.getNumber())), cellGbc);

            // Display Flight Number (if available)
            cellGbc.gridx = 0;
            cellGbc.gridy = 1;
            reservationPanel.add(new JLabel("Flight Number:"), cellGbc);

            if (value.getFlight() != null) {
                cellGbc.gridx = 1;
                reservationPanel.add(new JLabel(value.getFlight().getFlightNum()), cellGbc);

                // Display Passenger Count
                cellGbc.gridx = 0;
                cellGbc.gridy = 2;
                reservationPanel.add(new JLabel("Passenger Count:"), cellGbc);

                cellGbc.gridx = 1;
                reservationPanel.add(new JLabel(String.valueOf(value.getFlight().getPassengerCount())), cellGbc);
            } else {
                cellGbc.gridx = 1;
                reservationPanel.add(new JLabel("N/A"), cellGbc);
            }

            // Display Reservation Date
            cellGbc.gridx = 0;
            cellGbc.gridy = 3;
            reservationPanel.add(new JLabel("Reservation Date:"), cellGbc);

            cellGbc.gridx = 1;
            reservationPanel.add(new JLabel(value.getDate().toString()), cellGbc);

            // Display Reservation State
            cellGbc.gridx = 0;
            cellGbc.gridy = 4;
            reservationPanel.add(new JLabel("Reservation State:"), cellGbc);

            cellGbc.gridx = 1;
            reservationPanel.add(new JLabel(value.getState()), cellGbc);

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

        // Add reservation list to the center of the panel
        JScrollPane scrollPane = new JScrollPane(reservationList);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));  // Add padding around the scroll pane
        userReservationsPanel.add(scrollPane, BorderLayout.CENTER);

        return userReservationsPanel;
    }

    // Método de Check-in
    private void performCheckIn(ReservationDTO reservation) {
        // Verificar si el estado de la reserva permite Check-in
        if (!reservation.getState().equals("Booked")) {
            JOptionPane.showMessageDialog(frame, "Only booked reservations can be checked in.");
            return;
        }

        // Generar código de embarque
        String boardingPassCode = generateBoardingPassCode();

        // Actualizar el estado de la reserva a "Checked-in"
        reservation.setState("Checked-in");
        reservationController.updateReservationState(reservation.getNumber(), "Checked-in");

        // Mostrar tarjeta de embarque
        showBoardingPass(reservation, boardingPassCode);
    }

    // Genera un código de embarque único
    private String generateBoardingPassCode() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder boardingPassCode = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 8; i++) {
            boardingPassCode.append(characters.charAt(random.nextInt(characters.length())));
        }
        return boardingPassCode.toString();
    }

    // Muestra la tarjeta de embarque en un JOptionPane
    private void showBoardingPass(ReservationDTO reservation, String boardingPassCode) {
        JPanel boardingPassPanel = new JPanel(new GridLayout(0, 1));
        boardingPassPanel.add(new JLabel("Boarding Pass"));
        boardingPassPanel.add(new JLabel("Reservation Number: " + reservation.getNumber()));
        boardingPassPanel.add(new JLabel("Flight Number: " + reservation.getFlight().getFlightNum()));
        boardingPassPanel.add(new JLabel("Passenger Count: " + reservation.getFlight().getPassengerCount()));
        boardingPassPanel.add(new JLabel("Boarding Pass Code: " + boardingPassCode));

        JOptionPane.showMessageDialog(frame, boardingPassPanel, "Boarding Pass", JOptionPane.INFORMATION_MESSAGE);
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
        userPaymentsPanel.setBackground(Color.LIGHT_GRAY);  // Soft background color

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

        // Panel for buttons (Refresh and Main Menu)
        JPanel buttonPanel = new JPanel(new GridBagLayout());
        buttonPanel.setBackground(Color.LIGHT_GRAY);  // Consistent background color
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);  // Generous padding for button panel
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Refresh button
        JButton refreshButton = new JButton("Refresh");
        refreshButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        refreshButton.setBackground(new Color(70, 130, 180));  // Consistent color scheme
        refreshButton.setForeground(Color.WHITE);
        refreshButton.setFocusPainted(false);
        gbc.gridx = 0;
        gbc.gridy = 0;
        buttonPanel.add(refreshButton, gbc);

        // Main Menu button
        gbc.gridx= 1;
        buttonPanel.add(createBackButton(), gbc);

        // Add button panel to the top of the userPaymentsPanel
        userPaymentsPanel.add(buttonPanel, BorderLayout.NORTH);

        // Refresh button action
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

        // Custom renderer for payment list
        paymentList.setCellRenderer((list, value, index, isSelected, cellHasFocus) -> {
            JPanel paymentPanel = new JPanel(new GridBagLayout());
            GridBagConstraints cellGbc = new GridBagConstraints();
            cellGbc.insets = new Insets(5, 5, 5, 5);  // Padding inside each payment panel
            cellGbc.fill = GridBagConstraints.HORIZONTAL;

            paymentPanel.setBorder(BorderFactory.createTitledBorder("Payment Details"));

            // Display Payment Number
            cellGbc.gridx = 0;
            cellGbc.gridy = 0;
            paymentPanel.add(new JLabel("Payment Number:"), cellGbc);

            cellGbc.gridx = 1;
            paymentPanel.add(new JLabel(String.valueOf(value.getNumber())), cellGbc);

            // Display Payment Type
            cellGbc.gridx = 0;
            cellGbc.gridy = 1;
            paymentPanel.add(new JLabel("Payment Type:"), cellGbc);

            cellGbc.gridx = 1;
            paymentPanel.add(new JLabel(value.getType()), cellGbc);

            // Display Payment Amount
            cellGbc.gridx = 0;
            cellGbc.gridy = 2;
            paymentPanel.add(new JLabel("Amount of Payments:"), cellGbc);

            cellGbc.gridx = 1;
            paymentPanel.add(new JLabel(String.valueOf(value.getAmountOfPayments())), cellGbc);

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

        // Add payment list to the center of the panel
        JScrollPane scrollPane = new JScrollPane(paymentList);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));  // Padding around the list
        userPaymentsPanel.add(scrollPane, BorderLayout.CENTER);

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
        private MainApp mainApp; // Referencia a MainApp para navegar entre paneles

        // Constructor que recibe FlightDTO y MainApp
        public SeatSelectionPanel(FlightDTO flightDTO, MainApp mainApp) {
            this.flightDTO = flightDTO;
            this.mainApp = mainApp; // Guardar referencia a la aplicación principal
            initializeUI();
        }

        // Inicializar la interfaz gráfica
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

        // Listener para seleccionar asiento
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

        // Confirmar la selección del asiento y proceder al pago
        private void confirmSeatSelection() {
            if (selectedSeat != null) {
                flightDTO.reserveSeat(selectedSeat); // Reservar el asiento
                JOptionPane.showMessageDialog(this, "Asiento " + selectedSeat + " reservado. Proceder al pago.");

                // Navegar al panel de pago, pasando el FlightDTO actualizado
                mainApp.showPaymentPanel(flightDTO);
            } else {
                JOptionPane.showMessageDialog(this, "Por favor selecciona un asiento.");
            }
        }
    }
    // Método para mostrar el panel de selección de asientos
    public void showSeatSelectionPanel(FlightDTO flightDTO) {
        SeatSelectionPanel seatSelectionPanel = new SeatSelectionPanel(flightDTO, this);
        mainPanel.add(seatSelectionPanel, "SeatSelection");
        cardLayout.show(mainPanel, "SeatSelection");
    }

    // Método para mostrar el panel de pago después de la selección de asientos
    public void showPaymentPanel(FlightDTO flightDTO) {
        JPanel paymentPanel = createUserPaymentsPanel();
        mainPanel.add(paymentPanel, "Payment");
        cardLayout.show(mainPanel, "Payment");
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainApp::new);
    }
}