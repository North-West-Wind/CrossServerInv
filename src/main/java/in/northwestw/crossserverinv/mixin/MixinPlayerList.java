package in.northwestw.crossserverinv.mixin;

import in.northwestw.crossserverinv.DatabaseManager;
import in.northwestw.crossserverinv.types.DBInventory;
import net.minecraft.network.Connection;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerList.class)
public abstract class MixinPlayerList {
    @Inject(at = @At("TAIL"), method = "placeNewPlayer")
    public void placeNewPlayer(Connection connection, ServerPlayer serverPlayer, CallbackInfo ci) {
        DBInventory inv = DatabaseManager.getPlayer(serverPlayer.getUUID());
        if (inv == null) return;
        for (int ii = 0; ii < inv.getItems().size(); ii++) serverPlayer.getInventory().setItem(ii, inv.getItems().get(ii).getItemStack());
        for (int ii = 0; ii < inv.getArmor().size(); ii++) serverPlayer.getInventory().setItem(36 + ii, inv.getArmor().get(ii).getItemStack());
        serverPlayer.getInventory().setItem(40, inv.getOffhand().getItemStack());
        serverPlayer.setExperienceLevels(inv.getXp());
    }
}
