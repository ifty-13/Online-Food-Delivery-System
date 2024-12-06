import java.sql.*;

public class DatabaseManager {
    private static final String DB_URL = "jdbc:sqlite:accounts.db";

    public DatabaseManager() {
        createAccountTable();
    }

private void createAccountTable() {
    try (Connection conn = DriverManager.getConnection(DB_URL);
         Statement stmt = conn.createStatement()) {
        String sql = "CREATE TABLE IF NOT EXISTS accounts (" +
                     "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                     "name TEXT NOT NULL, " +
                     "email TEXT NOT NULL UNIQUE, " +
                     "password TEXT NOT NULL);";
        stmt.execute(sql);
    } catch (SQLException e) {
        e.printStackTrace();
    }
}




    public boolean registerCustomer(String name, String email, String password) {
        String sql = "INSERT INTO accounts (name, email, password) VALUES (?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setString(2, email);
            pstmt.setString(3, password);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            if (e.getMessage().contains("UNIQUE")) {
                System.out.println("Email already exists.");
            } else {
                e.printStackTrace();
            }
            return false;
        }
    }

    public boolean verifyCustomer(String email, String password) {
        String sql = "SELECT * FROM accounts WHERE email = ? AND password = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}
