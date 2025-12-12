package org.unsa;

import org.unsa.utils.DBConnection;
import org.unsa.ui.MainFrame;
import javax.swing.*;
import java.sql.Connection;

public class Main {
    public static void main(String[] args) {
        try (Connection conn = DBConnection.getConnection()) {
            if (conn != null) {
                System.out.println("Database connected successfully!");
            } else {
                System.err.println("Failed to connect to database.");
            }
        } catch (Exception e) {
            System.err.println("Database connection error:");
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                MainFrame mainFrame = new MainFrame();
                mainFrame.setVisible(true);
            }
        });
    }
}