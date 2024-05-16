package org.example;

import java.sql.*;
import java.util.Scanner;

public class GenDir extends Person {

    public GenDir(String username, String password) {
        super(username, password);
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Для запуска программы, пожалуйста введите тип аккаунта: >>> ");
        String accountType = scanner.nextLine();

        if (!accountType.equalsIgnoreCase("director")) {
            System.out.println("Извините, но мы не нашли такой тип аккаунта, пожалуйста повторите.");
            return;
        }

        System.out.print("Пожалуйста введите свой логин: >>> ");
        String login = scanner.nextLine();
        System.out.print("Пожалуйста введите свой пароль: >>> ");
        String password = scanner.nextLine();

        authenticateAndDisplayMenu(login, password);
    }

    public static void authenticateAndDisplayMenu(String login, String password) {
        try (Connection connection = MyJDBC.getConnection();) {
            if (authenticate(connection, login, password)) {
                System.out.println("Авторизация прошла успешно!");
                displayMenu(connection);
            } else {
                System.out.println("Ошибка аутентификации. Пожалуйста, проверьте логин и пароль.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean authenticate(Connection connection, String login, String password) throws SQLException {
        String query = "SELECT * FROM directors WHERE login = ? AND password = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, login);
        preparedStatement.setString(2, password);
        ResultSet resultSet = preparedStatement.executeQuery();
        return resultSet.next();
    }

    public static void displayMenu(Connection connection) {
        Scanner scanner = new Scanner(System.in);
        int choice;
        do {
            System.out.println("Приветствую дорогой, Директор!");
            System.out.println("Пожалуйста наберите номер меню для работы с программой, если закончили, то наберите 9:");
            System.out.println("1. Показать список всех зон покрытия");
            System.out.println("2. Показать список категорий бюджета");
            System.out.println("3. Показать выделенный бюджет для определенной категории мест для маркетинга");
            System.out.println("4. Показать текущие средства для маркетинга");
            System.out.println("5. Показать общий бюджет необходимый для зарплаты");
            System.out.println("6. Повысить зарплату сотруднику");
            System.out.println("7. Понизить зарплату сотруднику");
            System.out.println("8. Показать список оборудований для строительства объектов");
            System.out.println("9. Выход");

            choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (choice) {
                case 1:
                    showCoverageZones(connection);
                    break;
                case 2:
                    showBudgetCategories(connection);
                    break;
                case 3:
                    showAllocatedMarketingBudget(connection);
                    break;
                case 4:
                    showCurrentMarketingBudget(connection);
                    break;
                case 5:
                    showTotalSalaryBudget(connection);
                    break;
                case 6:
                    increaseEmployeeSalary(connection);
                    break;
                case 7:
                    decreaseEmployeeSalary(connection);
                    break;
                case 8:
                    showConstructionEquipment(connection);
                    break;
                case 9:
                    System.out.println("Программа завершена, мы будем рады вашему возвращению!");
                    break;
                default:
                    System.out.println("Неверный ввод, пожалуйста, попробуйте еще раз.");
                    break;
            }
        } while (choice != 9);
    }

    // Implement methods for each menu option here...

    // Example methods:

    public static void showCoverageZones(Connection connection) {
        // Implement logic to show coverage zones
    }

    public static void showBudgetCategories(Connection connection) {
        // Implement logic to show budget categories
    }

    public static void showAllocatedMarketingBudget(Connection connection) {
        // Implement logic to show allocated marketing budget
    }

    public static void showCurrentMarketingBudget(Connection connection) {
        // Implement logic to show current marketing budget
    }

    public static void showTotalSalaryBudget(Connection connection) {
        // Implement logic to show total salary budget
    }

    public static void increaseEmployeeSalary(Connection connection) {
        // Implement logic to increase employee salary
    }

    public static void decreaseEmployeeSalary(Connection connection) {
        // Implement logic to decrease employee salary
    }

    public static void showConstructionEquipment(Connection connection) {
        // Implement logic to show construction equipment
    }
}
