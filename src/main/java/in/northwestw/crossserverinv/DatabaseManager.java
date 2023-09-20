package in.northwestw.crossserverinv;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import in.northwestw.crossserverinv.types.DBInventory;
import in.northwestw.crossserverinv.types.DBItem;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.sql.*;
import java.util.UUID;

public class DatabaseManager {
    private static final Gson GSON = Gsons.CONCISE_GSON;
    private static String JDBC_URL;

    public static void init() {
        if (Config.isEnabled()) {
            JDBC_URL = "jdbc:mysql://" + Config.getMysqlAddress() + ":" + Config.getMysqlPort() + "/" + Config.getDatabaseName();
            if (!setupDatabase()) JDBC_URL = null;
        } else JDBC_URL = null;
        CrossServerInv.LOGGER.info("CrossServerInv enabled: {}", JDBC_URL != null);
    }

    private static boolean setupDatabase() {
        try (Connection conn = DriverManager.getConnection(JDBC_URL, Config.getMysqlUsername(), Config.getMysqlPassword())) {
            PreparedStatement stmt = conn.prepareStatement("SELECT count(*) FROM information_schema.tables WHERE table_schema = ? AND table_name = ?");
            stmt.setString(1, Config.getDatabaseName());
            stmt.setString(2, "players");
            ResultSet rs = stmt.executeQuery();
            rs.next();
            if (rs.getInt(1) == 1) {
                CrossServerInv.LOGGER.info("Database is setup already.");
                return true;
            }
            CrossServerInv.LOGGER.info("Players table doesn't exist in database. Creating one...");
            stmt = conn.prepareStatement("CREATE TABLE players (uuid CHAR(36) PRIMARY KEY, inventory JSON, xp INT)");
            return stmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void setPlayer(Player player) {
        if (JDBC_URL == null) return;
        try (Connection conn = DriverManager.getConnection(JDBC_URL, Config.getMysqlUsername(), Config.getMysqlPassword())) {
            // Create inventory json for db
            JsonObject json = new JsonObject();
            Inventory inv = player.getInventory();
            JsonArray items = new JsonArray();
            for (ItemStack stack : inv.items) {
                JsonObject itemJson = new JsonObject();
                DBItem item = new DBItem(stack);
                itemJson.addProperty("resourceLocation", item.getResourceLocation());
                itemJson.addProperty("count", item.getCount());
                itemJson.addProperty("snbt", item.getSNBT());
                items.add(itemJson);
            }
            json.add("items", items);
            JsonArray armor = new JsonArray();
            for (ItemStack stack : inv.items) {
                JsonObject armorJson = new JsonObject();
                DBItem item = new DBItem(stack);
                armorJson.addProperty("resourceLocation", item.getResourceLocation());
                armorJson.addProperty("count", item.getCount());
                armorJson.addProperty("snbt", item.getSNBT());
                armor.add(armorJson);
            }
            json.add("armor", armor);
            JsonObject offhandJson = new JsonObject();
            DBItem item = new DBItem(inv.offhand.get(0));
            offhandJson.addProperty("resourceLocation", item.getResourceLocation());
            offhandJson.addProperty("count", item.getCount());
            offhandJson.addProperty("snbt", item.getSNBT());
            json.add("offhand", offhandJson);
            String jsonStr = GSON.toJson(json);
            // Prepare statement to check if row exists
            PreparedStatement stmt = conn.prepareStatement("SELECT uuid FROM players WHERE uuid = ?");
            stmt.setString(1, player.getUUID().toString());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                // Row exists
                stmt = conn.prepareStatement("UPDATE players SET inventory = ?, xp = ? WHERE uuid = ?");
                stmt.setString(1, jsonStr);
                stmt.setLong(2, player.experienceLevel);
                stmt.setString(3, player.getUUID().toString());
            } else {
                // Row doesn't exist
                stmt = conn.prepareStatement("INSERT INTO players VALUES (?, ?, ?)");
                stmt.setString(1, player.getUUID().toString());
                stmt.setString(2, jsonStr);
                stmt.setInt(3, player.experienceLevel);
            }
            stmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static DBInventory getPlayer(UUID uuid) {
        if (JDBC_URL == null) return null;
        try (Connection conn = DriverManager.getConnection(JDBC_URL, Config.getMysqlUsername(), Config.getMysqlPassword())) {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM players WHERE uuid = ?");
            stmt.setString(1, uuid.toString());
            ResultSet rs = stmt.executeQuery();
            // Read 1 row only
            if (rs.next()) {
                String invJsonStr = rs.getString("inventory");
                JsonObject json = GSON.fromJson(invJsonStr, JsonObject.class);
                return new DBInventory(
                        json.getAsJsonArray("items").asList().stream().map(el -> GSON.fromJson(el, DBItem.class)).toList(),
                        json.getAsJsonArray("armor").asList().stream().map(el -> GSON.fromJson(el, DBItem.class)).toList(),
                        GSON.fromJson(json.getAsJsonObject("offhand"), DBItem.class),
                        rs.getInt("xp")
                        );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
