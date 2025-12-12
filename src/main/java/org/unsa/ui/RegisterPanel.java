package org.unsa.ui;

import org.unsa.services.DonorService;
import org.unsa.services.AssociationService;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLIntegrityConstraintViolationException;

public class RegisterPanel extends JPanel {
    private MainFrame mainFrame;
    private DonorService donorService;
    private AssociationService associationService;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JTextField emailField;
    private JTextField nameField;
    private JTextField addressField;
    private JComboBox<String> userTypeCombo;
    private JLabel addressLabel;
    
    public RegisterPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.donorService = new DonorService();
        this.associationService = new AssociationService();
        
        setLayout(new BorderLayout());
        setBackground(StyleConstants.BLACK);
        
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(StyleConstants.BLACK);
        
        StyleConstants.RoundedPanel formPanel = new StyleConstants.RoundedPanel(30, StyleConstants.DARK_GRAY);
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setPreferredSize(new Dimension(450, 650));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        
        JLabel titleLabel = new JLabel("REGISTRO");
        titleLabel.setFont(StyleConstants.TITLE_FONT);
        titleLabel.setForeground(StyleConstants.PRIMARY_RED);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        formPanel.add(Box.createVerticalStrut(20));
        formPanel.add(titleLabel);
        formPanel.add(Box.createVerticalStrut(30));
        
        String[] userTypes = {"Donante", "Asociación"};
        userTypeCombo = new JComboBox<>(userTypes);
        userTypeCombo.setFont(StyleConstants.NORMAL_FONT);
        userTypeCombo.setBackground(StyleConstants.LIGHT_GRAY);
        userTypeCombo.setForeground(StyleConstants.TEXT_COLOR);
        userTypeCombo.setMaximumSize(new Dimension(350, 40));
        userTypeCombo.addActionListener(e -> updateAddressLabel());
        formPanel.add(createLabelFieldPair("Tipo de Usuario", userTypeCombo));
        
        usernameField = StyleConstants.createStyledTextField();
        formPanel.add(createLabelFieldPair("Usuario", usernameField));
        
        passwordField = StyleConstants.createStyledPasswordField();
        formPanel.add(createLabelFieldPair("Contraseña", passwordField));
        
        emailField = StyleConstants.createStyledTextField();
        formPanel.add(createLabelFieldPair("Email", emailField));
        
        nameField = StyleConstants.createStyledTextField();
        formPanel.add(createLabelFieldPair("Nombre", nameField));
        
        addressLabel = new JLabel("Dirección");
        addressLabel.setFont(StyleConstants.NORMAL_FONT);
        addressLabel.setForeground(StyleConstants.TEXT_COLOR);
        addressLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        addressField = StyleConstants.createStyledTextField();
        formPanel.add(Box.createVerticalStrut(10));
        formPanel.add(addressLabel);
        formPanel.add(Box.createVerticalStrut(5));
        formPanel.add(addressField);
        formPanel.add(Box.createVerticalStrut(25));
        
        JButton registerButton = new StyleConstants.RoundedButton("REGISTRARSE", StyleConstants.PRIMARY_RED, StyleConstants.HOVER_RED);
        registerButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        registerButton.setMaximumSize(new Dimension(350, 45));
        registerButton.addActionListener(e -> handleRegister());
        formPanel.add(registerButton);
        formPanel.add(Box.createVerticalStrut(10));
        
        JButton backButton = new StyleConstants.RoundedButton("VOLVER", StyleConstants.LIGHT_GRAY, StyleConstants.DARK_GRAY.brighter());
        backButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        backButton.setMaximumSize(new Dimension(350, 45));
        backButton.addActionListener(e -> mainFrame.showPanel("login"));
        formPanel.add(backButton);
        formPanel.add(Box.createVerticalStrut(20));
        
        centerPanel.add(formPanel);
        add(centerPanel, BorderLayout.CENTER);
    }
    
    private JPanel createLabelFieldPair(String labelText, JComponent field) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(StyleConstants.DARK_GRAY);
        panel.setMaximumSize(new Dimension(350, 70));
        
        JLabel label = new JLabel(labelText);
        label.setFont(StyleConstants.NORMAL_FONT);
        label.setForeground(StyleConstants.TEXT_COLOR);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        field.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        panel.add(label);
        panel.add(Box.createVerticalStrut(5));
        panel.add(field);
        panel.add(Box.createVerticalStrut(10));
        
        return panel;
    }
    
    private void updateAddressLabel() {
        String userType = (String) userTypeCombo.getSelectedItem();
        if (userType.equals("Asociación")) {
            addressLabel.setText("Ubicación");
        } else {
            addressLabel.setText("Dirección");
        }
    }
    
    private void handleRegister() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        String email = emailField.getText();
        String name = nameField.getText();
        String address = addressField.getText();
        String userType = (String) userTypeCombo.getSelectedItem();
        
        if (username.isEmpty() || password.isEmpty() || email.isEmpty() || 
            name.isEmpty() || address.isEmpty()) {
            showError("Por favor complete todos los campos");
            return;
        }
        
        try {
            if (userType.equals("Donante")) {
                donorService.registerDonorUser(username, password, email, name, address);
                showSuccess("Donante registrado exitosamente");
            } else {
                associationService.registerAssociationUser(username, password, email, name, address);
                showSuccess("Asociación registrada exitosamente");
            }
            mainFrame.showPanel("login");
        } catch (SQLIntegrityConstraintViolationException e) {
            showError("El usuario o email ya existe");
        } catch (Exception e) {
            showError("Error al registrar: " + e.getMessage());
        }
    }
    
    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
    
    private void showSuccess(String message) {
        JOptionPane.showMessageDialog(this, message, "Éxito", JOptionPane.INFORMATION_MESSAGE);
    }
}
