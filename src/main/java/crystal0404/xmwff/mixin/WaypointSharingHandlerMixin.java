package crystal0404.xmwff.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import xaero.hud.minimap.waypoint.WaypointSharingHandler;

@Mixin(WaypointSharingHandler.class)
public abstract class WaypointSharingHandlerMixin {
    @ModifyVariable(
            method = "getReceivedDestinationWorld",
            at = @At("HEAD"),
            ordinal = 0,
            argsOnly = true,
            remap = false
    )
    private String getReceivedDestinationWorldMixin(String value) {
        if (value.startsWith("Internal_")) {
            return value.replaceAll("_", "-");
        } else {
            return value;
        }
    }
}
