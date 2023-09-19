package in.northwestw.crossserverinv;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.nbt.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class NBTSerializer {
    private static final Gson GSON = Gsons.CONCISE_GSON;

    private static JsonObject nbtToJson(Tag nbt) {
        byte type = nbt.getId();
        JsonObject obj = new JsonObject();
        obj.addProperty("type", type);
        switch (type) {
            case Tag.TAG_BYTE -> obj.addProperty("value", ((ByteTag) nbt).getAsByte());
            case Tag.TAG_SHORT -> obj.addProperty("value", ((ShortTag) nbt).getAsShort());
            case Tag.TAG_INT -> obj.addProperty("value", ((IntTag) nbt).getAsInt());
            case Tag.TAG_LONG -> obj.addProperty("value", ((LongTag) nbt).getAsLong());
            case Tag.TAG_FLOAT -> obj.addProperty("value", ((FloatTag) nbt).getAsFloat());
            case Tag.TAG_DOUBLE -> obj.addProperty("value", ((DoubleTag) nbt).getAsDouble());
            case Tag.TAG_BYTE_ARRAY -> {
                JsonArray array = new JsonArray();
                byte[] bytes = ((ByteArrayTag) nbt).getAsByteArray();
                for (byte b : bytes) array.add(b);
                obj.add("value", array);
            }
            case Tag.TAG_STRING -> obj.addProperty("value", nbt.getAsString());
            case Tag.TAG_LIST -> {
                JsonArray array = new JsonArray();
                ListTag list = (ListTag) nbt;
                for (int ii = 0; ii < list.size(); ii++) array.add(nbtToJson(list.get(ii)));
                obj.add("value", array);
            }
            case Tag.TAG_COMPOUND -> {
                JsonObject values = new JsonObject();
                CompoundTag compound = (CompoundTag) nbt;
                for (String key : compound.getAllKeys()) {
                    Tag tag = Objects.requireNonNull(compound.get(key));
                    JsonObject val = nbtToJson(tag);
                    values.add(key, val);
                }
                obj.add("value", values);
            }
            case Tag.TAG_INT_ARRAY -> {
                JsonArray array = new JsonArray();
                int[] ints = ((IntArrayTag) nbt).getAsIntArray();
                for (int i : ints) array.add(i);
                obj.add("value", array);
            }
            case Tag.TAG_LONG_ARRAY -> {
                JsonArray array = new JsonArray();
                long[] longs = ((LongArrayTag) nbt).getAsLongArray();
                for (long l : longs) array.add(l);
                obj.add("value", array);
            }
        }
        return obj;
    }

    public static String nbtToString(Tag nbt) {
        return GSON.toJson(nbtToJson(nbt));
    }

    private static Tag jsonToNbt(JsonObject json) {
        Tag tag = null;
        JsonElement value = json.get("value");
        switch (json.get("type").getAsByte()) {
            case Tag.TAG_BYTE -> tag = ByteTag.valueOf(value.getAsByte());
            case Tag.TAG_SHORT -> tag = ShortTag.valueOf(value.getAsShort());
            case Tag.TAG_INT -> tag = IntTag.valueOf(value.getAsInt());
            case Tag.TAG_LONG -> tag = LongTag.valueOf(value.getAsLong());
            case Tag.TAG_FLOAT -> tag = FloatTag.valueOf(value.getAsFloat());
            case Tag.TAG_DOUBLE -> tag = DoubleTag.valueOf(value.getAsDouble());
            case Tag.TAG_BYTE_ARRAY -> {
                JsonArray array = value.getAsJsonArray();
                List<JsonElement> list = new ArrayList<>();
                for (JsonElement el : array) list.add(el);
                tag = new ByteArrayTag(list.stream().map(JsonElement::getAsByte).toList());
            }
            case Tag.TAG_STRING -> tag = StringTag.valueOf(value.getAsString());
            case Tag.TAG_LIST -> {
                JsonArray array = value.getAsJsonArray();
                ListTag list = new ListTag();
                for (JsonElement el : array) list.add(jsonToNbt(el.getAsJsonObject()));
                tag = list;
            }
            case Tag.TAG_COMPOUND -> {
                JsonObject values = value.getAsJsonObject();
                CompoundTag compound = new CompoundTag();
                for (String key : values.keySet()) compound.put(key, jsonToNbt(values.getAsJsonObject(key)));
                tag = compound;
            }
            case Tag.TAG_INT_ARRAY -> {
                JsonArray array = value.getAsJsonArray();
                List<JsonElement> list = new ArrayList<>();
                for (JsonElement el : array) list.add(el);
                tag = new IntArrayTag(list.stream().map(JsonElement::getAsInt).toList());
            }
            case Tag.TAG_LONG_ARRAY -> {
                JsonArray array = value.getAsJsonArray();
                List<JsonElement> list = new ArrayList<>();
                for (JsonElement el : array) list.add(el);
                tag = new LongArrayTag(list.stream().map(JsonElement::getAsLong).toList());
            }
        }
        return tag;
    }

    public static Tag stringToNbt(String str) {
        return jsonToNbt(GSON.fromJson(str, JsonObject.class));
    }
}
