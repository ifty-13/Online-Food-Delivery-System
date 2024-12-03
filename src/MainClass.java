
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
        JPasswordField passwordField = new JPasswordField(15);

        // Eye button for password visibility
        JButton eyeButton = createEyeButton(passwordField);

        JRadioButton adminButton = new JRadioButton("Admin");
        JRadioButton customerButton = new JRadioButton("Customer");
        adminButton.setBackground(new Color(240, 248, 255));
        customerButton.setBackground(new Color(240, 248, 255));
        ButtonGroup roleGroup = new ButtonGroup();
        roleGroup.add(adminButton);
        roleGroup.add(customerButton);

        JButton loginButton = new JButton("Log In");
        JButton createAccountSwitchButton = new JButton("Create New Account");

        // Add components
        gbc.gridx = 0;
        gbc.gridy = 0;
        loginPanel.add(emailLabel, gbc);
        gbc.gridx = 1;
        loginPanel.add(emailField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        loginPanel.add(passwordLabel, gbc);
        gbc.gridx = 1;
        loginPanel.add(passwordField, gbc);

        gbc.gridx = 2;
        loginPanel.add(eyeButton, gbc);

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
        JLabel newEmailLabel = new JLabel("Enter New Email:");
        newEmailLabel.setFont(new Font("Arial", Font.BOLD, 14));

        JLabel newPasswordLabel = new JLabel("Enter New Password:");
        newPasswordLabel.setFont(new Font("Arial", Font.BOLD, 14));

        // Input fields
        JTextField newEmailField = new JTextField(20);
        JPasswordField newPasswordField = new JPasswordField(20);

        // Eye button for password visibility
        JButton eyeButton = createEyeButton(newPasswordField);

        // Buttons
        JButton registerButton = new JButton("Register");
        JButton backToLoginButton = new JButton("Back to Login");

        // Add components to the panel
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.LINE_END;
        createAccountPanel.add(newEmailLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.LINE_START;
        createAccountPanel.add(newEmailField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.LINE_END;
        createAccountPanel.add(newPasswordLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        createAccountPanel.add(newPasswordField, gbc);

        // Add eye button beside password field
        gbc.gridx = 2;
        gbc.gridy = 1;
        createAccountPanel.add(eyeButton, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        createAccountPanel.add(registerButton, gbc);

        gbc.gridy = 3;
        createAccountPanel.add(backToLoginButton, gbc);

        // Register button action
        registerButton.addActionListener(e -> {
            String newEmail = newEmailField.getText();
            String newPassword = new String(newPasswordField.getPassword());

            if (newEmail.isEmpty() || newPassword.isEmpty()) {
                JOptionPane.showMessageDialog(createAccountPanel, "Email or Password cannot be empty!", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                if (registerCustomer(newEmail, newPassword)) {
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
        JButton eyeButton = new JButton("\uD83D\uDC41"); // Unicode for an eye symbol
        eyeButton.setPreferredSize(new Dimension(50, 30));
        eyeButton.setFocusable(false);
        eyeButton.addActionListener(new ActionListener() {
            boolean showing = false;

            @Override
            public void actionPerformed(ActionEvent e) {
                if (showing) {
                    passwordField.setEchoChar('*');
                } else {
                    passwordField.setEchoChar((char) 0); // Show characters
                }
                showing = !showing;
            }
        });
        return eyeButton;
    }

    private static boolean registerCustomer(String email, String password) {
        try {
            if (verifyCustomer(email, null)) {
                return false;
            }
            BufferedWriter writer = new BufferedWriter(new FileWriter(ACCOUNT_FILE, true));
            writer.write(email + "," + password);
            writer.newLine();
            writer.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
/*
    private static boolean verifyCustomer(String email, String password) {
        try {
            File file = new File(ACCOUNT_FILE);
            if (!file.exists()) {
                return false;
            }
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String[] account = scanner.nextLine().split(",");
                if (account[0].equals(email)) {
                    if (password == null || account[1].equals(password)) {
                        return true;
                    }
                }
            }
            scanner.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }*/
}
