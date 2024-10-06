package com.danielpietka.handler;

import com.danielpietka.model.UserModel;
import com.danielpietka.service.UserService;
import com.danielpietka.service.UserServiceException;
import com.danielpietka.util.PasswordEncoder;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserHandler {
    private static final Logger logger = Logger.getLogger(UserHandler.class.getName());
    private final UserService userService;
    private final String[] args;

    public UserHandler(UserService userService, String[] args) {
        this.userService = userService;
        this.args = args;
    }

    public void handleCommand() {
        if (args.length < 1) {
            printUsage();
            return;
        }

        String command = args[0].toLowerCase();

        try {
            switch (command) {
                case "user:add":
                    handleAddUser();
                    break;
                case "user:update":
                    handleUpdateUser();
                    break;
                case "user:delete":
                    handleDeleteUser();
                    break;
                case "user:list":
                    handleListUsers();
                    break;
                default:
                    logger.warning("Unknown command: " + command);
                    System.out.println("Unknown command.");
            }
        } catch (UserServiceException e) {
            logger.log(Level.SEVERE, "User service error: " + e.getMessage(), e);
            System.err.println("User service error: " + e.getMessage());
        }
    }

    private void handleAddUser() throws UserServiceException {
        if (args.length != 3) {
            System.out.println("Usage: user:add <username> <password>");
            return;
        }

        String username = args[1];
        String password = args[2];

        if (isInvalid(username, password)) {
            System.out.println("Username and password cannot be empty.");
            return;
        }

        String encodedPassword = PasswordEncoder.encodePassword(password);
        UserModel newUser = new UserModel(0, username, encodedPassword);
        userService.addUser(newUser);
        logger.info("User added successfully: " + username);
        System.out.println("User added successfully.");
    }

    private void handleUpdateUser() throws UserServiceException {
        if (args.length != 4) {
            System.out.println("Usage: user:update <id> <username> <password>");
            return;
        }

        try {
            int updateId = Integer.parseInt(args[1]);
            String newUsername = args[2];
            String newPassword = args[3];

            if (isInvalid(newUsername, newPassword)) {
                System.out.println("Username and password cannot be empty.");
                return;
            }

            String encodedNewPassword = PasswordEncoder.encodePassword(newPassword);
            UserModel updatedUser = new UserModel(updateId, newUsername, encodedNewPassword);
            userService.updateUser(updatedUser);
            logger.info("User updated successfully: ID=" + updateId + ", Username=" + newUsername);
            System.out.println("User updated successfully.");
        } catch (NumberFormatException e) {
            logger.log(Level.WARNING, "Invalid ID format.", e);
            System.out.println("Invalid ID format. Please provide a valid number.");
        }
    }

    private void handleDeleteUser() throws UserServiceException {
        if (args.length != 2) {
            System.out.println("Usage: user:delete <id>");
            return;
        }

        try {
            int deleteId = Integer.parseInt(args[1]);
            userService.deleteUser(deleteId);
            logger.info("User deleted successfully: ID=" + deleteId);
            System.out.println("User deleted successfully.");
        } catch (NumberFormatException e) {
            logger.log(Level.WARNING, "Invalid ID format.", e);
            System.out.println("Invalid ID format. Please provide a valid number.");
        }
    }

    private void handleListUsers() throws UserServiceException {
        if (args.length != 3) {
            System.out.println("Usage: user:list <limit> <offset>");
            return;
        }

        try {
            int limit = Integer.parseInt(args[1]);
            int offset = Integer.parseInt(args[2]);
            List<UserModel> users = userService.listUsers(limit, offset);

            if (users.isEmpty()) {
                System.out.println("No users found.");
            } else {
                System.out.println("User List:");
                for (UserModel user : users) {
                    System.out.println("ID: " + user.getId() + ", Username: " + user.getUsername());
                }
            }
            logger.info("User list retrieved successfully.");
        } catch (NumberFormatException e) {
            logger.log(Level.WARNING, "Invalid number format for limit or offset.", e);
            System.out.println("Invalid number format for limit or offset. Please provide valid numbers.");
        }
    }

    private boolean isInvalid(String username, String password) {
        return username == null || username.isEmpty() || password == null || password.isEmpty();
    }

    private void printUsage() {
        System.out.println("Usage:");
        System.out.println("  user:add <username> <password>");
        System.out.println("  user:update <id> <username> <password>");
        System.out.println("  user:delete <id>");
        System.out.println("  user:list <limit> <offset>");
    }
}
