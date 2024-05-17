import java.sql.*;
import java.util.Scanner;

public class GenDir extends Person {

    public GenDir(String username, String password, String accountType) {
        super(username, password, accountType);
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
            System.out.print("Ваш выбор: ");

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

    private static void showConstructionEquipment(Connection connection) {
        String query = "SELECT id, equipment_name, quantity FROM construction_equipment";

        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            System.out.println("List of Construction Equipment:");
            while (rs.next()) {
                int id = rs.getInt("id");
                String equipmentName = rs.getString("equipment_name");
                int quantity = rs.getInt("quantity");

                System.out.println("ID: " + id + ", Equipment Name: " + equipmentName + ", Quantity: " + quantity);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error connecting to the database: " + e.getMessage());
        }
    }


    public static void showCoverageZones(Connection connection) {
        String query = "SELECT id, area_name, region FROM coverage_zones";

        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            System.out.println("Список всех зон покрытия:");
            while (rs.next()) {
                System.out.println(rs.getInt("id") + ": " + rs.getString("area_name") + " - " +
                        rs.getString("region"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Ошибка при подключении к базе данных: " + e.getMessage());
        }
    }


    public static void showBudgetCategories(Connection connection) {
        String query = "SELECT id, category_name, description FROM budget_categories";

        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            System.out.println("Список категорий бюджета:");
            while (rs.next()) {
                System.out.println(rs.getInt("id") + ": " + rs.getString("category_name") + " - " +
                        rs.getString("description"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Ошибка при подключении к базе данных: " + e.getMessage());
        }
    }


    public static void showAllocatedMarketingBudget(Connection connection) {
        String query = "SELECT SUM(amount) AS allocated_budget FROM marketing_budget";

        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                double allocatedBudget = rs.getDouble("allocated_budget");
                System.out.println("Выделенный маркетинговый бюджет: $" + allocatedBudget);
            } else {
                System.out.println("Маркетинговый бюджет не выделен.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Ошибка при подключении к базе данных: " + e.getMessage());
        }
    }


    public static void showCurrentMarketingBudget(Connection connection) {
        String allocatedBudgetQuery = "SELECT SUM(amount) AS budget FROM marketing_budget";
        String spentBudgetQuery = "SELECT SUM(amount) AS spent_budget FROM marketing_expenses";

        try (PreparedStatement allocatedStmt = connection.prepareStatement(allocatedBudgetQuery);
             ResultSet allocatedRs = allocatedStmt.executeQuery();
             PreparedStatement spentStmt = connection.prepareStatement(spentBudgetQuery);
             ResultSet spentRs = spentStmt.executeQuery()) {

            double budget = 0;
            double spentBudget = 0;

            if (allocatedRs.next()) {
                budget = allocatedRs.getDouble("budget");
            }

            if (spentRs.next()) {
                spentBudget = spentRs.getDouble("spent_budget");
            }

            double remainingBudget = budget - spentBudget;

            System.out.println("Текущий маркетинговый бюджет: $" + remainingBudget);
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Ошибка при подключении к базе данных: " + e.getMessage());
        }
    }


    public static void showTotalSalaryBudget(Connection connection) {
        String query = "SELECT SUM(salary) AS total_salary FROM employees";

        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            if (resultSet.next()) {
                double totalSalary = resultSet.getDouble("total_salary");
                System.out.println("Total Salary Budget: " + totalSalary);
            } else {
                System.out.println("No data available.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error retrieving total salary budget: " + e.getMessage());
        }
    }


    public static void increaseEmployeeSalary(Connection connection) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the ID of the employee whose salary you want to increase: ");
        int employeeId = scanner.nextInt();

        System.out.print("Enter the amount by which to increase the salary: ");
        double increaseAmount = scanner.nextDouble();

        String query = "UPDATE employees SET salary = salary + ? WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setDouble(1, increaseAmount);
            statement.setInt(2, employeeId);

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Employee salary increased successfully.");
            } else {
                System.out.println("No employee found with the specified ID.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error increasing employee salary: " + e.getMessage());
        }
    }



    public static void decreaseEmployeeSalary(Connection connection) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the ID of the employee whose salary you want to decrease: ");
        int employeeId = scanner.nextInt();

        System.out.print("Enter the amount by which to decrease the salary: ");
        double decreaseAmount = scanner.nextDouble();

        String query = "UPDATE employees SET salary = salary - ? WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setDouble(1, decreaseAmount);
            statement.setInt(2, employeeId);

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Employee salary decreased successfully.");
            } else {
                System.out.println("No employee found with the specified ID.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error decreasing employee salary: " + e.getMessage());
        }

    }
}
