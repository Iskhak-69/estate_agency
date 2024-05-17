import java.sql.*;
import java.util.Scanner;

public class GenDir extends Person {
    private static final  Scanner scanner = new Scanner(System.in);
    public GenDir(String username, String password, String accountType) {
        super(username, password, accountType);
    }



    public void displayOptions(Connection connection) {

        int choice;
        System.out.println("Приветствую дорогой, Директор!");
        do {
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


    public static void showCoverageZones(Connection connection) {
        String query = "SELECT id, area_name, region_name FROM coverage_zones";

        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            System.out.println("Список всех зон покрытия:");
            while (rs.next()) {
                System.out.println(rs.getInt("id") + ": " + rs.getString("area_name") + " - " +
                        rs.getString("region_name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Ошибка при подключении к базе данных: " + e.getMessage());
        }
        System.out.println();
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
        System.out.println();
    }


    public static void showAllocatedMarketingBudget(Connection connection) {

        String query = "SELECT DISTINCT id, category_name FROM marketing_budgets";

        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            System.out.println("Список зон для маркетинга:");

            while (rs.next()) {
                int id = rs.getInt("id");
                String category = rs.getString("category_name");
                System.out.println( id + " " + category);
            }


        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Ошибка при подключении к базе данных: " + e.getMessage());
        }
        System.out.println("Выберите зону, чтобы увидеть маркетинговый бюджет:");
        String category = scanner.nextLine();

        query = "SELECT budget FROM marketing_budgets WHERE category_name = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, category);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                double budget = rs.getDouble("budget");
                System.out.println("Выделенный маркетинговый бюджет для категории '" + category + "': $" + budget);
            } else {
                System.out.println("Маркетинговый бюджет для категории '" + category + "' не выделен.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Ошибка при подключении к базе данных: " + e.getMessage());
        }
        System.out.println();
    }


    public static void showCurrentMarketingBudget(Connection connection) {
        String budgetQuery = "SELECT SUM(budget) AS budget FROM marketing_budgets";
        String spentBudgetQuery = "SELECT SUM(amount) AS spent_budget FROM marketing_expenses";

        try (PreparedStatement allocatedStmt = connection.prepareStatement(budgetQuery);
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
        System.out.println();
    }


    public static void showTotalSalaryBudget(Connection connection) {
        String query = "SELECT SUM(salary) AS total_salary FROM employees";

        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            if (resultSet.next()) {
                double totalSalary = resultSet.getDouble("total_salary");
                System.out.println("Общий бюджет заработной платы: " + totalSalary);
            } else {
                System.out.println("Нет доступных данных.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Ошибка при получении общего бюджета заработной платы: " + e.getMessage());
        }
        System.out.println();
    }


    public static void increaseEmployeeSalary(Connection connection) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Введите ID сотрудника, зарплату которого вы хотите увеличить: ");
        int employeeId = scanner.nextInt();

        System.out.print("Введите сумму, на которую нужно увеличить зарплату: ");
        double increaseAmount = scanner.nextDouble();

        String query = "UPDATE employees SET salary = salary + ? WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setDouble(1, increaseAmount);
            statement.setInt(2, employeeId);

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Заработная плата сотрудников успешно повышалась.");
            } else {
                System.out.println("Сотрудник с указанным идентификатором не найден.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Ошибка в повышении заработной платы сотрудника: " + e.getMessage());
        }
        System.out.println();
    }



    public static void decreaseEmployeeSalary(Connection connection) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Введите ID сотрудника, зарплату которого вы хотите уменьшить: ");
        int employeeId = scanner.nextInt();

        System.out.print("Введите сумму, на которую следует уменьшить заработную плату: ");
        double decreaseAmount = scanner.nextDouble();

        String query = "UPDATE employees SET salary = salary - ? WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setDouble(1, decreaseAmount);
            statement.setInt(2, employeeId);

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Заработная плата сотрудников успешно снизилась.");
            } else {
                System.out.println("Сотрудник с указанным идентификатором не найден.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Ошибка, приводящая к снижению заработной платы сотрудника: " + e.getMessage());
        }
        System.out.println();
    }

    private static void showConstructionEquipment(Connection connection) {
        String query = "SELECT id, equipment_name, quantity FROM construction_equipment";

        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            System.out.println("Перечень строительного оборудования: ");
            while (rs.next()) {
                int id = rs.getInt("id");
                String equipmentName = rs.getString("equipment_name");
                int quantity = rs.getInt("quantity");

                System.out.println("ID: " + id + ", Название оборудования: " + equipmentName + ", Количество: " + quantity);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Ошибка подключения к базе данных: " + e.getMessage());
        }
        System.out.println();
    }
}
