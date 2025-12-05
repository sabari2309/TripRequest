import java.io.*;
import java.sql.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/login")
public class Login extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/plain");
        PrintWriter out = response.getWriter();
        String empId = request.getParameter("empId");
        String empName = request.getParameter("empName");
        
        try (Connection conn = DB.getInstance().getConnection()) {
            String sql = "SELECT COUNT(*) FROM employees WHERE emp_id = ? AND emp_name = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, empId);
                pstmt.setString(2, empName);
                
                try (ResultSet rs = pstmt.executeQuery()) {
                    rs.next();  
                    int count = rs.getInt(1);
                    if (count > 0) {
                        out.println("true");
                    } else {
                        out.println("false");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.println("false");
        }
    }
}
