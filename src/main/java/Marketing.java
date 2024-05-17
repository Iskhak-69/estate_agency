import java.sql.*;
import java.util.Scanner;

public class Marketing extends Person {
    Scanner scanner = new Scanner(System.in);

    public Marketing(String username, String password, String accountType) {
        super(username, password, accountType);
    }
    public void displayOptions() {
        int choice;
        do{
            System.out.println("1. View users\n2. Add product to cart\n3. Remove product from cart\n4. View cart\n5. Leave a review about the product\n6. Exit");
            choice = getChoice();
        }while (choice != 6);
    }
    public int getChoice(){
        int choice = 0 ;
        do {
            System.out.println("Приветствую дорогой, Маркетолог!");
            System.out.println("Пожалуйста наберите номер меню для работы с программой, если закончили, то наберите 5:");
            System.out.println("1. Показать список категорий для маркетинга");
            System.out.println("2. Показать выделенный бюджет для определенной категории мест для маркетинга");
            System.out.println("3. Показать общий бюджет для маркетинга");
            System.out.println("4. Потратить бюджет на продвижение");
            System.out.println("5. Выход");

            choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    viewUsers();
                    break;
                case 2:
                    showCategoryBudget();
                    break;
                case 3:
                    showTotalMarketingBudget();
                    break;
                case 4:
                    spendMarketingBudget();
                    break;
                case 5:
                    System.out.println("Программа завершена, мы будем рады вашему возвращению!");
                    break;
                default:
                    System.out.println("Неверный ввод, пожалуйста, попробуйте еще раз.");
                    break;
            }
        } while (choice != 5);
        return choice;
    }
    public void viewUsers() {
        try (Connection conn = MyJDBC.getConnection(); // Get connection from MyJDBC class
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT id, name, role FROM users")) {

            if (!rs.next()) {
                System.out.println("The users is currently empty.");
            } else {
                System.out.println("users:");
                do {
                    System.out.println("ID: " + rs.getInt("id") + ", Name: " + rs.getString("name") +
                            ", role: " + rs.getString("role"));
                } while (rs.next());
            }
        } catch (SQLException e) {
            System.out.println("Error accessing the catalog: " + e.getMessage());
        }
    }



    public void showCategoryBudget() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите название категории для отображения бюджета:");
        String categoryName = scanner.nextLine();

        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/yourdatabase", "yourusername", "yourpassword");
            String query = "SELECT category_name, budget FROM marketing_budgets WHERE category_name = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, categoryName);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                System.out.println("Категория: " + resultSet.getString("category_name") + ", Бюджет: " + resultSet.getDouble("budget"));
            } else {
                System.out.println("Бюджет для данной категории не найден.");
            }

            resultSet.close();
            statement.close();
            connection.close();
        } catch (Exception e) {
            System.out.println("Ошибка при подключении к базе данных: " + e.getMessage());
        }
    }
    public void showTotalMarketingBudget() {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/yourdatabase", "yourusername", "yourpassword");
            String query = "SELECT SUM(budget) AS total_budget FROM marketing_budgets";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                double totalBudget = resultSet.getDouble("total_budget");
                System.out.println("Общий бюджет для маркетинга: " + totalBudget);
            } else {
                System.out.println("Информация о бюджетах не найдена.");
            }

            resultSet.close();
            statement.close();
            connection.close();
        } catch (Exception e) {
            System.out.println("Ошибка при подключении к базе данных: " + e.getMessage());
        }
    }

    public void spendMarketingBudget() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Выберите название категории для продвижения:");
        String categoryName = scanner.nextLine();
        System.out.println("Наберите сумму расхода, которую вы хотите потратить из бюджета:");
        double spendingAmount = scanner.nextDouble();

        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/yourdatabase", "yourusername", "yourpassword");
            connection.setAutoCommit(false); // Используем транзакцию

            // Проверяем текущий бюджет
            String checkQuery = "SELECT budget FROM marketing_budgets WHERE category_name = ?";
            PreparedStatement checkStmt = connection.prepareStatement(checkQuery);
            checkStmt.setString(1, categoryName);
            ResultSet resultSet = checkStmt.executeQuery();

            if (!resultSet.next()) {
                System.out.println("Категория не найдена.");
                return;
            }

            double currentBudget = resultSet.getDouble("budget");
            if (spendingAmount > currentBudget) {
                System.out.println("Недостаточно средств в бюджете.");
                return;
            }

            // Обновляем бюджет
            String updateQuery = "UPDATE marketing_budgets SET budget = budget - ? WHERE category_name = ?";
            PreparedStatement updateStmt = connection.prepareStatement(updateQuery);
            updateStmt.setDouble(1, spendingAmount);
            updateStmt.setString(2, categoryName);
            updateStmt.executeUpdate();

            connection.commit(); // Подтверждаем изменения
            System.out.println("Бюджет успешно расходован.");

            resultSet.close();
            checkStmt.close();
            updateStmt.close();
            connection.close();
        } catch (Exception e) {
            System.out.println("Ошибка при подключении к базе данных: " + e.getMessage());
        }
    }

}
