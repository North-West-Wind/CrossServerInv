package in.northwestw.crossserverinv.types;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
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
            else this.snbt = "";
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

    public ItemStack getItemStack() {
        if (this.resourceLocation.isEmpty() || this.count == 0) return ItemStack.EMPTY;
        Item item = BuiltInRegistries.ITEM.get(new ResourceLocation(this.resourceLocation));
        ItemStack stack = new ItemStack(item, this.count);
        CompoundTag nbt = new CompoundTag();
        
        stack.setTag();
    }
}
