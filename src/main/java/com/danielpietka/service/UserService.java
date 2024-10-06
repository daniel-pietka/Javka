package com.danielpietka.service;

import com.danielpietka.model.UserModel;
import com.danielpietka.resource.UserResource;
import com.danielpietka.util.PasswordEncoder;

import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserService {
    private final UserResource userResource;
    private static final Logger logger = Logger.getLogger(UserService.class.getName());

    public UserService(UserResource userResource) {
        this.userResource = userResource;
    }

    public void addUser(UserModel user) throws UserServiceException {
        try {
            userResource.addUser(user);
            logger.info("User added successfully: " + user.getUsername());
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Failed to add user: " + user.getUsername(), e);
            throw new UserServiceException("Failed to add user", e);
        }
    }

    public void updateUser(UserModel user) throws UserServiceException {
        try {
            userResource.updateUser(user);
            logger.info("User updated successfully: ID=" + user.getId() + ", Username=" + user.getUsername());
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Failed to update user: ID=" + user.getId(), e);
            throw new UserServiceException("Failed to update user", e);
        }
    }

    public void deleteUser(int userId) throws UserServiceException {
        try {
            userResource.deleteUser(userId);
            logger.info("User deleted successfully: ID=" + userId);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Failed to delete user: ID=" + userId, e);
            throw new UserServiceException("Failed to delete user", e);
        }
    }

    public List<UserModel> listUsers(int limit, int offset) throws UserServiceException {
        try {
            List<UserModel> users = userResource.getUsers(limit, offset);
            logger.info("Retrieved " + users.size() + " users from database.");
            return users;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Failed to list users", e);
            throw new UserServiceException("Failed to list users", e);
        }
    }

    public UserModel authenticateUser(String username, String password) throws UserServiceException {
        try {
            UserModel user = userResource.getUserByUsernameAndPassword(username, PasswordEncoder.encodePassword(password));
            if (user != null) {
                logger.info("User authenticated successfully: " + username);
            } else {
                logger.warning("Authentication failed for user: " + username);
            }
            return user;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Failed to authenticate user: " + username, e);
            throw new UserServiceException("Failed to authenticate user", e);
        }
    }
}
