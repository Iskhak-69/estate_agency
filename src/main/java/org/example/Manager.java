package org.example;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Manager extends Person {

    Scanner scanner = new Scanner(System.in);

    public Manager(String username, String password) {
        super(username, password);
    }


    public void deleteCategoryProduct(int categoryId) {
        String sql = "DELETE FROM Categories WHERE id = ?";
        try (Connection conn = MyJDBC.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, categoryId);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Category deleted successfully.\n\n");
            } else {
                System.out.println("Failed to delete category.\n\n");
            }
        } catch (SQLException e) {
            System.out.println("Error while deleting category: " + e.getMessage());
        }
    }
    public void registerMaster(String username , String password){

        String sql = "INSERT INTO Users (username, password, role) VALUES (?, ?, 'master')";
        try (Connection conn = MyJDBC.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Master added successfully.\n\n");
            } else {
                System.out.println("Failed to add master.\n\n");
            }
        } catch (SQLException e) {
            System.out.println("Error while adding master: " + e.getMessage());
        }
    }

    public void displayOptions() {
        int choice;
        do {
            System.out.println("1. Register master\n2. Add product category\n3. Delete product category\n4. Exit");
            choice = getChoice(); // Get user choice and handle it in getChoice
        } while (choice != 4); // Loop until '4' is chosen
    }

    public int getChoice() {
        int choice = 0;
        do {
            System.out.println("Приветствую дорогой, Менеджер!");
            System.out.println("Пожалуйста наберите номер меню для работы с программой, если закончили, то наберите 7:");
            System.out.println("1. Показать список сотрудников");
            System.out.println("2. Показать список дел");
            System.out.println("3. Показать список указаний к сотрудникам");
            System.out.println("4. Показать список всех зон покрытия");
            System.out.println("5. Показать сумму по недвижимостям");
            System.out.println("6. Посчитать % по категориям недвижимости");
            System.out.println("7. Выход");

            choice = scanner.nextInt();
            scanner.nextLine(); // Это нужно, чтобы очистить буфер после чтения числа

            switch (choice) {
                case 1:
                    // Показать список сотрудников
                    break;
                case 2:
                    // Показать список дел
                    break;
                case 3:
                    // Показать список указаний к сотрудникам
                    break;
                case 4:
                    // Показать список всех зон покрытия
                    break;
                case 5:
                    // Показать сумму по недвижимостям
                    break;
                case 6:
                    // Посчитать % по категориям недвижимости
                    break;
                case 7:
                    System.out.println("Программа завершена, мы будем рады вашему возвращению!");
                    break;
                default:
                    System.out.println("Неверный ввод, пожалуйста, попробуйте еще раз.");
                    break;
            }
        } while (choice != 7);
        return choice;
    }
    public void showEmployees() {
        String url = "jdbc:mysql://localhost:3306/company_db?useSSL=false";
        String user = "yourusername"; // Замените на ваше имя пользователя
        String password = "yourpassword"; // Замените на ваш пароль

        String query = "SELECT id, first_name, last_name, position, department FROM employees";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            System.out.println("Список сотрудников:");
            while (rs.next()) {
                System.out.println(rs.getInt("id") + ": " + rs.getString("first_name") + " " +
                        rs.getString("last_name") + " - " +
                        rs.getString("position") + " [" +
                        rs.getString("department") + "]");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Ошибка при подключении к базе данных: " + e.getMessage());
        }
    }

    public void showAndAddTasks() {
        String url = "jdbc:mysql://localhost:3306/company_db?useSSL=false";
        String user = "yourusername"; // Замените на ваше имя пользователя
        String password = "yourpassword"; // Замените на ваш пароль
        String query = "SELECT id, task_name, description, due_date FROM tasks";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            System.out.println("Список всех дел:");
            while (rs.next()) {
                System.out.println(rs.getInt("id") + ": " + rs.getString("task_name") + " - " + rs.getString("description") + " (Due: " + rs.getDate("due_date") + ")");
            }

            // Добавление задачи в файл
            Scanner scanner = new Scanner(System.in);
            System.out.println("Напишите название дела, чтобы добавить его в файл:");
            String taskName = scanner.nextLine();
            addTaskToFile(taskName);

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Ошибка при подключении к базе данных: " + e.getMessage());
        }
    }

    private  void addTaskToFile(String taskName) {
        try (FileWriter writer = new FileWriter("tasks.txt", true)) {
            writer.append(taskName).append("\n");
            System.out.println("Задача добавлена в файл: " + taskName);
        } catch (IOException e) {
            System.out.println("Ошибка при записи в файл: " + e.getMessage());
        }
    }



    public void showInstructions() {
        String url = "jdbc:mysql://localhost:3306/company_db?useSSL=false";
        String user = "yourusername"; // Замените на ваше имя пользователя
        String password = "yourpassword"; // Замените на ваш пароль
        String query = "SELECT i.id, e.first_name, e.last_name, i.instruction, i.date_assigned FROM instructions i JOIN employees e ON i.employee_id = e.id";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            System.out.println("Список всех указаний к сотрудникам:");
            while (rs.next()) {
                System.out.println(rs.getInt("id") + ": " + rs.getString("first_name") + " " +
                        rs.getString("last_name") + " - " +
                        rs.getString("instruction") + " (Assigned: " +
                        rs.getDate("date_assigned") + ")");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Ошибка при подключении к базе данных: " + e.getMessage());
        }
    }


    public void showRealEstateSummaries() {
        String url = "jdbc:mysql://localhost:3306/company_db?useSSL=false";
        String user = "yourusername";
        String password = "yourpassword";

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            showTotalSales(conn);
            showRentalSumsByQuarter(conn);
            showLeaseToOwn(conn);
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Ошибка при подключении к базе данных: " + e.getMessage());
        }
    }

    private void showTotalSales(Connection conn) throws SQLException {
        String query = "SELECT SUM(price) AS total_sales FROM real_estate WHERE transaction_type = 'sale'";
        try (PreparedStatement stmt = conn.prepareStatement(query); ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                System.out.println("Общая сумма на продажу: " + rs.getDouble("total_sales"));
            }
        }
    }

    private void showRentalSumsByQuarter(Connection conn) throws SQLException {
        String query = "SELECT QUARTER(date_listed) AS quarter, SUM(price) AS total_rent FROM real_estate WHERE transaction_type = 'rent' GROUP BY QUARTER(date_listed)";
        try (PreparedStatement stmt = conn.prepareStatement(query); ResultSet rs = stmt.executeQuery()) {
            System.out.println("Сумма за аренду по кварталам:");
            while (rs.next()) {
                System.out.println("Квартал " + rs.getInt("quarter") + ": " + rs.getDouble("total_rent"));
            }
        }
    }

    private void showLeaseToOwn(Connection conn) throws SQLException {
        String query = "SELECT COUNT(*) AS lease_to_own_properties FROM real_estate WHERE transaction_type = 'lease-to-own'";
        try (PreparedStatement stmt = conn.prepareStatement(query); ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                System.out.println("Количество недвижимости на долгосрочный лизинг с последующим выкупом: " + rs.getInt("lease_to_own_properties"));
            }
        }
    }

    public void calculateRealEstatePercentages() {
        try (Connection conn = MyJDBC.getConnection();) {
            double totalProperties = getTotalProperties(conn);
            double rentPercentage = getPercentage(conn, "rent", totalProperties);
            double salePercentage = getPercentage(conn, "sale", totalProperties);
            double leaseToOwnPercentage = getPercentage(conn, "lease-to-own", totalProperties);

            System.out.println("Процент аренды: " + String.format("%.2f", rentPercentage) + "%");
            System.out.println("Процент продаж: " + String.format("%.2f", salePercentage) + "%");
            System.out.println("Процент долгосрочного лизинга с последующим выкупом: " + String.format("%.2f", leaseToOwnPercentage) + "%");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Ошибка при подключении к базе данных: " + e.getMessage());
        }
    }

    private double getTotalProperties(Connection conn) throws SQLException {
        String query = "SELECT COUNT(*) AS total FROM real_estate";
        try (PreparedStatement stmt = conn.prepareStatement(query); ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getDouble("total");
            }
        }
        return 0; // В случае ошибки или отсутствия данных
    }

    private double getPercentage(Connection conn, String transactionType, double total) throws SQLException {
        if (total == 0) return 0;
        String query = "SELECT COUNT(*) AS count FROM real_estate WHERE transaction_type = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, transactionType);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    double count = rs.getDouble("count");
                    return (count / total) * 100;
                }
            }
        }
        return 0; // В случае ошибки или отсутствия данных
    }


}
