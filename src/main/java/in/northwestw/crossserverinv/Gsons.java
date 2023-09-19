package in.northwestw.crossserverinv;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Gsons {
    public static final Gson CONCISE_GSON = new Gson(), PRETTY_GSON = new GsonBuilder().setPrettyPrinting().create();
}
