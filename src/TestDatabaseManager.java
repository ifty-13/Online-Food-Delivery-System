
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class TestDatabaseManager {
public static void main(String[] args) {
    String dbUrl = "jdbc:sqlite:accounts.db"; // Adjust path if needed
    try (Connection conn = DriverManager.getConnection(dbUrl)) {
        if (conn != null) {
            System.out.println("Connection established.");
        } else {
            System.out.println("Connection failed.");
        }
    } catch (SQLException e) {
        System.out.println("Error while connecting: " + e.getMessage());
        e.printStackTrace();
    }
}

}
