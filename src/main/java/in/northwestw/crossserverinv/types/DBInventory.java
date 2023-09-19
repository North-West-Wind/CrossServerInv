package in.northwestw.crossserverinv.types;

import com.google.common.collect.ImmutableList;

import java.util.List;

public class DBInventory {
    private final List<DBItem> items, armor;
    private final DBItem offhand;
    private final int totalExperience;

    public DBInventory(List<DBItem> items, List<DBItem> armor, DBItem offhand, int totalExperience) {
        this.items = ImmutableList.copyOf(items);
        this.armor = ImmutableList.copyOf(armor);
        this.offhand = offhand;
        this.totalExperience = totalExperience;
    }

    public List<DBItem> getItems() {
        return items;
    }

    public List<DBItem> getArmor() {
        return armor;
    }

    public DBItem getOffhand() {
        return offhand;
    }

    public int getTotalExperience() {
        return totalExperience;
    }
}
