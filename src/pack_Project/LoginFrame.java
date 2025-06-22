// File: LoginFrame.java
package pack_Project;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginFrame extends JFrame {
    private PersonalFinanceManagerApp app; // Reference to the main application
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;

    public LoginFrame(PersonalFinanceManagerApp app) {
        this.app = app;
        setTitle("Personal Finance Manager - Login");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the frame


        initComponents(); // Initialize UI components
        addListeners();   // Add event listeners
        setVisible(true);
    }


    private void initComponents() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBackground(new Color(240, 248, 255));
        panel.setBorder(new EmptyBorder(40, 40, 40, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;


        JLabel titleLabel = new JLabel("Welcome to PFM");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(0, 102, 204));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(titleLabel, gbc);


        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(usernameLabel, gbc);

        usernameField = new JTextField(15);
        usernameField.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 1;
        gbc.gridy = 1;
        panel.add(usernameField, gbc);


        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(passwordLabel, gbc);

        passwordField = new JPasswordField(15);
        passwordField.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 1;
        gbc.gridy = 2;
        panel.add(passwordField, gbc);

        // Login Button
        loginButton = new JButton("Login");
        loginButton.setFont(new Font("Arial", Font.BOLD, 16));
        loginButton.setBackground(new Color(63, 150, 219));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        loginButton.setBorderPainted(false);
        loginButton.setOpaque(true);
        loginButton.setPreferredSize(new Dimension(100, 40));
        loginButton.setBorder(BorderFactory.createLineBorder(new Color(40, 100, 150), 2)); // Darker border
        loginButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                loginButton.setBackground(new Color(90, 170, 230));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                loginButton.setBackground(new Color(63, 150, 219));
            }
        });
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(loginButton, gbc);

        add(panel);
    }


    private void addListeners() {
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                attemptLogin();
            }
        });


        passwordField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                attemptLogin();
            }
        });
    }


    private void attemptLogin() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        User authenticatedUser = app.getUserDAO().authenticateUser(username, password);

        if (authenticatedUser != null) {
            CustomMessageBox.showInfoMessage(this, "Login Successful!", "Success");
            dispose();
            new DashboardFrame(app, authenticatedUser);
        } else {
            CustomMessageBox.showErrorMessage(this, "Invalid Username or Password.", "Login Failed");
            passwordField.setText("");
        }
    }
}
