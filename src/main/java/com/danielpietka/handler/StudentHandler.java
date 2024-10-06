package com.danielpietka.handler;

import com.danielpietka.database.ConnectionManager;
import com.danielpietka.model.StudentModel;
import com.danielpietka.resource.StudentResource;
import com.danielpietka.util.ErrorResponse;
import com.danielpietka.util.RequestHelper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StudentHandler implements HttpHandler {
    private static final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
    private static final Logger logger = Logger.getLogger(StudentHandler.class.getName());
    private final StudentResource studentResource;

    public StudentHandler() {
        this.studentResource = new StudentResource();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String response = "";
        int statusCode = 200;

        try {
            String requestMethod = RequestHelper.getRequestMethod(exchange);
            String requestUri = RequestHelper.getRequestURI(exchange);

            switch (requestMethod) {
                case "GET":
                    response = handleGetRequest(exchange, requestUri);
                    break;
                case "POST":
                    response = handlePostRequest(exchange);
                    statusCode = 201; // Created
                    break;
                case "PUT":
                    response = handlePutRequest(exchange);
                    break;
                case "DELETE":
                    response = handleDeleteRequest(requestUri);
                    statusCode = 204; // No Content
                    break;
                default:
                    response = gson.toJson(new ErrorResponse("Method Not Allowed", "HTTP method not supported"));
                    statusCode = 405;
                    break;
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Database error: ", e);
            response = gson.toJson(new ErrorResponse("Internal Server Error", "Database error"));
            statusCode = 500;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Server error: ", e);
            response = gson.toJson(new ErrorResponse("Internal Server Error", "Server error"));
            statusCode = 500;
        }

        sendResponse(exchange, response, statusCode);
    }

    private String handleGetRequest(HttpExchange exchange, String requestUri) throws SQLException, UnsupportedEncodingException {
        if (requestUri.matches("/api/students/\\d+")) {
            int studentId = RequestHelper.extractIdFromUri(requestUri, "/api/students/");
            StudentModel student = studentResource.getStudentById(studentId);
            if (student != null) {
                return gson.toJson(student);
            } else {
                return gson.toJson(new ErrorResponse("Not Found", "Student not found"));
            }
        } else {
            Map<String, String> queryParams = RequestHelper.getQueryParams(exchange);
            int limit = queryParams.containsKey("limit") ? Integer.parseInt(queryParams.get("limit")) : 10;
            int offset = queryParams.containsKey("offset") ? Integer.parseInt(queryParams.get("offset")) : 0;
            List<StudentModel> students = studentResource.getStudents(limit, offset);
            return gson.toJson(students);
        }
    }

    private String handlePostRequest(HttpExchange exchange) throws IOException, SQLException {
        InputStreamReader reader = new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8);
        StudentModel newStudent = gson.fromJson(reader, StudentModel.class);
        if (isValidStudent(newStudent)) {
            newStudent.setCreatedAt(LocalDateTime.now());
            newStudent.setUpdatedAt(LocalDateTime.now());
            newStudent.setActive(true);
            studentResource.addStudent(newStudent);
            return "Student added successfully";
        } else {
            return gson.toJson(new ErrorResponse("Bad Request", "Invalid student data"));
        }
    }

    private String handlePutRequest(HttpExchange exchange) throws IOException, SQLException {
        InputStreamReader reader = new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8);
        StudentModel updatedStudent = gson.fromJson(reader, StudentModel.class);
        if (isValidStudent(updatedStudent)) {
            updatedStudent.setUpdatedAt(LocalDateTime.now());
            studentResource.updateStudent(updatedStudent);
            return "Student updated successfully";
        } else {
            return gson.toJson(new ErrorResponse("Bad Request", "Invalid student data"));
        }
    }

    private String handleDeleteRequest(String requestUri) throws SQLException {
        int studentId = RequestHelper.extractIdFromUri(requestUri, "/api/students/");
        studentResource.deleteStudent(studentId);
        return "Student deleted successfully";
    }

    private void sendResponse(HttpExchange exchange, String response, int statusCode) throws IOException {
        exchange.sendResponseHeaders(statusCode, response.getBytes().length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response.getBytes());
        }
    }

    private boolean isValidStudent(StudentModel student) {
        return student.getFirstName() != null && !student.getFirstName().isEmpty() &&
                student.getLastName() != null && !student.getLastName().isEmpty() &&
                student.getEmail() != null && !student.getEmail().isEmpty();
    }
}
