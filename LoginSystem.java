import java.sql.*;
import java.util.Scanner;

public class LoginSystem {

    static final String URL = "jdbc:mysql://localhost:3306/food_delivery";
    static final String USER = "root";
    static final String PASSWORD = "1234";

    public static Connection connect() {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            System.out.println("❌ Database connection failed.");
            return null;
        }
    }

    public static void registerUser(String username, String password) {
        Connection conn = connect();
        if (conn == null) return;

        String sql = "INSERT INTO users (username, password) VALUES (?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.executeUpdate();
            System.out.println("✅ Registered successfully!");
        } catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("⚠️ Username already taken.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean login(String username, String password) {
        Connection conn = connect();
        if (conn == null) return false;

        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void forgotPassword(String username) {
        Connection conn = connect();
        if (conn == null) return;

        String sql = "SELECT password FROM users WHERE username = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                System.out.println("🔐 Your password is: " + rs.getString("password"));
            } else {
                System.out.println("❌ Username not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void insertFoodItem(int id, String name, double price) {
        Connection conn = connect();
        if (conn == null) return;

        String sql = "INSERT INTO food_items (id, name, price) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.setString(2, name);
            stmt.setDouble(3, price);
            stmt.executeUpdate();
            System.out.println("✅ Food item added!");
        } catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("⚠️ Food ID already exists.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void viewAllFoodItems() {
        Connection conn = connect();
        if (conn == null) return;

        String sql = "SELECT * FROM food_items";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            System.out.println("\n📦 All Food Items:");
            while (rs.next()) {
                System.out.println("---------------------------");
                System.out.println("ID: " + rs.getInt("id"));
                System.out.println("Name: " + rs.getString("name"));
                System.out.println("Price: " + rs.getDouble("price"));
            }
            System.out.println("---------------------------");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteFoodItem(int id) {
        Connection conn = connect();
        if (conn == null) return;

        String sql = "DELETE FROM food_items WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            int result = stmt.executeUpdate();
            if (result > 0) {
                System.out.println("✅ Food item deleted.");
            } else {
                System.out.println("❌ Item not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        boolean loggedIn = false;

        while (!loggedIn) {
            System.out.println("\n=== Welcome to Food Delivery App ===");
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. Forgot Password");
            System.out.print("Choose an option: ");
            int choice = sc.nextInt();
            sc.nextLine(); // Clear buffer

            switch (choice) {
                case 1:
                    System.out.print("Set Username: ");
                    String newUser = sc.nextLine();
                    System.out.print("Set Password: ");
                    String newPass = sc.nextLine();
                    registerUser(newUser, newPass);
                    break;
                case 2:
                    System.out.print("Enter Username: ");
                    String user = sc.nextLine();
                    System.out.print("Enter Password: ");
                    String pass = sc.nextLine();
                    if (login(user, pass)) {
                        System.out.println("✅ Login successful!");
                        loggedIn = true;
                    } else {
                        System.out.println("❌ Invalid credentials.");
                    }
                    break;
                case 3:
                    System.out.print("Enter Username: ");
                    String forgotUser = sc.nextLine();
                    forgotPassword(forgotUser);
                    break;
                default:
                    System.out.println("⚠️ Invalid option.");
            }
        }

        // Main menu after login
        while (true) {
            System.out.println("\n===== Food Menu =====");
            System.out.println("1. Add Food Item");
            System.out.println("2. View All Food Items");
            System.out.println("3. Delete Food Item");
            System.out.println("4. Logout");
            System.out.print("Choose option: ");
            int op = sc.nextInt();
            sc.nextLine();

            switch (op) {
                case 1:
                    System.out.print("Enter ID (less than 1000): ");
                    int id = sc.nextInt();
                    sc.nextLine();
                    System.out.print("Enter Food Name: ");
                    String name = sc.nextLine();
                    System.out.print("Enter Price: ");
                    double price = sc.nextDouble();
                    insertFoodItem(id, name, price);
                    break;
                case 2:
                    viewAllFoodItems();
                    break;
                case 3:
                    System.out.print("Enter ID to delete: ");
                    int delId = sc.nextInt();
                    deleteFoodItem(delId);
                    break;
                case 4:
                    System.out.println("👋 Logged out. Bye!");
                    return;
                default:
                    System.out.println("⚠️ Invalid option.");
            }
        }
    }
}
