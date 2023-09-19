package in.northwestw.crossserverinv.mixin;

import in.northwestw.crossserverinv.DatabaseManager;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayer.class)
public class MixinServerPlayer {
    @Inject(at = @At("TAIL"), method = "disconnect")
    public void disconnect(CallbackInfo ci) {
        DatabaseManager.setPlayer((ServerPlayer) (Object) this);
    }
}
