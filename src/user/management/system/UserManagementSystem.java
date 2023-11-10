package user.management.system;

import java.sql.*;
import java.util.Scanner;

public class UserManagementSystem {

    static void menu() {
        System.out.println(purple + "USER MANAGEMENT SYSTEM \n");
        System.out.println(blue + "1. Show Users");
        System.out.println(blue + "2. Add new Users");
        System.out.println(blue + "3. Update Users");
        System.out.println(blue + "4. Delete Users");
        System.out.println(blue + "5. Exit");

        Scanner input = new Scanner(System.in);
        System.out.print(cyan + "Enter a number : ");
        int num = input.nextInt();

        switch (num) {
            case 1:
                show();
                break;
            case 2:
                insert();
                break;
            case 3:
                update();
                break;
            case 4:
                delete();
                break;
            case 5:
                System.out.println(red + "Exiting the program. Tata Bye!");
                System.exit(0);
                break;
            default:
                System.out.println(red + "Invalid Input");
                System.out.println(" ");
                menu();
        }
    }

    static void show() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM users");

            System.out.println(blue + "+----+------------+-----------+-----+----------------+");
            System.out.println(purple + "| ID | First Name | Last Name | Age | Address        |");
            System.out.println(blue + "+----+------------+-----------+-----+----------------+"); 

            while (rs.next()) {
                int id = rs.getInt("id");
                String fname = rs.getString("fname");
                String lname = rs.getString("lname");
                String age = rs.getString("age");
                String address = rs.getString("address");

                System.out.format(blue + "| %-2d | %-10s | %-9s | %-3s | %-14s |%n", id, fname, lname, age, address);
            }

