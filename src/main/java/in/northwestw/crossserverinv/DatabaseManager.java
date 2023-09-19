package in.northwestw.crossserverinv;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.UUID;

public class DatabaseManager {
    private static final String JDBC_URL;

    static {
        JDBC_URL = "jdbc:mysql://" + Config.getMysqlAddress() + "/" + Config.getDatabaseName();
    }

    public static void getPlayer(UUID uuid) {
        try (Connection conn = DriverManager.getConnection(JDBC_URL, Config.getMysqlUsername(), Config.getMysqlPassword())) {

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
