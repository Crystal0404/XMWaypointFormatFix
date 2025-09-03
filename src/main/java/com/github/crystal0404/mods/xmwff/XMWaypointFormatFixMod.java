package com.github.crystal0404.mods.xmwff;

//? if fabric {
import net.fabricmc.api.ClientModInitializer;

public class XMWaypointFormatFixMod implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        XMWaypointFormatFixCommon.init();
    }
}
//?}

//? if neoforge {
/*import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.common.Mod;

@Mod(value = XMWaypointFormatFixCommon.MOD_ID, dist = Dist.CLIENT)
public class XMWaypointFormatFixMod {
    public XMWaypointFormatFixMod() {
        XMWaypointFormatFixCommon.init();
    }
}
*///?}
