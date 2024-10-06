package com.danielpietka.service;

import com.danielpietka.model.StudentModel;
import com.danielpietka.resource.StudentResource;

import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StudentService {
    private final StudentResource studentResource;
    private static final Logger logger = Logger.getLogger(StudentService.class.getName());

    public StudentService(StudentResource studentResource) {
        this.studentResource = studentResource;
    }

    public void addStudent(StudentModel student) throws StudentServiceException {
        try {
            studentResource.addStudent(student);
            logger.info("Student added successfully: " + student.getFirstName());
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Failed to add student: " + student.getFirstName(), e);
            throw new StudentServiceException("Failed to add student", e);
        }
    }

    public void updateStudent(StudentModel student) throws StudentServiceException {
        try {
            studentResource.updateStudent(student);
            logger.info("Student updated successfully: ID=" + student.getId() + ", Name=" + student.getFirstName());
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Failed to update student: ID=" + student.getId(), e);
            throw new StudentServiceException("Failed to update student", e);
        }
    }

    public void deleteStudent(int studentId) throws StudentServiceException {
        try {
            studentResource.deleteStudent(studentId);
            logger.info("Student deleted successfully: ID=" + studentId);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Failed to delete student: ID=" + studentId, e);
            throw new StudentServiceException("Failed to delete student", e);
        }
    }

    public List<StudentModel> listStudents(int limit, int offset) throws StudentServiceException {
        try {
            List<StudentModel> students = studentResource.getStudents(limit, offset);
            logger.info("Retrieved " + students.size() + " students from database.");
            return students;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Failed to list students", e);
            throw new StudentServiceException("Failed to list students", e);
        }
    }
}
