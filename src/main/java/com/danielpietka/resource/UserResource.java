package com.danielpietka.resource;

import com.danielpietka.model.UserModel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserResource {
    private final Connection connection;
    private static final Logger logger = Logger.getLogger(UserResource.class.getName());

    public UserResource(Connection connection) {
        this.connection = connection;
    }

    public List<UserModel> getUsers(int limit, int offset) throws SQLException {
        List<UserModel> users = new ArrayList<>();
        String query = "SELECT * FROM users LIMIT ? OFFSET ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, limit);
            stmt.setInt(2, offset);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    users.add(new UserModel(
                            rs.getInt("id"),
                            rs.getString("username"),
                            rs.getString("email")
                    ));
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error fetching users: ", e);
            throw e;
        }
        return users;
    }

    public UserModel getUserById(int id) throws SQLException {
        String query = "SELECT * FROM users WHERE id = ?";
        UserModel user = null;

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    user = new UserModel(
                            rs.getInt("id"),
                            rs.getString("username"),
                            rs.getString("email")
                    );
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error fetching user by ID: ", e);
            throw e;
        }
        return user;
    }

    public void addUser(UserModel user) throws SQLException {
        String query = "INSERT INTO users (username, email) VALUES (?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getEmail());
            stmt.executeUpdate();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error adding user: ", e);
            throw e;
        }
    }

    public void updateUser(UserModel user) throws SQLException {
        String query = "UPDATE users SET username = ?, email = ? WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getEmail());
            stmt.setInt(3, user.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error updating user: ", e);
            throw e;
        }
    }

    public void deleteUser(int id) throws SQLException {
        String query = "DELETE FROM users WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error deleting user: ", e);
            throw e;
        }
    }
}
