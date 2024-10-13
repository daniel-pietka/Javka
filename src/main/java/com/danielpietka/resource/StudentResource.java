package com.danielpietka.resource;

import com.danielpietka.database.ConnectionManager;
import com.danielpietka.model.StudentModel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StudentResource {
    private static final Logger logger = Logger.getLogger(StudentResource.class.getName());
    private final ConnectionManager connectionManager;

    public StudentResource(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    public void addStudent(StudentModel student) throws SQLException {
        String query = "INSERT INTO students (first_name, last_name, email, birth_date, address, phone_number, gender, " +
                "created_at, updated_at, is_active) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, student.getFirstName());
            stmt.setString(2, student.getLastName());
            stmt.setString(3, student.getEmail());
            stmt.setDate(4, student.getBirthDate());
            stmt.setString(5, student.getAddress());
            stmt.setString(6, student.getPhoneNumber());
            stmt.setString(7, student.getGender());
            stmt.setTimestamp(8, Timestamp.valueOf(LocalDateTime.now()));
            stmt.setTimestamp(9, Timestamp.valueOf(LocalDateTime.now()));
            stmt.setBoolean(10, student.isActive());
            stmt.executeUpdate();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error adding student: ", e);
            throw e;
        }
    }

    public void updateStudent(StudentModel student) throws SQLException {
        String query = "UPDATE students SET first_name = ?, last_name = ?, email = ?, birth_date = ?, address = ?, " +
                "phone_number = ?, gender = ?, updated_at = ?, is_active = ? WHERE id = ?";
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, student.getFirstName());
            stmt.setString(2, student.getLastName());
            stmt.setString(3, student.getEmail());
            stmt.setDate(4, student.getBirthDate());
            stmt.setString(5, student.getAddress());
            stmt.setString(6, student.getPhoneNumber());
            stmt.setString(7, student.getGender());
            stmt.setTimestamp(8, Timestamp.valueOf(LocalDateTime.now()));
            stmt.setBoolean(9, student.isActive());
            stmt.setInt(10, student.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error updating student: ", e);
            throw e;
        }
    }

    public void deleteStudent(int id) throws SQLException {
        String query = "DELETE FROM students WHERE id = ?";
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error deleting student: ", e);
            throw e;
        }
    }

    public StudentModel getStudentById(int studentId) throws SQLException {
        String query = "SELECT * FROM students WHERE id = ?";
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, studentId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new StudentModel(
                            rs.getInt("id"),
                            rs.getString("first_name"),
                            rs.getString("last_name"),
                            rs.getString("email"),
                            rs.getDate("birth_date"),
                            rs.getString("address"),
                            rs.getString("phone_number"),
                            rs.getString("gender"),
                            rs.getTimestamp("created_at").toLocalDateTime(),
                            rs.getTimestamp("updated_at").toLocalDateTime(),
                            rs.getBoolean("is_active")
                    );
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error fetching student by ID: ", e);
            throw e;
        }
        return null;
    }

    public List<StudentModel> getStudents(int limit, int offset) throws SQLException {
        String query = "SELECT * FROM students LIMIT ? OFFSET ?";
        List<StudentModel> students = new ArrayList<>();
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, limit);
            stmt.setInt(2, offset);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    students.add(new StudentModel(
                            rs.getInt("id"),
                            rs.getString("first_name"),
                            rs.getString("last_name"),
                            rs.getString("email"),
                            rs.getDate("birth_date"),
                            rs.getString("address"),
                            rs.getString("phone_number"),
                            rs.getString("gender"),
                            rs.getTimestamp("created_at").toLocalDateTime(),
                            rs.getTimestamp("updated_at").toLocalDateTime(),
                            rs.getBoolean("is_active")
                    ));
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error fetching students: ", e);
            throw e;
        }
        return students;
    }
}
