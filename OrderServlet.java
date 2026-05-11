package cafe_res_mgt;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.*;
import javax.servlet.http.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;
import javax.servlet.annotation.WebServlet;

@WebServlet("/OrderServlet")

public class OrderServlet extends HttpServlet {

    private CafeRestaurantInterface service;

    @Override
    public void init() throws ServletException {
        try {
            Registry registry = LocateRegistry.getRegistry("127.0.0.1", 1099);
            service = (CafeRestaurantInterface) registry.lookup("CafeRestaurantService");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 👉 Place Order
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        try {
            String item = request.getParameter("item");
            int qty = Integer.parseInt(request.getParameter("quantity"));
            double price = Double.parseDouble(request.getParameter("price"));

            String result = service.placeOrder(item, qty, price);

            out.println("<h2>" + result + "</h2>");
            out.println("<a href='index.html'>Back</a>");

        } catch (Exception e) {
            out.println("<h3>Error: " + e.getMessage() + "</h3>");
        }
    }

 @Override
protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

    response.setContentType("text/html");
    PrintWriter out = response.getWriter();

    try {

        List<String> orders = service.getAllOrders();

        out.println("<html>");
        out.println("<head>");
        out.println("<title>All Orders</title>");

        out.println("<style>");
        out.println("body{font-family:Arial;background:#f4f4f4;padding:20px;}");
        out.println("table{border-collapse:collapse;width:80%;margin:auto;background:white;}");
        out.println("th,td{border:1px solid black;padding:10px;text-align:center;}");
        out.println("th{background:#333;color:white;}");
        out.println("h2{text-align:center;color:#333;}");
        out.println("</style>");

        out.println("</head>");
        out.println("<body>");

        out.println("<h2>All Orders</h2>");

        out.println("<table>");

        out.println("<tr>");
        out.println("<th>ID</th>");
        out.println("<th>Item</th>");
        out.println("<th>Quantity</th>");
        out.println("<th>Price</th>");
        out.println("<th>Total</th>");
        out.println("</tr>");

        for (String o : orders) {

            String[] parts = o.split("\\|");

            out.println("<tr>");

            for (String p : parts) {

                String value = p.substring(p.indexOf(":") + 1).trim();

                out.println("<td>" + value + "</td>");
            }

            out.println("</tr>");
        }

        out.println("</table>");

        out.println("<br><center>");
        out.println("<a href='index.html'>Back</a>");
        out.println("</center>");

        out.println("</body>");
        out.println("</html>");

    } catch (Exception e) {

        out.println("<h3>Error: " + e.getMessage() + "</h3>");
    }
}
}