package crystal0404.xmwff;

import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XMWaypointFormatFix implements ModInitializer {
	public static final String MOD_ID = "xmwff";
	public static final String MOD_NAME = "XMWaypointFormatFix";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);

	@Override
	public void onInitialize() {
		LOGGER.info("XMWaypointFormatFix!");
	}
}