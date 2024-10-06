package com.danielpietka.handler;

import com.danielpietka.model.UserModel;
import com.danielpietka.service.UserService;
import com.danielpietka.service.UserServiceException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserCommandHandler {
    private static final Logger logger = Logger.getLogger(UserCommandHandler.class.getName());
    private final UserService userService;

    public UserCommandHandler(UserService userService) {
        this.userService = userService;
    }

    public void handleCommand(String command, String[] args) {
        try {
            switch (command) {
                case "user:add":
                    handleAddUser(args);
                    break;
                case "user:update":
                    handleUpdateUser(args);
                    break;
                case "user:delete":
                    handleDeleteUser(args);
                    break;
                case "user:list":
                    handleListUsers(args);
                    break;
                default:
                    logger.warning("Unknown command: " + command);
                    System.out.println("Unknown command.");
                    break;
            }
        } catch (UserServiceException e) {
            logger.log(Level.SEVERE, "Error executing command: " + command, e);
            System.err.println("Error executing command: " + e.getMessage());
        } catch (NumberFormatException e) {
            logger.log(Level.SEVERE, "Invalid number format: ", e);
            System.err.println("Invalid number format: " + e.getMessage());
        }
    }

    private void handleAddUser(String[] args) throws UserServiceException {
        if (args.length != 2) {
            System.out.println("Usage: user:add <username> <password>");
            return;
        }
        String username = args[0];
        String password = args[1];
        userService.addUser(new UserModel(0, username, password));
        System.out.println("User added successfully.");
    }

    private void handleUpdateUser(String[] args) throws UserServiceException {
        if (args.length != 3) {
            System.out.println("Usage: user:update <id> <username> <password>");
            return;
        }
        try {
            int userId = Integer.parseInt(args[0]);
            String username = args[1];
            String password = args[2];
            userService.updateUser(new UserModel(userId, username, password));
            System.out.println("User updated successfully.");
        } catch (NumberFormatException e) {
            System.err.println("Invalid ID format. Please provide a valid integer.");
            logger.log(Level.SEVERE, "Invalid ID format: ", e);
        }
    }

    private void handleDeleteUser(String[] args) throws UserServiceException {
        if (args.length != 1) {
            System.out.println("Usage: user:delete <id>");
            return;
        }
        try {
            int userId = Integer.parseInt(args[0]);
            userService.deleteUser(userId);
            System.out.println("User deleted successfully.");
        } catch (NumberFormatException e) {
            System.err.println("Invalid ID format. Please provide a valid integer.");
            logger.log(Level.SEVERE, "Invalid ID format: ", e);
        }
    }

    private void handleListUsers(String[] args) throws UserServiceException {
        if (args.length != 2) {
            System.out.println("Usage: user:list <limit> <offset>");
            return;
        }
        try {
            int limit = Integer.parseInt(args[0]);
            int offset = Integer.parseInt(args[1]);
            userService.listUsers(limit, offset).forEach(
                    user -> System.out.println("ID: " + user.getId() + ", Username: " + user.getUsername())
            );
        } catch (NumberFormatException e) {
            System.err.println("Invalid limit or offset format. Please provide valid integers.");
            logger.log(Level.SEVERE, "Invalid number format: ", e);
        }
    }
}
