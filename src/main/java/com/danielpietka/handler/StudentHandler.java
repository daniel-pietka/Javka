package com.danielpietka.handler;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import com.google.gson.Gson;
import com.danielpietka.model.StudentModel;
import com.danielpietka.resource.StudentResource;
import com.danielpietka.util.RequestHelper;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StudentHandler implements HttpHandler {
    private final StudentResource studentResource;
    private static final Gson gson = new Gson();
    private static final Logger logger = Logger.getLogger(StudentHandler.class.getName());

    public StudentHandler(Connection connection) {
        this.studentResource = new StudentResource(connection);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String response = "";
        int statusCode = 200;

        try {
            String requestMethod = RequestHelper.getRequestMethod(exchange);
            String requestUri = RequestHelper.getRequestURI(exchange);
            InputStreamReader reader;

            switch (requestMethod) {
                case "GET":
                    if (requestUri.matches("/api/students/\\d+")) {
                        int studentId = RequestHelper.extractIdFromUri(requestUri, "/api/students/");
                        StudentModel student = studentResource.getStudentById(studentId);
                        if (student != null) {
                            response = gson.toJson(student);
                        } else {
                            response = "Student not found";
                            statusCode = 404;
                        }
                    } else {
                        Map<String, String> queryParams = RequestHelper.getQueryParams(exchange);
                        int limit = queryParams.containsKey("limit") ? Integer.parseInt(queryParams.get("limit")) : 10;
                        int offset = queryParams.containsKey("offset") ? Integer.parseInt(queryParams.get("offset")) : 0;

                        List<StudentModel> students = studentResource.getStudents(limit, offset);
                        response = gson.toJson(students);
                    }
                    break;

                case "POST":
                    reader = new InputStreamReader(exchange.getRequestBody());
                    StudentModel newStudent = gson.fromJson(reader, StudentModel.class);
                    if (isValidStudent(newStudent)) {
                        studentResource.addStudent(newStudent);
                        response = "Student added successfully";
                        statusCode = 201;
                    } else {
                        response = "Invalid student data";
                        statusCode = 400;
                    }
                    break;

                case "PUT":
                    reader = new InputStreamReader(exchange.getRequestBody());
                    StudentModel updatedStudent = gson.fromJson(reader, StudentModel.class);
                    if (isValidStudent(updatedStudent)) {
                        studentResource.updateStudent(updatedStudent);
                        response = "Student updated successfully";
                    } else {
                        response = "Invalid student data";
                        statusCode = 400;
                    }
                    break;

                case "DELETE":
                    int studentId = RequestHelper.extractIdFromUri(requestUri, "/api/students/");
                    studentResource.deleteStudent(studentId);
                    response = "Student deleted successfully";
                    statusCode = 204;
                    break;

                default:
                    response = "HTTP method not supported";
                    statusCode = 405;
                    break;
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Database error: ", e);
            response = "Database error";
            statusCode = 500;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Server error: ", e);
            response = "Server error";
            statusCode = 500;
        }

        exchange.sendResponseHeaders(statusCode, response.getBytes().length);
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    private boolean isValidStudent(StudentModel student) {
        return student.getFirstName() != null && !student.getFirstName().isEmpty() &&
                student.getLastName() != null && !student.getLastName().isEmpty() &&
                student.getEmail() != null && !student.getEmail().isEmpty();
    }
}
