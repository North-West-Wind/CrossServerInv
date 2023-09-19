package in.northwestw.crossserverinv;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

import java.io.*;

public class Config {
    private static final File CONFIG_FILE = new File("config/csi.json");
    private static String MYSQL_ADDRESS, MYSQL_USERNAME, MYSQL_PASSWORD, DATABASE_NAME;
    private static int MYSQL_PORT;

    static {
        try {
            if (!CONFIG_FILE.exists()) {
                MYSQL_ADDRESS = "";
                MYSQL_PORT = 3306;
                MYSQL_USERNAME = "";
                MYSQL_PASSWORD = "";
                DATABASE_NAME = "";
                JsonObject json = new JsonObject();
                json.addProperty("mysql_address", MYSQL_ADDRESS);
                json.addProperty("mysql_port", MYSQL_PORT);
                json.addProperty("mysql_username", MYSQL_USERNAME);
                json.addProperty("mysql_password", MYSQL_PASSWORD);
                json.addProperty("db_name", DATABASE_NAME);
                CrossServerInv.GSON.toJson(json, new FileWriter(CONFIG_FILE));
            } else {
                JsonReader reader = new JsonReader(new FileReader(CONFIG_FILE));
                JsonObject json = CrossServerInv.GSON.fromJson(reader, JsonObject.class);
                MYSQL_ADDRESS = json.get("mysql_address").getAsString();
                MYSQL_PORT = json.get("mysql_port").getAsInt();
                MYSQL_USERNAME = json.get("mysql_username").getAsString();
                MYSQL_PASSWORD = json.get("mysql_password").getAsString();
                DATABASE_NAME = json.get("db_name").getAsString();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean isEnabled() {
        return MYSQL_ADDRESS.isEmpty() || MYSQL_PORT < 0 || MYSQL_PORT > 65535 || MYSQL_USERNAME.isEmpty() || MYSQL_PASSWORD.isEmpty() || DATABASE_NAME.isEmpty();
    }

    public static String getMysqlAddress() {
        return MYSQL_ADDRESS;
    }

    public static int getMysqlPort() {
        return MYSQL_PORT;
    }

    public static String getMysqlUsername() {
        return MYSQL_USERNAME;
    }

    public static String getMysqlPassword() {
        return MYSQL_PASSWORD;
    }

    public static String getDatabaseName() {
        return DATABASE_NAME;
    }
}
