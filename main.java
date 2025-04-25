import java.sql.*;
import java.util.Scanner;

public class main {

    static final String URL = "jdbc:mysql://localhost:3306/food_delivery";
    static final String USER = "root";
    static final String PASSWORD = "1234"; // use your MySQL password if you have one

    public static Connection connect() {
        try {
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("✅ Connected to Database!");
            return conn;
        } catch (SQLException e) {
            System.out.println("❌ Connection Failed:");
            e.printStackTrace();
            return null;
        }
    }

    public static void insertFoodItem(String name, double price) {
        Connection conn = connect();
        if (conn == null) return;

        String sql = "INSERT INTO food_items (name, price) VALUES (?, ?)";

        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, name);
            stmt.setDouble(2, price);
            stmt.executeUpdate();
            System.out.println("✅ Food item inserted successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void viewAllFoodItems() {
        Connection conn = connect();
        if (conn == null) return;

        String sql = "SELECT * FROM food_items";

        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            System.out.println("\n📦 All Food Items:");
            System.out.println("---------------------------");
            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("id"));
                System.out.println("Name: " + rs.getString("name"));
                System.out.println("Price: " + rs.getDouble("price"));
                System.out.println("---------------------------");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n===== Food Delivery Menu =====");
            System.out.println("1. Add Food Item");
            System.out.println("2. View All Food Items");
            System.out.println("3. Exit");
            System.out.print("Choose an option: ");

            int choice = sc.nextInt();
            sc.nextLine(); // clear buffer

            switch (choice) {
                case 1:
                    System.out.print("Enter food name: ");
                    String name = sc.nextLine();
                    System.out.print("Enter price: ");
                    double price = sc.nextDouble();
                    insertFoodItem(name, price);
                    break;
                case 2:
                    viewAllFoodItems();
                    break;
                case 3:
                    System.out.println("👋 Exiting...");
                    return;
                default:
                    System.out.println("⚠️ Invalid option. Try again.");
            }
        }
    }
}

