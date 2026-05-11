package cafe_res_mgt;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CafeRestaurantImpl extends UnicastRemoteObject implements CafeRestaurantInterface {

    public CafeRestaurantImpl() throws RemoteException {
        super();
    }

    @Override
    public String placeOrder(String itemName, int quantity, double price) throws RemoteException {

        // ✅ validation
        if (itemName == null || itemName.isEmpty()) {
            return "Item name is required!";
        }
        if (quantity <= 0 || price <= 0) {
            return "Quantity and price must be positive!";
        }

        double total = quantity * price;

        String sql = "INSERT INTO orders(item_name, quantity, price, total) VALUES (?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, itemName);
            ps.setInt(2, quantity);
            ps.setDouble(3, price);
            ps.setDouble(4, total);

            int rows = ps.executeUpdate();

            if (rows > 0) {
                return "Order placed successfully. Total = " + total;
            } else {
                return "Order not saved.";
            }

        } catch (SQLException e) {
            e.printStackTrace(); // debug
            return "Failed to place order. Please try again.";
        }
    }

    @Override
    public List<String> getAllOrders() throws RemoteException {

        List<String> orders = new ArrayList<>();

        String sql = "SELECT * FROM orders";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String order =
                        //"-----------------------------\n" +
                        "ID: " + rs.getInt("id") + "\n" +
                        "Item: " + rs.getString("item_name") + "\n" +
                        "Quantity: " + rs.getInt("quantity") + "\n" +
                        "Price: " + rs.getDouble("price") + "\n" +
                        "Total: " + rs.getDouble("total") + "\n";

                orders.add(order);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            orders.add("Failed to retrieve orders.");
        }

        return orders;
    }
}