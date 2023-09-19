package in.northwestw.crossserverinv.mixin;

import in.northwestw.crossserverinv.DatabaseManager;
import in.northwestw.crossserverinv.types.DBInventory;
import in.northwestw.crossserverinv.types.DBItem;
import net.minecraft.network.Connection;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;

@Mixin(PlayerList.class)
public abstract class MixinPlayerList {

    @Inject(at = @At("TAIL"), method = "placeNewPlayer")
    public void placeNewPlayer(Connection connection, ServerPlayer serverPlayer, CallbackInfo ci) {
        DBInventory inv = DatabaseManager.getPlayer(serverPlayer.getUUID());
        if (inv == null) return;
        for (int ii = 0; ii < inv.getItems().size(); ii++) {
            DBItem item = inv.getItems().get(ii);
            serverPlayer.getInventory().setItem(ii, item.getItemStack());
        }
    }
}
