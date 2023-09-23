package in.northwestw.crossserverinv.types;

import in.northwestw.crossserverinv.NBTSerializer;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class DBItem {
    private final String resourceLocation;
    private final int count;
    private final String snbt;

    public DBItem(ItemStack stack) {
        if (stack == null || stack.isEmpty()) {
            this.resourceLocation = "";
            this.count = 0;
            this.snbt = "";
        } else {
            ResourceLocation rl = BuiltInRegistries.ITEM.getKey(stack.getItem());
            this.resourceLocation = rl.getNamespace() + ":" + rl.getPath();
            this.count = stack.getCount();
            if (stack.hasTag()) this.snbt = NBTSerializer.nbtToString(stack.getTag());
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
        if (!this.snbt.isEmpty()) stack.setTag((CompoundTag) NBTSerializer.stringToNbt(this.snbt));
        return stack;
    }
}
