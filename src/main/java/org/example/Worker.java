package org.example;

import java.util.Scanner;
import java.sql.*;

public class Worker extends Person{


    public Worker(String username, String password, String accountType) {
        super(username, password, accountType);
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Для запуска программы, пожалуйста введите тип аккаунта: >>> ");
        String accountType = scanner.nextLine();

        if (!accountType.equalsIgnoreCase("worker") && !accountType.equalsIgnoreCase("comp-worker")) {
            System.out.println("Извините, но мы не нашли такой тип аккаунта, пожалуйста повторите.");
            return;
        }

        System.out.print("Пожалуйста введите свой логин: >>> ");
        String login = scanner.nextLine();
        System.out.print("Пожалуйста введите свой пароль: >>> ");
        String password = scanner.nextLine();

        Worker worker = new Worker(login, password, accountType);
        worker.displayOptions();
    }

    public void displayOptions() {
        if (authenticate(username, password, accountType)) {
            System.out.println("Авторизация прошла успешно!");

            Scanner scanner = new Scanner(System.in);

            while (true) {
                System.out.println("Приветствую, дорогой Сотрудник " + username + "!");
                System.out.println("Пожалуйста, наберите номер меню для работы с программой, или 5 для выхода:");
                System.out.println("1. Показать список порученных вам дел.");
                System.out.println("2. Выполнить поручение.");
                System.out.println("3. Показать список завершенных задач.");
                System.out.println("4. Показать зарплату.");
                System.out.println("5. Выход.");

                System.out.print("Ваш выбор: ");
                int choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1:
                        showAssignedTasks();
                        break;
                    case 2:
                        System.out.print("Введите описание задачи для завершения: ");
                        String taskDescription = scanner.nextLine();
                        completeTask(taskDescription);
                        break;
                    case 3:
                        showCompletedTasks();
                        break;
                    case 4:
                        showSalary();
                        break;
                    case 5:
                        System.out.println("Программа завершена, мы будем рады вашему возвращению!");
                        return;
                    default:
                        System.out.println("Некорректный выбор. Пожалуйста, попробуйте снова.");
                }
            }
        } else {
            System.out.println("Ошибка аутентификации. Пожалуйста, проверьте логин и пароль.");
        }
    }

    private static void showAssignedTasks() {
        try (Connection connection = MyJDBC.getConnection();
           ) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT task_description FROM assigned_tasks WHERE employee_login = ?");
            if(!resultSet.next()){
                System.out.println("Task is empty ");
            }
            else{
                System.out.println("Список порученных вам дел:");
                do{
                    String taskDescription = resultSet.getString("task_description");
                    System.out.println("- " + taskDescription);
                }
                while (resultSet.next());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void completeTask(String taskDescription) {
        try (Connection connection = MyJDBC.getConnection();
             PreparedStatement statement = connection.prepareStatement("DELETE FROM assigned_tasks WHERE task_description = ?")) {
            statement.setString(1, taskDescription);
            int rowsDeleted = statement.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Task '" + taskDescription + "' completed successfully.");
            } else {
                System.out.println("Failed to complete task '" + taskDescription + "'. Task not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void showCompletedTasks() {
        try (Connection connection = MyJDBC.getConnection();
             Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("SELECT task_description FROM completed_tasks WHERE employee_login = ?");
            if (!resultSet.next()) {
                System.out.println("No completed tasks found.");
            } else {
                System.out.println("Список завершенных вами задач:");
                do {
                    String taskDescription = resultSet.getString("task_description");
                    System.out.println("- " + taskDescription);
                } while (resultSet.next());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void showSalary() {
        try (Connection connection = MyJDBC.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT salary FROM workers WHERE username = ?");
        ) {
            // Set the username parameter in the prepared statement
            statement.setString(1, username);

            // Execute the query to fetch the salary
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                double salary = resultSet.getDouble("salary");
                System.out.println("Your salary is: $" + salary);
            } else {
                System.out.println("Salary information not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
