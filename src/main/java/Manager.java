import java.sql.*;
import java.util.Scanner;

public class Manager extends Person {

    Scanner scanner = new Scanner(System.in);

    public Manager(String username, String password, String accountType) {
        super(username, password, accountType);
    }


    public void displayOptions() {
        int choice = 0;
        System.out.println("Приветствую дорогой, Менеджер!");
        do {
            System.out.println("Пожалуйста наберите номер меню для работы с программой, если закончили, то наберите 7:");
            System.out.println("1. Показать список сотрудников");
            System.out.println("2. Показать список дел");
            System.out.println("3. Показать список указаний к сотрудникам");
            System.out.println("4. Показать список всех зон покрытия");
            System.out.println("5. Показать сумму по недвижимостям");
            System.out.println("6. Посчитать % по категориям недвижимости");
            System.out.println("7. Выход");

            choice = scanner.nextInt();
            scanner.nextLine(); // Это нужно чтобы очистить бцфер после чтения числа

            switch (choice) {
                case 1:
                    // Показать список сотрудников
                    showEmployees();
                    break;
                case 2:
                    // Показать список дел
                    showAndAddTasks();
                    break;
                case 3:
                    // Показать список указаний к сотрудникам
                    showInstructions();
                    break;
                case 4:
                    // Показать список всех зон покрытия
                    showCoverageAreas();
                    break;

                case 5:
                    // Показать сумму по недвижимостям
                    showRealEstateSummaries();
                    break;
                case 6:
                    // Посчитать % по категориям недвижимости
                    calculateRealEstatePercentages();
                    break;
                case 7:
                    System.out.println("Программа завершена, мы будем рады вашему возвращению!");
                    break;
                default:
                    System.out.println("Неверный ввод, пожалуйста, попробуйте еще раз.");
                    break;
            }
        } while (choice != 7);
    }
    public void showEmployees() {
        String query = "SELECT id, first_name, last_name, position, department FROM employees";

        try (Connection connection = MyJDBC.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query);
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
        System.out.println();
    }

    public void showAndAddTasks() {

        try (Connection conn = MyJDBC.getConnection();
        ) {
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT id, task_name, description, due_date FROM tasks");

            System.out.println("Список всех дел:");
            while (resultSet.next()) {
                System.out.println(resultSet.getInt("id") + ": " + resultSet.getString("task_name") + " - " +
                        resultSet.getString("description") + " (Due: " + resultSet.getDate("due_date") + ")");
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
        System.out.println();
    }

    private void addTaskToFile(String taskName) {
        String query = "INSERT INTO tasks (task_name) VALUES (?)";

        try (Connection conn = MyJDBC.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, taskName);
            int rowsInserted = stmt.executeUpdate();

            if (rowsInserted > 0) {
                System.out.println("Задача добавлена в базу данных: " + taskName);
            } else {
                System.out.println("Ошибка при добавлении задачи в базу данных.");
            }
        } catch (SQLException e) {
            System.out.println("Ошибка при выполнении SQL-запроса: " + e.getMessage());
        }
        System.out.println();
    }



    public void showInstructions() {
        String query = "SELECT id, first_name, last_name, instruction, date_assigned FROM instructions JOIN employees ON employee_id = id";

        try (Connection connection = MyJDBC.getConnection();
        ) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            System.out.println("Список всех указаний к сотрудникам:");
            while (resultSet.next()) {
                System.out.println(resultSet.getInt("id") + ": " + resultSet.getString("first_name") + " " +
                        resultSet.getString("last_name") + " - " +
                        resultSet.getString("instruction") + " (Assigned: " +
                        resultSet.getDate("date_assigned") + ")");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Ошибка при подключении к базе данных: " + e.getMessage());
        }
        System.out.println();
    }

    public void showCoverageAreas() {
        String query = "SELECT id, area_name, region FROM coverage_areas";

        try (Connection connection = MyJDBC.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query);
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
        System.out.println();
    }


    public void showRealEstateSummaries() {
        try (Connection connection = MyJDBC.getConnection();) {
            showTotalSales(connection);
            showRentalSumsByQuarter(connection);
            showLeaseToOwn(connection);
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Ошибка при подключении к базе данных: " + e.getMessage());
        }
        System.out.println();
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
        System.out.println();
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
