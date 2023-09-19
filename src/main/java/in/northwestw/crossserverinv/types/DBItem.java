package in.northwestw.crossserverinv.types;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.ItemStack;

public class DBItem {
    private String resourceLocation;
    private int count;
    private String snbt;

    public DBItem(ItemStack stack) {
        if (stack == null || stack.isEmpty()) {
            this.resourceLocation = "";
            this.count = 0;
            this.snbt = "";
        } else {
            this.resourceLocation = BuiltInRegistries.ITEM.getKey(stack.getItem()).toString();
            this.count = stack.getCount();
            if (stack.hasTag()) this.snbt = stack.getTag().toString();
            else this.snbt = null;
        }
    }

    public String getResourceLocation() {
        return resourceLocation;
    }

    public int getCount() {
        return count;
    }

    public String getSNBT() {
        return snbt;
    }
}
