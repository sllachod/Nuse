package org.unsa.ui;

import org.unsa.models.Association;
import org.unsa.models.Donation;
import org.unsa.dao.AssociationDAO;
import org.unsa.services.AssociationService;
import org.unsa.services.DonationCollectionService;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class AssociationPanel extends JPanel {
    private MainFrame mainFrame;
    private String username;
    private Association currentAssociation;
    private AssociationService associationService;
    private DonationCollectionService collectionService;
    private JTable donationsTable;
    private DefaultTableModel tableModel;
    
    public AssociationPanel(MainFrame mainFrame, String username) {
        this.mainFrame = mainFrame;
        this.username = username;
        this.associationService = new AssociationService();
        this.collectionService = new DonationCollectionService();
        
        AssociationDAO associationDAO = new AssociationDAO();
        this.currentAssociation = associationDAO.getAssociationByUsername(username);
        
        setLayout(new BorderLayout());
        setBackground(StyleConstants.BLACK);
        
        add(createHeaderPanel(), BorderLayout.NORTH);
        add(createCenterPanel(), BorderLayout.CENTER);
        
        loadAvailableDonations();
    }
    
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(StyleConstants.DARK_GRAY);
        headerPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 3, 0, StyleConstants.PRIMARY_RED),
            BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));
        
        JLabel titleLabel = new JLabel("Panel de Asociación - " + currentAssociation.getName());
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
        
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBackground(StyleConstants.BLACK);
        
        JLabel infoLabel = new JLabel("Donaciones Disponibles");
        infoLabel.setFont(StyleConstants.HEADER_FONT);
        infoLabel.setForeground(StyleConstants.PRIMARY_RED);
        topPanel.add(infoLabel);
        
        String[] columnNames = {"ID", "Tipo", "Descripción", "Cantidad", "Donante"};
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
        
        JButton collectButton = new StyleConstants.RoundedButton("Recolectar Donación", StyleConstants.PRIMARY_RED, StyleConstants.HOVER_RED);
        collectButton.setPreferredSize(new Dimension(180, 35));
        collectButton.addActionListener(e -> showCollectDialog());
        
        JButton refreshButton = new StyleConstants.RoundedButton("Actualizar", StyleConstants.LIGHT_GRAY, StyleConstants.DARK_GRAY.brighter());
        refreshButton.setPreferredSize(new Dimension(180, 35));
        refreshButton.addActionListener(e -> loadAvailableDonations());
        
        buttonPanel.add(collectButton);
        buttonPanel.add(refreshButton);
        
        centerPanel.add(topPanel, BorderLayout.NORTH);
        centerPanel.add(scrollPane, BorderLayout.CENTER);
        centerPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        return centerPanel;
    }
    
    private void loadAvailableDonations() {
        tableModel.setRowCount(0);
        ArrayList<Donation> donations = associationService.getAvailableDonations();
        
        if (donations != null) {
            for (Donation donation : donations) {
                Object[] row = {
                    donation.getId(),
                    donation.getType(),
                    donation.getDescription(),
                    donation.getQuantity(),
                    donation.getDonor().getName()
                };
                tableModel.addRow(row);
            }
        }
    }
    
    private void showCollectDialog() {
        int selectedRow = donationsTable.getSelectedRow();
        if (selectedRow == -1) {
            showError("Por favor seleccione una donación");
            return;
        }
        
        int donationId = (Integer) tableModel.getValueAt(selectedRow, 0);
        String type = (String) tableModel.getValueAt(selectedRow, 1);
        int availableQuantity = (Integer) tableModel.getValueAt(selectedRow, 3);
        
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Recolectar Donación", true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(400, 350);
        dialog.setLocationRelativeTo(this);
        dialog.getContentPane().setBackground(StyleConstants.BLACK);
        
        StyleConstants.RoundedPanel formPanel = new StyleConstants.RoundedPanel(20, StyleConstants.DARK_GRAY);
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel infoLabel = new JLabel("Donación: " + type);
        infoLabel.setFont(StyleConstants.NORMAL_FONT);
        infoLabel.setForeground(StyleConstants.TEXT_COLOR);
        infoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel availLabel = new JLabel("Disponible: " + availableQuantity);
        availLabel.setFont(StyleConstants.NORMAL_FONT);
        availLabel.setForeground(StyleConstants.TEXT_COLOR);
        availLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JSpinner quantitySpinner = new JSpinner(new SpinnerNumberModel(1, 1, availableQuantity, 1));
        styleSpinner(quantitySpinner);
        
        formPanel.add(Box.createVerticalStrut(30));
        formPanel.add(infoLabel);
        formPanel.add(Box.createVerticalStrut(10));
        formPanel.add(availLabel);
        formPanel.add(Box.createVerticalStrut(20));
        formPanel.add(createFieldPanel("Cantidad a recolectar:", quantitySpinner));
        formPanel.add(Box.createVerticalStrut(20));
        
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setOpaque(false);
        
        JButton collectButton = new StyleConstants.RoundedButton("Recolectar", StyleConstants.PRIMARY_RED, StyleConstants.HOVER_RED);
        collectButton.setPreferredSize(new Dimension(120, 35));
        collectButton.addActionListener(e -> {
            int quantity = (Integer) quantitySpinner.getValue();
            
            boolean success = collectionService.collectDonation(
                currentAssociation.getId(), 
                donationId, 
                quantity
            );
            
            if (success) {
                loadAvailableDonations();
                dialog.dispose();
                showSuccess("Donación recolectada exitosamente");
            } else {
                showError("Error al recolectar la donación");
            }
        });
        
        JButton cancelButton = new StyleConstants.RoundedButton("Cancelar", StyleConstants.LIGHT_GRAY, StyleConstants.DARK_GRAY.brighter());
        cancelButton.setPreferredSize(new Dimension(120, 35));
        cancelButton.addActionListener(e -> dialog.dispose());
        
        buttonPanel.add(collectButton);
        buttonPanel.add(cancelButton);
        formPanel.add(buttonPanel);
        
        JPanel container = new JPanel(new BorderLayout());
        container.setBackground(StyleConstants.BLACK);
        container.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        container.add(formPanel, BorderLayout.CENTER);
        
        dialog.add(container, BorderLayout.CENTER);
        dialog.setVisible(true);
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
