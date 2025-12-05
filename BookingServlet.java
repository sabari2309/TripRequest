import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@WebServlet("/bookingServlet")
public class BookingServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
         response.setContentType("text/plain");
        PrintWriter out = response.getWriter();

        String empId = request.getParameter("empId");
        String empName = request.getParameter("empName");
        String tripName = request.getParameter("tripName");
        String bookingType = request.getParameter("bookingType");
        String selectedOption = request.getParameter("bookingOption");
        String fromPlace = request.getParameter("fromPlace");
        String toPlace = request.getParameter("toPlace");
        String bookingTime = request.getParameter("bookingTime");
        System.out.println("Received parameters:");
        System.out.println("empId: " + empId);
        System.out.println("empName: " + empName);
        System.out.println("tripName: " + tripName);
        System.out.println("bookingType: " + bookingType);
        System.out.println("selectedOption: " + selectedOption);
        System.out.println("fromPlace: " + fromPlace);
        System.out.println("toPlace: " + toPlace);
        System.out.println("bookingTime: " + bookingTime);
        try (Connection connection = DB.getInstance().getConnection()) {
            String sql = "INSERT INTO bookings (empId, empName, tripName, bookingType, selectedOption, fromPlace, toPlace, bookingTime) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, empId);
                statement.setString(2, empName);
                statement.setString(3, tripName);
                statement.setString(4, bookingType);
                statement.setString(5, selectedOption);
                statement.setString(6, fromPlace);
                statement.setString(7, toPlace);
                statement.setString(8, bookingTime);

                int rowsInserted = statement.executeUpdate();
                if (rowsInserted > 0) {
                    out.println("true");
                    return;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
    }

    
}
