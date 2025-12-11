package org.unsa.services;

import org.unsa.dao.AssociationDAO;
import org.unsa.dao.DonationDAO;
import org.unsa.models.Association;
import org.unsa.models.Donation;
import org.unsa.models.User;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;

public class AssociationService {
    private org.unsa.services.UserService userService = new org.unsa.services.UserService();

    public Association registerAssociationUser(String username, String password, String email, String name, String location) throws SQLIntegrityConstraintViolationException {
        try {
            User user = userService.registerUser(username, password, email);
            if (user != null) {
                Association association = new Association(0, name, location, user);
                boolean associationCreated = AssociationDAO.addAssociation(association);
                if (associationCreated)
                    return association;
            }
        } catch (SQLIntegrityConstraintViolationException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<Donation> getAvailableDonations() {
        DonationDAO donationDAO = new dao.DonationDAO();
        return donationDAO.getAvailableDonations();
    }
}