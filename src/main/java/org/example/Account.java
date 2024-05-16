package org.example;

import org.example.Marketing;
import java.sql.*;
import java.util.Scanner;

public class Account {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("1. Login");
            System.out.println("2. Exit");
            System.out.println("Choose an option:");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    login(scanner);
                    break;
                case 2:
                    System.out.println("Exiting the system.");
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option selected. Please choose again.");
                    break;
            }
        }
        scanner.close();
    }

    private static void login(Scanner scanner) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = MyJDBC.getConnection();
            System.out.println("Enter username:");
            String username = scanner.nextLine();
            System.out.println("Enter password:");
            String password = scanner.nextLine();

            String query = "SELECT username, password, role FROM users WHERE username = ? AND password = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);

            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String role = resultSet.getString("role");
                System.out.println("Login successful!");
                switch (role) {
                    case "admin":
                        Manager manager = new Manager(username, password);
                        manager.displayOptions();
                        break;
                    case "master":
                        GenDir genDir = new GenDir(username, password);
                        genDir.displayOptions();
                        break;
                    case "buyer":
                        Marketing buyer = new Marketing(username, password);
                        buyer.displayOptions();
                        break;
                    default:
                        System.out.println("No role-specific options available.");
                        break;
                }
            } else {
                System.out.println("Invalid username or password. Try again.");
            }
        } catch (SQLException e) {
            System.out.println("Error connecting to the database: " + e.getMessage());
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) connection.close();
            } catch (SQLException ex) {
                System.out.println("Error closing resources: " + ex.getMessage());
            }
        }
    }
}
