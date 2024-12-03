
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.Scanner;

public class MainClass {

    private static final String ACCOUNT_FILE = "accounts.txt";

    public static void main(String[] args) {
        JFrame frame = new JFrame("Login Interface");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 350);
        frame.setLayout(new BorderLayout());

        JPanel contentPanel = new JPanel(new CardLayout());
        frame.add(contentPanel, BorderLayout.CENTER);

        JPanel loginPanel = createLoginPanel(contentPanel);
        JPanel createAccountPanel = createAccountPanel(contentPanel);

        contentPanel.add(loginPanel, "LoginPanel");
        contentPanel.add(createAccountPanel, "CreateAccountPanel");

        frame.setVisible(true);
    }

    private static JPanel createLoginPanel(JPanel contentPanel) {
        JPanel loginPanel = new JPanel();
        loginPanel.setLayout(new GridBagLayout());
        loginPanel.setBackground(new Color(240, 248, 255));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 10, 5, 10);

        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setFont(new Font("Arial", Font.BOLD, 14));
        JTextField emailField = new JTextField(15);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Arial", Font.BOLD, 14));

        // Create a password panel to house both the password field and the eye button
        JPanel passwordPanel = new JPanel(new GridBagLayout());
        passwordPanel.setBackground(Color.WHITE);

        JPasswordField passwordField = new JPasswordField(15);
        JButton eyeButton = createEyeButton(passwordField);

        GridBagConstraints pGbc = new GridBagConstraints();
        pGbc.fill = GridBagConstraints.HORIZONTAL;
        pGbc.weightx = 1.0;
        pGbc.insets = new Insets(0, 0, 0, 0);

        // Add password field
        pGbc.gridx = 0;
        pGbc.gridy = 0;
        pGbc.weightx = 1.0; // Allow the password field to take most of the space
        passwordPanel.add(passwordField, pGbc);

        // Add eye button
        pGbc.gridx = 1;
        pGbc.weightx = 0; // Eye button doesn't expand
        passwordPanel.add(eyeButton, pGbc);

        JRadioButton adminButton = new JRadioButton("Admin");
        JRadioButton customerButton = new JRadioButton("Customer");
        adminButton.setBackground(new Color(240, 248, 255));
        customerButton.setBackground(new Color(240, 248, 255));
        ButtonGroup roleGroup = new ButtonGroup();
        roleGroup.add(adminButton);
        roleGroup.add(customerButton);

        JButton loginButton = new JButton("Log In");
        JButton createAccountSwitchButton = new JButton("Create New Account");

        // Add components to the login panel
        gbc.gridx = 0;
        gbc.gridy = 0;
        loginPanel.add(emailLabel, gbc);
        gbc.gridx = 1;
        loginPanel.add(emailField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        loginPanel.add(passwordLabel, gbc);
        gbc.gridx = 1;
        loginPanel.add(passwordPanel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        JPanel rolePanel = new JPanel(new FlowLayout());
        rolePanel.setBackground(new Color(240, 248, 255));
        rolePanel.add(adminButton);
        rolePanel.add(customerButton);
        loginPanel.add(rolePanel, gbc);

        gbc.gridy = 3;
        loginPanel.add(loginButton, gbc);

        gbc.gridy = 4;
        loginPanel.add(createAccountSwitchButton, gbc);

        createAccountSwitchButton.addActionListener(e -> {
            CardLayout cl = (CardLayout) contentPanel.getLayout();
            cl.show(contentPanel, "CreateAccountPanel");
        });

        loginButton.addActionListener(e -> {
            String email = emailField.getText();
            String password = new String(passwordField.getPassword());
            String role = adminButton.isSelected() ? "Admin" : customerButton.isSelected() ? "Customer" : null;

            if (role == null) {
                JOptionPane.showMessageDialog(loginPanel, "Please select a role!", "Error", JOptionPane.ERROR_MESSAGE);
            } else if (email.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(loginPanel, "Email or Password cannot be empty!", "Error", JOptionPane.ERROR_MESSAGE);
            } else if (role.equals("Admin")) {
                JOptionPane.showMessageDialog(loginPanel, "Admin logged in successfully:\nEmail: " + email, "Success", JOptionPane.INFORMATION_MESSAGE);
            } else if (role.equals("Customer")) {
                if (verifyCustomer(email, password)) {
                    JOptionPane.showMessageDialog(loginPanel, "Customer logged in successfully:\nEmail: " + email, "Success", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(loginPanel, "Invalid credentials. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        return loginPanel;
    }

    private static JPanel createAccountPanel(JPanel contentPanel) {
        JPanel createAccountPanel = new JPanel();
        createAccountPanel.setLayout(new GridBagLayout());
        createAccountPanel.setBackground(new Color(255, 228, 225));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        // Labels
        JLabel newNameLabel = new JLabel("Enter Name:");
        newNameLabel.setFont(new Font("Arial", Font.BOLD, 14));

        JLabel newEmailLabel = new JLabel("Enter New Email:");
        newEmailLabel.setFont(new Font("Arial", Font.BOLD, 14));

        JLabel newPasswordLabel = new JLabel("Enter New Password:");
        newPasswordLabel.setFont(new Font("Arial", Font.BOLD, 14));

        // Input fields
        JTextField newNameField = new JTextField(20);
        JTextField newEmailField = new JTextField(20);
        JPasswordField newPasswordField = new JPasswordField(15);

        // Eye button for password visibility
        JPanel passwordPanel = new JPanel(new GridBagLayout());
        passwordPanel.setBackground(Color.WHITE);

        JButton eyeButton = createEyeButton(newPasswordField);

        GridBagConstraints pGbc = new GridBagConstraints();
        pGbc.fill = GridBagConstraints.HORIZONTAL;
        pGbc.insets = new Insets(0, 0, 0, 0);

        // Add password field to panel
        pGbc.gridx = 0;
        pGbc.gridy = 0;
        pGbc.weightx = 1.0; // Expand password field
        passwordPanel.add(newPasswordField, pGbc);

        // Add eye button beside password field
        pGbc.gridx = 1;
        pGbc.weightx = 0; // No expansion for button
        passwordPanel.add(eyeButton, pGbc);

        // Buttons
        JButton registerButton = new JButton("Register");
        JButton backToLoginButton = new JButton("Back to Login");

        // Add components to the panel
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.LINE_END;
        createAccountPanel.add(newNameLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.LINE_START;
        createAccountPanel.add(newNameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.LINE_END;
        createAccountPanel.add(newEmailLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        createAccountPanel.add(newEmailField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.LINE_END;
        createAccountPanel.add(newPasswordLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.LINE_START;
        createAccountPanel.add(passwordPanel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.CENTER;
        createAccountPanel.add(registerButton, gbc);

        gbc.gridy = 4;
        createAccountPanel.add(backToLoginButton, gbc);

        // Register button action
        registerButton.addActionListener(e -> {
            String name = newNameField.getText();
            String newEmail = newEmailField.getText();
            String newPassword = new String(newPasswordField.getPassword());

            if (name.isEmpty() || newEmail.isEmpty() || newPassword.isEmpty()) {
                JOptionPane.showMessageDialog(createAccountPanel, "All fields must be filled!", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                if (registerCustomer(name, newEmail, newPassword)) {
                    JOptionPane.showMessageDialog(createAccountPanel, "Account created successfully:\nEmail: " + newEmail, "Success", JOptionPane.INFORMATION_MESSAGE);
                    CardLayout cl = (CardLayout) contentPanel.getLayout();
                    cl.show(contentPanel, "LoginPanel");
                } else {
                    JOptionPane.showMessageDialog(createAccountPanel, "Account already exists. Please use a different email.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Back to login button action
        backToLoginButton.addActionListener(e -> {
            CardLayout cl = (CardLayout) contentPanel.getLayout();
            cl.show(contentPanel, "LoginPanel");
        });

        return createAccountPanel;
    }

    private static JButton createEyeButton(JPasswordField passwordField) {
        JButton eyeButton = new JButton("\uD83D\uDC41"); // Unicode for eye
        eyeButton.setPreferredSize(new Dimension(30, 20));
        eyeButton.setFocusable(false);
        eyeButton.setBackground(Color.WHITE);
        eyeButton.setBorder(null); // Remove border for a clean look
        eyeButton.addActionListener(new ActionListener() {
            private boolean showing = false;

            @Override
            public void actionPerformed(ActionEvent e) {
                if (showing) {
                    passwordField.setEchoChar('*'); // Hide characters
                } else {
                    passwordField.setEchoChar((char) 0); // Show characters
                }
                showing = !showing;
            }
        });
        return eyeButton;
    }

    private static boolean registerCustomer(String name, String email, String password) {
        try {
            if (verifyCustomer(email, null)) {
                return false;
            }
            BufferedWriter writer = new BufferedWriter(new FileWriter(ACCOUNT_FILE, true));
            writer.write(name + "," + email + "," + password); // Save name, email, and password
            writer.newLine();
            writer.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private static boolean verifyCustomer(String email, String password) {
        try {
            File file = new File(ACCOUNT_FILE);
            if (!file.exists()) {
                return false;
            }
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String[] account = scanner.nextLine().split(",");
                if (account[1].equals(email)) { // Check email
                    if (password == null || account[2].equals(password)) { // Check password
                        return true;
                    }
                }
            }
            scanner.close();
        } catch (IOException e) {
        }
        return false;
    }
}
