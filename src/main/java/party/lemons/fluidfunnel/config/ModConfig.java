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
		@Config.Comment("How much fluid should the funnel hold? (millibuckets)")
		public static int funnelCapacity = 1000;
		@Config.RangeInt(min = 0)

		@Config.Comment("How much fluid should the funnel transfer at once? (milibuckets")
		public static int funnelTransferMax = 100;

		@Config.RangeInt(min = 0)
		@Config.Comment("How often should the funnel transfer fluids? (ticks)")
		public static int funnelCooldown = 15;

		@Config.RequiresWorldRestart
		@Config.RangeInt(min = 1)
		@Config.Comment("How much fluid should the tap hold? (millibuckets)")
		public static int tapCapacity = 1000;

		@Config.RangeInt(min = 0)
		@Config.Comment("How much fluid should the tap transfer at once? (milibuckets")
		public static int tapTransferMax = 100;

		@Config.RangeInt(min = 0)
		@Config.Comment("How often should the tap transfer fluids? (ticks)")
		public static int tapCooldown = 15;

		@Config.Comment("Can funnels place fluids in the world?")
		public static boolean placeInWorld = true;

		@Config.Comment("can funnels take fluids from the world?")
		public static boolean takeFromWorld = true;

		@Config.Comment("Can funnels interact with cauldrons?")
		public static boolean fillCauldron = true;

		@Config.Comment("Can funnels fill items within containers? (chests, dispensers, etc)")
		public static boolean fillContainers = true;

		@Config.RequiresWorldRestart
		@Config.RangeInt(min = 1)
		@Config.Comment("How much fluid should the tank hold? (millibuckets)")
		public static int tankCapacity = 10000;

		@Config.RangeInt(min = 1)
		@Config.Comment("How much fluid should a glass bottle hold? (millibuckets)")
		public static int glassBottleSize = 100;

		@Config.Comment("Should fluid be rendered in tanks/funnels? (millibuckets)")
		public static boolean enableFluidRender = true;
	}
}