            System.out.println(blue + "+----+------------+-----------+-----+----------------+");
        } catch (SQLException e) {
            System.out.println("Exception " + e);
        }
        System.out.println(" ");
        menu();
    }

    static void insert() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "INSERT INTO users (fname, lname, age, address) VALUES (?,?,?,?)";
            PreparedStatement stmt = conn.prepareStatement(sql);

            Scanner input = new Scanner(System.in);

            System.out.print(green + "Enter first name : ");
            String fname = input.nextLine();
            System.out.print(green + "Enter last name : ");
            String lname = input.nextLine();
            System.out.print(green + "Enter age : ");
            String age = input.nextLine();
            System.out.print(green + "Enter address : ");
            String address = input.nextLine();

            stmt.setString(1, fname);
            stmt.setString(2, lname);
            stmt.setString(3, age);
            stmt.setString(4, address);

            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println(green + "Success");
            }
        } catch (SQLException e) {
            System.out.println("Exception " + e);
        }
        System.out.println(" ");
        menu();
    }

    static void update() {
        Scanner input = new Scanner(System.in);
        System.out.print(green + "Enter the ID of the user you want to update: ");
        int userId = input.nextInt();

        try (Connection conn = DatabaseConnection.getConnection()) {
            String checkSql = "SELECT * FROM users WHERE id = ?";
            PreparedStatement checkStmt = conn.prepareStatement(checkSql);
            checkStmt.setInt(1, userId);

            ResultSet checkResult = checkStmt.executeQuery();

            if (checkResult.next()) {
                System.out.println(blue + "+----+------------+-----------+-----+----------------+");
                System.out.println(purple + "| ID | First Name | Last Name | Age | Address        |");
                System.out.println(blue + "+----+------------+-----------+-----+----------------+");

                int id = checkResult.getInt("id");
                String fname = checkResult.getString("fname");
                String lname = checkResult.getString("lname");
                String age = checkResult.getString("age");
                String address = checkResult.getString("address");

                System.out.format(blue + "| %-2d | %-10s | %-9s | %-3s | %-14s |%n", id, fname, lname, age, address);
                System.out.println(blue + "+----+------------+-----------+-----+----------------+");

                System.out.println(cyan + "User with ID " + userId + " found. Please enter the updated information:");

                System.out.print(green + "Enter new first name: ");
                String newFirstName = input.next();
                System.out.print(green + "Enter new last name: ");
                String newLastName = input.next();
                System.out.print(green + "Enter new age: ");
                String newAge = input.next();
                System.out.print(green + "Enter new address: ");
                String newAddress = input.next();

                String updateSql = "UPDATE users SET fname = ?, lname = ?, age = ?, address = ? WHERE id = ?";
                PreparedStatement updateStmt = conn.prepareStatement(updateSql);
                updateStmt.setString(1, newFirstName);
                updateStmt.setString(2, newLastName);
                updateStmt.setString(3, newAge);
                updateStmt.setString(4, newAddress);
                updateStmt.setInt(5, userId);

                int rowsUpdated = updateStmt.executeUpdate();

                if (rowsUpdated > 0) {
                    System.out.println(green + "User with ID " + userId + " has been updated.");
                } else {
                    System.out.println(red + "Failed to update user with ID " + userId);
                }
            } else {
                System.out.println(red + "User with ID " + userId + " not found. Update aborted.");
            }
        } catch (SQLException e) {
            System.out.println("Exception " + e);
        }
        System.out.println(" ");
        menu();
    }

    static void delete() {
        Scanner input = new Scanner(System.in);
        System.out.print(green + "Enter the ID of the user you want to delete: ");
        int userId = input.nextInt();

        try (Connection conn = DatabaseConnection.getConnection()) {
            String checkSql = "SELECT * FROM users WHERE id = ?";
            PreparedStatement checkStmt = conn.prepareStatement(checkSql);
            checkStmt.setInt(1, userId);

            ResultSet checkResult = checkStmt.executeQuery();

            if (checkResult.next()) {
                System.out.println(blue + "+----+------------+-----------+-----+----------------+");
                System.out.println(purple + "| ID | First Name | Last Name | Age | Address        |");
                System.out.println(blue + "+----+------------+-----------+-----+----------------+");

                int id = checkResult.getInt("id");
                String fname = checkResult.getString("fname");
                String lname = checkResult.getString("lname");
                String age = checkResult.getString("age");
                String address = checkResult.getString("address");

                System.out.format(blue + "| %-2d | %-10s | %-9s | %-3s | %-14s |%n", id, fname, lname, age, address);
                System.out.println(blue + "+----+------------+-----------+-----+----------------+");

                System.out.print(green + "Do you want to delete this user? (yes/no): ");
                String confirmation = input.next().toLowerCase();

                if (confirmation.equals("yes")) {
                    String deleteSql = "DELETE FROM users WHERE id = ?";
                    PreparedStatement deleteStmt = conn.prepareStatement(deleteSql);
                    deleteStmt.setInt(1, userId);

                    int rowsDeleted = deleteStmt.executeUpdate();

                    if (rowsDeleted > 0) {
                        System.out.println(yellow + "User with ID " + userId + " has been deleted.");
                    } else {
                        System.out.println(red + "Failed to delete user with ID " + userId);
                    }
                } else {
                    System.out.println(purple + "Deletion canceled.");
                }
            } else {
                System.out.println(red + "User with ID " + userId + " not found. Deletion aborted.");
            }
        } catch (SQLException e) {
            System.out.println("Exception " + e);
        }
        System.out.println(" ");
        menu();
    }

    private static String red = "\u001B[31m";
    private static String blue = "\u001B[34m";
    private static String purple = "\u001B[35m";
    private static String green = "\u001B[32m";
    private static String cyan = "\u001B[36m";
    private static String yellow = "\u001B[33m";

    private static String redB = "\u001B[41m";
    private static String blueB = "\u001B[44m";
    private static String purpleB = "\u001B[45m";
    private static String greenB = "\u001B[42m";
    private static String cyanB = "\u001B[46m";
    private static String yellowB = "\u001B[43m";
    private static String whiteB = "\u001B[47m";

    public static void main(String[] args) {
        menu();
    }
}
