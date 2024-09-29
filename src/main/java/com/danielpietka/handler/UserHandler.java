package com.danielpietka.handler;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import com.google.gson.Gson;
import com.danielpietka.model.UserModel;
import com.danielpietka.resource.UserResource;
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

public class UserHandler implements HttpHandler {
    private final UserResource userResource;
    private static final Gson gson = new Gson();
    private static final Logger logger = Logger.getLogger(UserHandler.class.getName());

    public UserHandler(Connection connection) {
        this.userResource = new UserResource(connection);
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
                    if (requestUri.matches("/api/users/\\d+")) {
                        int userId = RequestHelper.extractIdFromUri(requestUri, "/api/users/");
                        UserModel user = userResource.getUserById(userId);
                        if (user != null) {
                            response = gson.toJson(user);
                        } else {
                            response = "User not found";
                            statusCode = 404;
                        }
                    } else {
                        Map<String, String> queryParams = RequestHelper.getQueryParams(exchange);
                        int limit = queryParams.containsKey("limit") ? Integer.parseInt(queryParams.get("limit")) : 10;
                        int offset = queryParams.containsKey("offset") ? Integer.parseInt(queryParams.get("offset")) : 0;

                        List<UserModel> users = userResource.getUsers(limit, offset);
                        response = gson.toJson(users);
                    }
                    break;

                case "POST":
                    reader = new InputStreamReader(exchange.getRequestBody());
                    UserModel newUser = gson.fromJson(reader, UserModel.class);
                    if (isValidUser(newUser)) {
                        userResource.addUser(newUser);
                        response = "User added successfully";
                        statusCode = 201;
                    } else {
                        response = "Invalid user data";
                        statusCode = 400;
                    }
                    break;

                case "PUT":
                    reader = new InputStreamReader(exchange.getRequestBody());
                    UserModel updatedUser = gson.fromJson(reader, UserModel.class);
                    if (isValidUser(updatedUser)) {
                        userResource.updateUser(updatedUser);
                        response = "User updated successfully";
                    } else {
                        response = "Invalid user data";
                        statusCode = 400;
                    }
                    break;

                case "DELETE":
                    int userId = RequestHelper.extractIdFromUri(requestUri, "/api/users/");
                    userResource.deleteUser(userId);
                    response = "User deleted successfully";
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

    private boolean isValidUser(UserModel user) {
        return user.getUsername() != null && !user.getUsername().isEmpty() &&
                user.getEmail() != null && !user.getEmail().isEmpty();
    }
}
