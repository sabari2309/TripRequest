import java.io.*;
import java.sql.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/CreateTripServlet")
public class CreateTripServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/plain");
        PrintWriter out = response.getWriter();

        String empId = request.getParameter("empId");
        String empName = request.getParameter("empName");
        String tripName = request.getParameter("tripName");
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        String tripType = request.getParameter("tripType");

        System.out.println("Received parameters:");
        System.out.println("empId: " + empId);
        System.out.println("empName: " + empName);
        System.out.println("tripName: " + tripName);
        System.out.println("startDate: " + startDate);
        System.out.println("endDate: " + endDate);
        System.out.println("tripType: " + tripType);

        try {
            java.sql.Date.valueOf(startDate);
            java.sql.Date.valueOf(endDate);
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid date format");
            out.println("Invalid date format");
            return;
        }

        try (Connection conn = DB.getInstance().getConnection()) {
            String check = "SELECT COUNT(*) FROM trips WHERE trip_name = ? AND from_date = ? AND to_date = ? ";
            try (PreparedStatement checkStmt = conn.prepareStatement(check)) {
                
                checkStmt.setString(1, tripName);
                checkStmt.setDate(2, java.sql.Date.valueOf(startDate));
                checkStmt.setDate(3, java.sql.Date.valueOf(endDate));
                //checkStmt.setString(4, tripType);
                try (ResultSet rs = checkStmt.executeQuery()) {
                    if (rs.next() && rs.getInt(1) > 0) {
                        out.println("Invalid");
                        return;
                    }
                }
            }

       
            String sql = "INSERT INTO trips (emp_id, emp_name, trip_name, from_date, to_date, trip_type) VALUES (?, ?, ?, ?, ?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, empId);
                pstmt.setString(2, empName);
                pstmt.setString(3, tripName);
                pstmt.setDate(4, java.sql.Date.valueOf(startDate));
                pstmt.setDate(5, java.sql.Date.valueOf(endDate));
                pstmt.setString(6, tripType);

                int rowsAffected = pstmt.executeUpdate();
                if (rowsAffected > 0) {
                    out.println("true");
                } else {
                    out.println("false");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
