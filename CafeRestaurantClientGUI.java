package cafe_res_mgt;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;

public class CafeRestaurantClientGUI extends JFrame implements ActionListener {

    JTextField itemField, qtyField, priceField;
    JButton orderButton, viewButton;
    JTextArea outputArea;

    CafeRestaurantInterface service;

    public CafeRestaurantClientGUI() {
        ImageIcon icon = new ImageIcon("C:\\Users\\hp\\Documents\\NetBeansProjects\\SimpleCafeAndResMngtSystem\\src\\cafe_res_mgt\\cumbo.jpg");
        setIconImage(icon.getImage());
        
        setTitle("Cafe & Restaurant System");
        setSize(450, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        JLabel title = new JLabel("Cafe & Restaurant Management", SwingConstants.CENTER);
        title.setBounds(50, 10, 350, 30);
        title.setFont(new Font("Arial", Font.BOLD, 14));
        add(title);

        JLabel itemLabel = new JLabel("Item:");
        itemLabel.setBounds(30, 60, 100, 25);
        add(itemLabel);

        itemField = new JTextField();
        itemField.setBounds(140, 60, 200, 25);
        add(itemField);

        JLabel qtyLabel = new JLabel("Quantity:");
        qtyLabel.setBounds(30, 100, 100, 25);
        add(qtyLabel);

        qtyField = new JTextField();
        qtyField.setBounds(140, 100, 200, 25);
        add(qtyField);

        JLabel priceLabel = new JLabel("Price:");
        priceLabel.setBounds(30, 140, 100, 25);
        add(priceLabel);

        priceField = new JTextField();
        priceField.setBounds(140, 140, 200, 25);
        add(priceField);

        orderButton = new JButton("Place Order");
        orderButton.setBounds(60, 190, 140, 30);
        orderButton.addActionListener(this);
        add(orderButton);

        viewButton = new JButton("View Orders");
        viewButton.setBounds(210, 190, 140, 30);
        viewButton.addActionListener(this);
        add(viewButton);

        outputArea = new JTextArea();
        outputArea.setEditable(false);

        JScrollPane sp = new JScrollPane(outputArea);
        sp.setBounds(30, 240, 360, 180);
        add(sp);

        connectToServer();

        setVisible(true);
    }

    private void connectToServer() {
        try {
            Registry registry = LocateRegistry.getRegistry("127.0.0.1", 1099);
            service = (CafeRestaurantInterface) registry.lookup("CafeRestaurantService");
            System.out.println("Connected to server!");
        } catch (RemoteException | NotBoundException e) {
            JOptionPane.showMessageDialog(this,
                    "Server not available!\nStart server first.");
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (service == null) {
            JOptionPane.showMessageDialog(this, "No server connection!");
            return;
        }

        if (e.getSource() == orderButton) {
            placeOrder();
        } else {
            viewOrders();
        }
    }

    private void placeOrder() {
        try {
            String item = itemField.getText();

            if (item.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Enter item name!");
                return;
            }

            int qty = Integer.parseInt(qtyField.getText());
            double price = Double.parseDouble(priceField.getText());

            if (qty <= 0 || price <= 0) {
                JOptionPane.showMessageDialog(this, "Invalid input!");
                return;
            }

            String result = service.placeOrder(item, qty, price);
            JOptionPane.showMessageDialog(this, result);

            itemField.setText("");
            qtyField.setText("");
            priceField.setText("");

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Enter valid numbers!");
        } catch (RemoteException ex) {
            JOptionPane.showMessageDialog(this, "Server error!");
        }
    }

    private void viewOrders() {
        try {
            List<String> orders = service.getAllOrders();
            outputArea.setText("");

            for (String o : orders) {
                outputArea.append(o + "\n");
            }

        } catch (RemoteException ex) {
            outputArea.setText("Failed to load orders.");
        }
    }

    public static void main(String[] args) {
        new CafeRestaurantClientGUI();
    }
}