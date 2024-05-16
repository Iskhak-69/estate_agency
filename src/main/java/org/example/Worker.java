package org.example;

import java.util.Scanner;
import java.sql.*;

public class Worker extends Person{


    public Worker(String username, String password) {
        super(username, password);
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

        Worker worker = new Worker(login, password);
        worker.displayOptions();
    }

    public void displayOptions() {
        if (authenticate(username, password)) {
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

                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                switch (choice) {
                    case 1:
                        showAssignedTasks();
                        break;
                    case 2:
                        // Complete task
                        break;
                    case 3:
                        // Show completed tasks
                        break;
                    case 4:
                        // Show salary
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
}
