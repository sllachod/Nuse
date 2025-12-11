package org.unsa;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        //TODO here test the database connection, missing DBConnection class
//        try (Connection conn = DBConnection.getConnection()) {
//            System.out.println("database connected successfully!");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        //TODO here launch the GUI, missing MainFrame class
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
            }
        });
    }
}