package org.unsa;

import org.unsa.utils.DBConnection;
import javax.swing.*;
import java.sql.Connection;

public class Main {
    public static void main(String[] args) {
        // Test the database connection
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

        // Launch the GUI
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                // TODO: Implement MainFrame for GUI
            }
        });
    }
}