package org.unsa.services;

import org.unsa.dao.AssociationDAO;
import org.unsa.dao.DonorDAO;
import org.unsa.dao.UserDAO;
import org.unsa.models.Association;
import org.unsa.models.Donor;
import org.unsa.models.User;

import java.sql.SQLIntegrityConstraintViolationException;

public class UserService {
    private UserDAO userDAO = new UserDAO();
    private DonorDAO donorDAO = new DonorDAO();
    private AssociationDAO associationDAO = new AssociationDAO();

    public User registerUser(String username, String password, String email) throws SQLIntegrityConstraintViolationException {
        try {
            User user = new User(0, username, password, email);
            return userDAO.addUser(user);
        } catch (SQLIntegrityConstraintViolationException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String loginUser(String username, String password) {
        Donor donor = donorDAO.getDonorByUsername(username);
        if (donor != null && donor.getUser().getPassword().equals(password)) {
            return "donor";
        }

        Association association = associationDAO.getAssociationByUsername(username);
        if (association != null && association.getUser().getPassword().equals(password)) {
            return "association";
        }

        return "invalid";
    }
}
