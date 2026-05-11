package cafe_res_mgt;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface CafeRestaurantInterface extends Remote {

    // Place new order
    String placeOrder(String itemName, int quantity, double price) throws RemoteException;

    // Get all orders
    List<String> getAllOrders() throws RemoteException;

}