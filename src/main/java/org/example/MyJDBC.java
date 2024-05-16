package org.example;


import java.sql.*;

public class MyJDBC {
    private static final String URL = "jdbc:mysql://127.0.0.1:3306/estate_db";
    private static final String USER = "root";
    private static final String PASSWORD = "Ik240406";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
