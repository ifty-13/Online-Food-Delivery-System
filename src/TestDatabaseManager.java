public class TestDatabaseManager {
    public static void main(String[] args) {
        DatabaseManager dbManager = new DatabaseManager();  // Initialize DatabaseManager

        // Test registering a new customer
        String name = "John Doe";
        String email = "john.doe@example.com";
        String password = "password123";

        boolean isRegistered = dbManager.registerCustomer(name, email, password);
        System.out.println("Customer registration successful: " + isRegistered);

        // Test verifying the customer
        boolean isVerified = dbManager.verifyCustomer(email, password);
        System.out.println("Customer verified: " + isVerified);
    }
}
