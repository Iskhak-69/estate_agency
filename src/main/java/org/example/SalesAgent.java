package org.example;

import java.sql.*;
import java.util.Scanner;

public class SalesAgent extends Person {
    public SalesAgent(String username, String password, String accountType) {
        super(username, password, accountType);
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Для запуска программы, пожалуйста введите тип аккаунта: >>> ");
        String accountType = scanner.nextLine();

        if (!accountType.equalsIgnoreCase("sale manager")) {
            System.out.println("Извините, но мы не нашли такой тип аккаунта, пожалуйста повторите.");
            System.out.print("Для запуска программы, пожалуйста введите корректный тип аккаунта: >>> ");
            accountType = scanner.nextLine();
        }

        System.out.print("Пожалуйста введите свой логин: >>> ");
        String username = scanner.nextLine();
        System.out.print("Пожалуйста введите свой пароль: >>> ");
        String password = scanner.nextLine();

        authenticateAndDisplayMenu(username, password, accountType);
    }

    public void authenticateAndDisplayMenu(String login, String password, String accpuntType) {
        // Assuming authentication logic here...
        // If authentication is successful, display the menu
        displayOptions();
    }

    public void displayOptions() {
        Scanner scanner = new Scanner(System.in);
        int choice;
        do {
            System.out.println("Приветствую дорогой, Агент по продажам!");
            System.out.println("Пожалуйста наберите номер меню для работы с программой, если закончили, то наберите 11:");
            System.out.println("1. Показать всех клиентов");
            System.out.println("2. Поиск клиента");
            System.out.println("3. Показать свободные квартиры для покупок");
            System.out.println("4. Показать проданные квартиры");
            System.out.println("5. Показать самую дорогую квартиру");
            System.out.println("6. Показать самую дешевую квартиру");
            System.out.println("7. Продать квартиру");
            System.out.println("8. Сдать в аренду квартиру");
            System.out.println("9. Показать свободные объекты в аренду");
            System.out.println("10. Показать занятые объекты в аренду");
            System.out.println("11. Выход");

            choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (choice) {
                case 1:
                    showAllClients();
                    break;
                case 2:
                    searchClient();
                    break;
                case 3:
                    showAvailableApartmentsForSale();
                    break;
                case 4:
                    showSoldApartments();
                    break;
                case 5:
                    showMostExpensiveApartment();
                    break;
                case 6:
                    showCheapestApartment();
                    break;
                case 7:
                    sellApartment("123 Main St", "John Doe");
                    break;
                case 8:
                    rentApartment("456 Elm St", 12, 1000.0, "Jane Smith");
                    break;
                case 9:
                    showAvailableApartmentsForRent();
                    break;
                case 10:
                    showRentedApartments();
                    break;
                case 11:
                    System.out.println("Программа завершена, мы будем рады вашему возвращению!");
                    break;
                default:
                    System.out.println("Неверный ввод, пожалуйста, попробуйте еще раз.");
                    break;
            }
        } while (choice != 11);
    }

    // Implement methods for each menu option here...

    // Example methods:

    public static void showAllClients() {
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

    public static void searchClient() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Введите имя клиента для поиска: ");
        String clientName = scanner.nextLine();

        try (Connection connection = MyJDBC.getConnection();) {
            String query = "SELECT * FROM clients WHERE name = ?";
            Statement statement = connection.prepareStatement(query);
            ((PreparedStatement) statement).setString(1, clientName);
            ResultSet resultSet = statement.executeQuery("SELECT * FROM clients");

            if (resultSet.next()) {
                System.out.println("Клиент найден: " + resultSet.getString("name") + " - " + resultSet.getString("details"));
            } else {
                System.out.println("Клиент не найден.");
            }
        } catch (SQLException e) {
            System.out.println("Ошибка при выполнении запроса: " + e.getMessage());
        }
    }

