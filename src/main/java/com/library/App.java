package com.library;

import java.sql.Connection;
import java.sql.DriverManager;

public class App {
    public static void main(String[] args) {
        String url = "jdbc:postgresql://localhost:5432/library";
        String user = "lms_user";
        String password = "Aleksander5628*";

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            System.out.println("Connected to the PostgreSQL database!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
