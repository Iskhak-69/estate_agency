import java.sql.*;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean proccesing = true;
        int choice = 0;

        while (proccesing && (choice != 1 || choice != 2)) {
            System.out.println("1. Login");
            System.out.println("2. Exit");
            System.out.print("Choose an option: ");
            choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    login(scanner);
                    break;
                case 2:
                    System.out.println("Exiting the program... \nThank you to visiting our program.");
                    proccesing = false;
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

            String query = "SELECT username, password, accountType FROM users WHERE username = ? AND password = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);

            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String accountType = resultSet.getString("accountType");
                System.out.println("Login successful!");

                switch (accountType) {
                    case "manager":
                        Manager manager = new Manager(username, password, accountType);
                        manager.displayOptions();
                        break;
                    case "genDir":
                        GenDir genDir = new GenDir(username, password, accountType);
                        genDir.displayOptions(connection);
                        break;
                    case "marketing":
                        Marketing marketing = new Marketing(username, password, accountType);
                        marketing.displayOptions();
                        break;
                    case "sale manager":
                        SalesAgent salesAgent = new SalesAgent(username, password, accountType);
                        salesAgent.displayOptions();
                        break;
                    case "worker":
                        Worker worker = new Worker(username, password, accountType);
                        worker.displayOptions();
                        break;
                    default:
                        System.out.println("Incorrect account type.");
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
