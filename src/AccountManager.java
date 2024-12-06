public class AccountManager {
    private final DatabaseManager dbManager;

    public AccountManager() {
        dbManager = new DatabaseManager();
    }

    public boolean register(String name, String email, String password) {
        return dbManager.registerCustomer(name, email, password);
    }

    public boolean login(String email, String password) {
        return dbManager.verifyCustomer(email, password);
    }
}
