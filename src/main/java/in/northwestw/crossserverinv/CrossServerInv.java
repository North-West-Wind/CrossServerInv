package in.northwestw.crossserverinv;

import com.google.gson.Gson;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class CrossServerInv implements ModInitializer {
    public static final String MOD_ID = "crossserverinv";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        Config.init();
        DatabaseManager.init();
    }
}
