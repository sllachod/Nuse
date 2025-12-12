package org.unsa.ui;

import org.unsa.services.UserService;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LoginPanel extends JPanel {
    private MainFrame mainFrame;
    private UserService userService;
    private JTextField usernameField;
    private JPasswordField passwordField;
    
    public LoginPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.userService = new UserService();
        
        setLayout(new BorderLayout());
        setBackground(StyleConstants.BLACK);
        
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(StyleConstants.BLACK);
        
        StyleConstants.RoundedPanel formPanel = new StyleConstants.RoundedPanel(30, StyleConstants.DARK_GRAY);
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setPreferredSize(new Dimension(400, 480));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        
        JLabel titleLabel = new JLabel("INICIAR SESIÓN");
        titleLabel.setFont(StyleConstants.TITLE_FONT);
        titleLabel.setForeground(StyleConstants.PRIMARY_RED);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        formPanel.add(Box.createVerticalStrut(20));
        formPanel.add(titleLabel);
        formPanel.add(Box.createVerticalStrut(40));
        
        JLabel usernameLabel = new JLabel("Usuario");
        usernameLabel.setFont(StyleConstants.NORMAL_FONT);
        usernameLabel.setForeground(StyleConstants.TEXT_COLOR);
        usernameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        formPanel.add(usernameLabel);
        formPanel.add(Box.createVerticalStrut(5));
        
        usernameField = StyleConstants.createStyledTextField();
        usernameField.setMaximumSize(new Dimension(320, 40));
        usernameField.setAlignmentX(Component.CENTER_ALIGNMENT);
        formPanel.add(usernameField);
        formPanel.add(Box.createVerticalStrut(20));
        
        JLabel passwordLabel = new JLabel("Contraseña");
        passwordLabel.setFont(StyleConstants.NORMAL_FONT);
        passwordLabel.setForeground(StyleConstants.TEXT_COLOR);
        passwordLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        formPanel.add(passwordLabel);
        formPanel.add(Box.createVerticalStrut(5));
        
        passwordField = StyleConstants.createStyledPasswordField();
        passwordField.setMaximumSize(new Dimension(320, 40));
        passwordField.setAlignmentX(Component.CENTER_ALIGNMENT);
        formPanel.add(passwordField);
        formPanel.add(Box.createVerticalStrut(40));
        
        JButton loginButton = new StyleConstants.RoundedButton("INGRESAR", StyleConstants.PRIMARY_RED, StyleConstants.HOVER_RED);
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginButton.setMaximumSize(new Dimension(320, 45));
        loginButton.addActionListener(e -> handleLogin());
        formPanel.add(loginButton);
        formPanel.add(Box.createVerticalStrut(15));
        
        JButton registerButton = new StyleConstants.RoundedButton("REGISTRARSE", StyleConstants.LIGHT_GRAY, StyleConstants.DARK_GRAY.brighter());
        registerButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        registerButton.setMaximumSize(new Dimension(320, 45));
        registerButton.addActionListener(e -> mainFrame.showPanel("register"));
        formPanel.add(registerButton);
        formPanel.add(Box.createVerticalStrut(20));
        
        centerPanel.add(formPanel);
        add(centerPanel, BorderLayout.CENTER);
    }
    
    private void handleLogin() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        
        if (username.isEmpty() || password.isEmpty()) {
            showError("Por favor complete todos los campos");
            return;
        }
        
        String userType = userService.loginUser(username, password);
        
        if (userType.equals("donor")) {
            mainFrame.showDonorPanel(username);
        } else if (userType.equals("association")) {
            mainFrame.showAssociationPanel(username);
        } else {
            showError("Usuario o contraseña incorrectos");
        }
    }
    
    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