    public static void showAvailableApartmentsForSale() {
        try (Connection connection = MyJDBC.getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM available_apartments_for_sale");

            if (!resultSet.next()) {
                System.out.println("No available apartments for sale.");
            } else {
                System.out.println("Available Apartments for Sale:");
                do {
                    int id = resultSet.getInt("id");
                    String address = resultSet.getString("address");
                    double price = resultSet.getDouble("price");
                    String status = resultSet.getString("status");

                    System.out.println("ID: " + id);
                    System.out.println("Address: " + address);
                    System.out.println("Price: " + price);
                    System.out.println("Status: " + status);
                    System.out.println();
                } while (resultSet.next());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static void showSoldApartments() {
        try (Connection connection = MyJDBC.getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM sold_apartments");

            if (!resultSet.next()) {
                System.out.println("No sold apartments found.");
            } else {
                System.out.println("Sold Apartments:");
                do {
                    int id = resultSet.getInt("id");
                    String address = resultSet.getString("address");
                    double price = resultSet.getDouble("price");
                    String buyerName = resultSet.getString("buyer_name");
                    Date saleDate = resultSet.getDate("sale_date");

                    System.out.println("ID: " + id);
                    System.out.println("Address: " + address);
                    System.out.println("Price: " + price);
                    System.out.println("Buyer Name: " + buyerName);
                    System.out.println("Sale Date: " + saleDate);
                    System.out.println();
                } while (resultSet.next());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static void showMostExpensiveApartment() {
        try (Connection connection = MyJDBC.getConnection();
             Statement statement = connection.createStatement()) {

            String query = "SELECT * FROM apartments ORDER BY price DESC LIMIT 1";
            ResultSet resultSet = statement.executeQuery(query);

            if (resultSet.next()) {
                int id = resultSet.getInt("id");
                String address = resultSet.getString("address");
                double price = resultSet.getDouble("price");

                System.out.println("Most Expensive Apartment:");
                System.out.println("ID: " + id);
                System.out.println("Address: " + address);
                System.out.println("Price: $" + price);
            } else {
                System.out.println("No apartments found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void showCheapestApartment() {
        try (Connection connection = MyJDBC.getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM apartments ORDER BY price ASC LIMIT 1");

            if (!resultSet.next()) {
                System.out.println("No apartments found.");
            } else {
                System.out.println("Cheapest apartment:");
                System.out.println("ID: " + resultSet.getInt("id"));
                System.out.println("Address: " + resultSet.getString("address"));
                System.out.println("Price: " + resultSet.getBigDecimal("price"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static void sellApartment(String address, String newOwner) {
        try (Connection connection = MyJDBC.getConnection()) {
            String query = "UPDATE apartments SET owner = ?, status = 'Sold' WHERE address = ? AND status = 'Available'";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, newOwner);
            statement.setString(2, address);
            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Apartment sold successfully.");
            } else {
                System.out.println("No available apartment found at the specified address.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Rent an apartment
    public static void rentApartment(String address, int durationMonths, double rentAmount, String newTenant) {
        try (Connection connection = MyJDBC.getConnection()) {
            String query = "UPDATE apartments SET status = 'Rented' WHERE address = ? AND status = 'Available'";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, address);
            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Apartment rented successfully.");
            } else {
                System.out.println("No available apartment found at the specified address.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Show available apartments for rent
    public static void showAvailableApartmentsForRent() {
        try (Connection connection = MyJDBC.getConnection()) {
            String query = "SELECT * FROM apartments WHERE status = 'Available'";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            if (!resultSet.next()) {
                System.out.println("No apartments are available for rent.");
            } else {
                System.out.println("Available apartments for rent:");
                do {
                    String address = resultSet.getString("address");
                    System.out.println("- " + address);
                } while (resultSet.next());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Show rented apartments
    public static void showRentedApartments() {
        try (Connection connection = MyJDBC.getConnection()) {
            String query = "SELECT * FROM apartments WHERE status = 'Rented'";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            if (!resultSet.next()) {
                System.out.println("No apartments are currently rented.");
            } else {
                System.out.println("Rented apartments:");
                do {
                    String address = resultSet.getString("address");
                    System.out.println("- " + address);
                } while (resultSet.next());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
