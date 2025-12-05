import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@WebServlet("/deleteBooking")
public class DeleteBookingServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String empId = request.getParameter("empId");
        String tripName = request.getParameter("tripName");
        String bookingType = request.getParameter("bookingType");
        String bookingOption = request.getParameter("bookingOption");
        String fromPlace = request.getParameter("fromPlace");
        String toPlace = request.getParameter("toPlace");
        String bookingTime = request.getParameter("bookingTime");

        System.out.println("Received parameters:");
        System.out.println("empId: " + empId);
        System.out.println("tripName: " + tripName);
        System.out.println("bookingType: " + bookingType);
        System.out.println("bookingOption: " + bookingOption);
        System.out.println("fromPlace: " + fromPlace);
        System.out.println("toPlace: " + toPlace);
        System.out.println("bookingTime: " + bookingTime);

        Connection connection = null;
        PreparedStatement deleteBookingStmt = null;
        PreparedStatement deleteTripStmt = null;

        try {
            connection = DB.getInstance().getConnection();
            connection.setAutoCommit(false);

            String deleteBookingSql = "DELETE FROM bookings WHERE empId = ? AND tripName = ? AND bookingType = ? AND selectedOption = ? AND fromPlace = ? AND toPlace = ? AND bookingTime = ?";
            deleteBookingStmt = connection.prepareStatement(deleteBookingSql);
            deleteBookingStmt.setString(1, empId);
            deleteBookingStmt.setString(2, tripName);
            deleteBookingStmt.setString(3, bookingType);
            deleteBookingStmt.setString(4, bookingOption);
            deleteBookingStmt.setString(5, fromPlace);
            deleteBookingStmt.setString(6, toPlace);
            deleteBookingStmt.setString(7, bookingTime);

            int bookingRowsDeleted = deleteBookingStmt.executeUpdate();

            String deleteTripSql = "DELETE FROM trips WHERE emp_id = ? AND trip_name = ?";
            deleteTripStmt = connection.prepareStatement(deleteTripSql);
            deleteTripStmt.setString(1, empId);
            deleteTripStmt.setString(2, tripName);

            int tripRowsDeleted = deleteTripStmt.executeUpdate();

            if (bookingRowsDeleted > 0 && tripRowsDeleted > 0) {
                connection.commit();
                response.getWriter().write("Booking and trip deleted successfully!");
            } else {
                connection.rollback();
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().write("Failed to delete booking and/or trip.");
            }
        } catch (SQLException e) {
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("Database error: " + e.getMessage());
        } finally {
            if (deleteBookingStmt != null) {
                try {
                    deleteBookingStmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (deleteTripStmt != null) {
                try {
                    deleteTripStmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                try {
                    connection.setAutoCommit(true);
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
