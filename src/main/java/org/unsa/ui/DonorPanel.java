package org.unsa.ui;

import org.unsa.models.Donation;
import org.unsa.models.Donor;
import org.unsa.services.DonationService;
import org.unsa.services.DonorService;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class DonorPanel extends JPanel {
    private MainFrame mainFrame;
    private String username;
    private Donor currentDonor;
    private DonorService donorService;
    private DonationService donationService;
    private JTable donationsTable;
    private DefaultTableModel tableModel;
    
    public DonorPanel(MainFrame mainFrame, String username) {
        this.mainFrame = mainFrame;
        this.username = username;
        this.donorService = new DonorService();
        this.donationService = new DonationService();
        this.currentDonor = donorService.getDonorByUsername(username);
        
        setLayout(new BorderLayout());
        setBackground(StyleConstants.BLACK);
        
        add(createHeaderPanel(), BorderLayout.NORTH);
        add(createCenterPanel(), BorderLayout.CENTER);
        
        loadDonations();
    }
    
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(StyleConstants.DARK_GRAY);
        headerPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 3, 0, StyleConstants.PRIMARY_RED),
            BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));
        
        JLabel titleLabel = new JLabel("Panel de Donante - " + currentDonor.getName());
        titleLabel.setFont(StyleConstants.HEADER_FONT);
        titleLabel.setForeground(StyleConstants.TEXT_COLOR);
        
        JButton logoutButton = new StyleConstants.RoundedButton("Cerrar Sesión", StyleConstants.PRIMARY_RED, StyleConstants.HOVER_RED);
        logoutButton.setPreferredSize(new Dimension(150, 35));
        logoutButton.addActionListener(e -> mainFrame.showPanel("login"));
        
        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(logoutButton, BorderLayout.EAST);
        
        return headerPanel;
    }
    
    private JPanel createCenterPanel() {
        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
        centerPanel.setBackground(StyleConstants.BLACK);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        topPanel.setBackground(StyleConstants.BLACK);
        
        JButton addButton = new StyleConstants.RoundedButton("Nueva Donación", StyleConstants.PRIMARY_RED, StyleConstants.HOVER_RED);
        addButton.setPreferredSize(new Dimension(160, 35));
        addButton.addActionListener(e -> showAddDonationDialog());
        topPanel.add(addButton);
        
        String[] columnNames = {"ID", "Tipo", "Descripción", "Cantidad", "Disponible"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        donationsTable = new JTable(tableModel);
        StyleConstants.configureTable(donationsTable);
        
        JScrollPane scrollPane = new JScrollPane(donationsTable);
        scrollPane.getViewport().setBackground(StyleConstants.BLACK);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBorder(null);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBackground(StyleConstants.BLACK);
        
        JButton editButton = new StyleConstants.RoundedButton("Editar", StyleConstants.PRIMARY_RED, StyleConstants.HOVER_RED);
        editButton.setPreferredSize(new Dimension(150, 35));
        editButton.addActionListener(e -> showEditDonationDialog());
        
        JButton deleteButton = new StyleConstants.RoundedButton("Eliminar", StyleConstants.DARK_RED, StyleConstants.PRIMARY_RED);
        deleteButton.setPreferredSize(new Dimension(150, 35));
        deleteButton.addActionListener(e -> deleteDonation());
        
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        
        centerPanel.add(topPanel, BorderLayout.NORTH);
        centerPanel.add(scrollPane, BorderLayout.CENTER);
        centerPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        return centerPanel;
    }
    
    private void loadDonations() {
        tableModel.setRowCount(0);
        ArrayList<Donation> donations = donationService.getDonationsForDonor(currentDonor.getId());
        
        if (donations != null) {
            for (Donation donation : donations) {
                Object[] row = {
                    donation.getId(),
                    donation.getType(),
                    donation.getDescription(),
                    donation.getQuantity(),
                    donation.isAvailable() ? "Sí" : "No"
                };
                tableModel.addRow(row);
            }
        }
    }
    
    private void showAddDonationDialog() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Nueva Donación", true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(450, 450);
        dialog.setLocationRelativeTo(this);
        dialog.getContentPane().setBackground(StyleConstants.BLACK);
        
        StyleConstants.RoundedPanel formPanel = new StyleConstants.RoundedPanel(20, StyleConstants.DARK_GRAY);
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        String[] donationTypes = {"Alimentos", "Ropa", "Medicinas", "Juguetes", "Dinero", "Muebles", "Otros"};
        JComboBox<String> typeCombo = StyleConstants.createStyledComboBox(donationTypes);
        
        JTextField descField = StyleConstants.createStyledTextField();
        JSpinner quantitySpinner = new JSpinner(new SpinnerNumberModel(1, 1, 1000, 1));
        styleSpinner(quantitySpinner);
        
        formPanel.add(Box.createVerticalStrut(10));
        formPanel.add(createFieldPanel("Tipo:", typeCombo));
        formPanel.add(createFieldPanel("Descripción:", descField));
        formPanel.add(createFieldPanel("Cantidad:", quantitySpinner));
        formPanel.add(Box.createVerticalStrut(20));
        
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setOpaque(false);
        
        JButton saveButton = new StyleConstants.RoundedButton("Guardar", StyleConstants.PRIMARY_RED, StyleConstants.HOVER_RED);
        saveButton.setPreferredSize(new Dimension(120, 35));
        saveButton.addActionListener(e -> {
            String type = (String) typeCombo.getSelectedItem();
            String desc = descField.getText();
            int quantity = (Integer) quantitySpinner.getValue();
            
            if (type == null || type.isEmpty()) {
                showError("El tipo es requerido");
                return;
            }
            
            Donation donation = donationService.createDonation(type, desc, quantity, currentDonor);
            if (donation != null) {
                loadDonations();
                dialog.dispose();
                showSuccess("Donación creada exitosamente");
            } else {
                showError("Error al crear la donación");
            }
        });
        
        JButton cancelButton = new StyleConstants.RoundedButton("Cancelar", StyleConstants.LIGHT_GRAY, StyleConstants.DARK_GRAY.brighter());
        cancelButton.setPreferredSize(new Dimension(120, 35));
        cancelButton.addActionListener(e -> dialog.dispose());
        
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        formPanel.add(buttonPanel);
        
        JPanel container = new JPanel(new BorderLayout());
        container.setBackground(StyleConstants.BLACK);
        container.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        container.add(formPanel, BorderLayout.CENTER);
        
        dialog.add(container, BorderLayout.CENTER);
        dialog.setVisible(true);
    }
    
    private void showEditDonationDialog() {
        int selectedRow = donationsTable.getSelectedRow();
        if (selectedRow == -1) {
            showError("Por favor seleccione una donación");
            return;
        }
        
        int donationId = (Integer) tableModel.getValueAt(selectedRow, 0);
        Donation donation = donationService.getDonationById(currentDonor, donationId);
        
        if (donation == null) {
            showError("No se pudo cargar la donación");
            return;
        }
        
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Editar Donación", true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(450, 450);
        dialog.setLocationRelativeTo(this);
        dialog.getContentPane().setBackground(StyleConstants.BLACK);
        
        StyleConstants.RoundedPanel formPanel = new StyleConstants.RoundedPanel(20, StyleConstants.DARK_GRAY);
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        String[] donationTypes = {"Alimentos", "Ropa", "Medicinas", "Juguetes", "Dinero", "Muebles", "Otros"};
        JComboBox<String> typeCombo = StyleConstants.createStyledComboBox(donationTypes);
        typeCombo.setSelectedItem(donation.getType());
        
        JTextField descField = StyleConstants.createStyledTextField();
        descField.setText(donation.getDescription());
        JSpinner quantitySpinner = new JSpinner(new SpinnerNumberModel(donation.getQuantity(), 1, 1000, 1));
        styleSpinner(quantitySpinner);
        
        formPanel.add(Box.createVerticalStrut(10));
        formPanel.add(createFieldPanel("Tipo:", typeCombo));
        formPanel.add(createFieldPanel("Descripción:", descField));
        formPanel.add(createFieldPanel("Cantidad:", quantitySpinner));
        formPanel.add(Box.createVerticalStrut(20));
        
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setOpaque(false);
        
        JButton saveButton = new StyleConstants.RoundedButton("Guardar", StyleConstants.PRIMARY_RED, StyleConstants.HOVER_RED);
        saveButton.setPreferredSize(new Dimension(120, 35));
        saveButton.addActionListener(e -> {
            String type = (String) typeCombo.getSelectedItem();
            String desc = descField.getText();
            int quantity = (Integer) quantitySpinner.getValue();
            
            if (type == null || type.isEmpty()) {
                showError("El tipo es requerido");
                return;
            }
            
            Donation updated = donationService.editDonation(donationId, type, desc, quantity, currentDonor);
            if (updated != null) {
                loadDonations();
                dialog.dispose();
                showSuccess("Donación actualizada exitosamente");
            } else {
                showError("Error al actualizar la donación");
            }
        });
        
        JButton cancelButton = new StyleConstants.RoundedButton("Cancelar", StyleConstants.LIGHT_GRAY, StyleConstants.DARK_GRAY.brighter());
        cancelButton.setPreferredSize(new Dimension(120, 35));
        cancelButton.addActionListener(e -> dialog.dispose());
        
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        formPanel.add(buttonPanel);
        
        JPanel container = new JPanel(new BorderLayout());
        container.setBackground(StyleConstants.BLACK);
        container.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        container.add(formPanel, BorderLayout.CENTER);
        
        dialog.add(container, BorderLayout.CENTER);
        dialog.setVisible(true);
    }
    
    private void deleteDonation() {
        int selectedRow = donationsTable.getSelectedRow();
        if (selectedRow == -1) {
            showError("Por favor seleccione una donación");
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, 
            "¿Está seguro de eliminar esta donación?", 
            "Confirmar", 
            JOptionPane.YES_NO_OPTION);
            
        if (confirm == JOptionPane.YES_OPTION) {
            int donationId = (Integer) tableModel.getValueAt(selectedRow, 0);
            boolean deleted = donationService.deleteDonationById(donationId);
            
            if (deleted) {
                loadDonations();
                showSuccess("Donación eliminada exitosamente");
            } else {
                showError("Error al eliminar la donación");
            }
        }
    }
    
    private void styleSpinner(JSpinner spinner) {
        spinner.setFont(StyleConstants.NORMAL_FONT);
        spinner.setMaximumSize(new Dimension(300, 35));
        JComponent editor = spinner.getEditor();
        if (editor instanceof JSpinner.DefaultEditor) {
            JSpinner.DefaultEditor spinnerEditor = (JSpinner.DefaultEditor) editor;
            spinnerEditor.getTextField().setBackground(StyleConstants.LIGHT_GRAY);
            spinnerEditor.getTextField().setForeground(StyleConstants.TEXT_COLOR);
            spinnerEditor.getTextField().setCaretColor(StyleConstants.TEXT_COLOR);
            spinnerEditor.getTextField().setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        }
        spinner.setBorder(BorderFactory.createLineBorder(StyleConstants.LIGHT_GRAY));
    }
    
    private JPanel createFieldPanel(String labelText, JComponent field) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(StyleConstants.DARK_GRAY);
        panel.setOpaque(false);
        panel.setMaximumSize(new Dimension(400, 65));
        
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
    
    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
    
    private void showSuccess(String message) {
        JOptionPane.showMessageDialog(this, message, "Éxito", JOptionPane.INFORMATION_MESSAGE);
    }
}
