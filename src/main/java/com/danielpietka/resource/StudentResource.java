package com.danielpietka.resource;

import com.danielpietka.model.StudentModel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StudentResource {
    private final Connection connection;
    private static final Logger logger = Logger.getLogger(StudentResource.class.getName());

    public StudentResource(Connection connection) {
        this.connection = connection;
    }

    public List<StudentModel> getStudents(int limit, int offset) throws SQLException {
        List<StudentModel> students = new ArrayList<>();
        String query = "SELECT * FROM students LIMIT ? OFFSET ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, limit);
            stmt.setInt(2, offset);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    students.add(new StudentModel(
                            rs.getInt("id"),
                            rs.getString("first_name"),
                            rs.getString("last_name"),
                            rs.getString("email"),
                            rs.getInt("age"),
                            rs.getString("address"),
                            rs.getString("phone_number")
                    ));
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error fetching students: ", e);
            throw e;
        }
        return students;
    }

    public StudentModel getStudentById(int id) throws SQLException {
        String query = "SELECT * FROM students WHERE id = ?";
        StudentModel student = null;

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    student = new StudentModel(
                            rs.getInt("id"),
                            rs.getString("first_name"),
                            rs.getString("last_name"),
                            rs.getString("email"),
                            rs.getInt("age"),
                            rs.getString("address"),
                            rs.getString("phone_number")
                    );
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error fetching student by ID: ", e);
            throw e;
        }
        return student;
    }

    public void addStudent(StudentModel student) throws SQLException {
        String query = "INSERT INTO students (first_name, last_name, email, age, address, phone_number) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, student.getFirstName());
            stmt.setString(2, student.getLastName());
            stmt.setString(3, student.getEmail());
            stmt.setInt(4, student.getAge());
            stmt.setString(5, student.getAddress());
            stmt.setString(6, student.getPhoneNumber());
            stmt.executeUpdate();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error adding student: ", e);
            throw e;
        }
    }

    public void updateStudent(StudentModel student) throws SQLException {
        String query = "UPDATE students SET first_name = ?, last_name = ?, email = ?, age = ?, address = ?, " +
                "phone_number = ? WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, student.getFirstName());
            stmt.setString(2, student.getLastName());
            stmt.setString(3, student.getEmail());
            stmt.setInt(4, student.getAge());
            stmt.setString(5, student.getAddress());
            stmt.setString(6, student.getPhoneNumber());
            stmt.setInt(7, student.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error updating student: ", e);
            throw e;
        }
    }

    public void deleteStudent(int id) throws SQLException {
        String query = "DELETE FROM students WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error deleting student: ", e);
            throw e;
        }
    }
}
