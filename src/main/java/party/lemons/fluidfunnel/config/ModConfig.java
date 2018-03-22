package party.lemons.fluidfunnel.config;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Created by Sam on 19/03/2018.
 */
@Mod.EventBusSubscriber
public class ModConfig
{
	@SubscribeEvent
	public static void onConfigChangedEvent(ConfigChangedEvent.OnConfigChangedEvent event)
	{
		if (event.getModID().equals(ModConstants.MODID))
		{
			ConfigManager.sync(ModConstants.MODID, Config.Type.INSTANCE);
		}
	}

	@Config(modid = ModConstants.MODID)
	public static class GameplayConfig
	{
		@Config.RequiresWorldRestart
		@Config.RangeInt(min = 1)
		public static int funnelCapacity = 1000;
		@Config.RangeInt(min = 0)
		public static int funnelTransferMax = 100;
		@Config.RangeInt(min = 0)
		public static int funnelCooldown = 15;

		public static boolean placeInWorld = true;
		public static boolean takeFromWorld = true;
		public static boolean fillCauldron = true;
		public static boolean fillContainers = true;

		@Config.RequiresWorldRestart
		@Config.RangeInt(min = 1)
		public static int tankCapacity = 10000;

		@Config.RangeInt(min = 1)
		public static int glassBottleSize = 100;

		public static boolean enableFluidRender = true;
	}
}
