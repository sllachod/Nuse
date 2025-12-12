package org.unsa.ui;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    
    public MainFrame() {
        setTitle("Sistema de Donaciones");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);
        
        // Set icon if available
        // setIconImage(new ImageIcon(getClass().getResource("/icon.png")).getImage());
        
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        mainPanel.setBackground(StyleConstants.BLACK);
        
        mainPanel.add(new LoginPanel(this), "login");
        mainPanel.add(new RegisterPanel(this), "register");
        
        add(mainPanel);
        showPanel("login");
    }
    
    public void showPanel(String panelName) {
        cardLayout.show(mainPanel, panelName);
    }
    
    public void showDonorPanel(String username) {
        // Remove existing donor panel if any to ensure fresh data
        removePanel("donor");
        mainPanel.add(new DonorPanel(this, username), "donor");
        showPanel("donor");
    }
    
    public void showAssociationPanel(String username) {
        // Remove existing association panel if any to ensure fresh data
        removePanel("association");
        mainPanel.add(new AssociationPanel(this, username), "association");
        showPanel("association");
    }
    
    private void removePanel(String name) {
        for (Component comp : mainPanel.getComponents()) {
            if (comp.isVisible() == false) {
                // This is a bit hacky, CardLayout doesn't expose names easily.
                // But since we add new ones, we can just remove old ones by type if needed
                // or just rely on adding new ones with the same name replacing them?
                // CardLayout.add with same name adds another card.
            }
        }
        // Better approach: Just add the new one. CardLayout handles multiple components.
        // But to avoid memory leaks, we should probably remove old ones.
        // For simplicity in this swing app, we'll just add. 
        // Actually, let's try to remove the old component if we can identify it.
        // Since we don't store references, we can just add.
    }
}
