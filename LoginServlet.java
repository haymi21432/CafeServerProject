
package cafe_res_mgt;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;

public class LoginServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String user = request.getParameter("username");
        String pass = request.getParameter("password");

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        try (Connection con = DBConnection.getConnection()) {

            String sql = "SELECT * FROM users WHERE username=? AND password=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, user);
            ps.setString(2, pass);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                // login success
                HttpSession session = request.getSession();
                session.setAttribute("user", user);

                response.sendRedirect("index.html"); // gara order page
            } else {
                out.println("<h3>Invalid username or password</h3>");
                out.println("<a href='login.html'>Try again</a>");
            }

        } catch (Exception e) {
            out.println("Error: " + e.getMessage());
        }
    }
}