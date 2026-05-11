package cafe_res_mgt;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static final String URL = "jdbc:mysql://127.0.0.1:3306/cafe_res_db";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public static Connection getConnection() {
        try {
            Connection con = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Database connected successfully!");
            return con;

        } catch (SQLException e) {
            System.out.println("Database connection failed!");
            e.printStackTrace(); // VERY IMPORTANT 🔥
            return null;
        }
    }
}