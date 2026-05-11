package cafe_res_mgt;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class CafeRestaurantServer {

    public static void main(String[] args) {
        try {
            CafeRestaurantImpl obj = new CafeRestaurantImpl();

            // Create registry only if not exists
            Registry registry;
            try {
                registry = LocateRegistry.getRegistry(1099);
                registry.list(); // test if running
            } catch (RemoteException e) {
                registry = LocateRegistry.createRegistry(1099);
                System.out.println("RMI Registry created on port 1099");
            }

            registry.rebind("CafeRestaurantService", obj);

            System.out.println("=================================");
            System.out.println(" Server is running successfully ");
            System.out.println(" Service: CafeRestaurantService ");
            System.out.println(" Port: 1099 ");
            System.out.println("=================================");

        } catch (RemoteException e) {
            System.out.println("Server failed to start!");
            e.printStackTrace();
        }
    }
}