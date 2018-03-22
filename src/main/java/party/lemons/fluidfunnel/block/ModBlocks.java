package party.lemons.fluidfunnel.block;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import party.lemons.fluidfunnel.block.te.TileEntityTap;
import party.lemons.fluidfunnel.block.te.render.FunnelLiquidRender;
import party.lemons.fluidfunnel.block.te.render.TankLiquidRender;
import party.lemons.fluidfunnel.block.te.TileEntityFunnel;
import party.lemons.fluidfunnel.block.te.TileEntityTank;
import party.lemons.fluidfunnel.config.ModConstants;

/**
 * Created by Sam on 19/03/2018.
 */
@Mod.EventBusSubscriber
@GameRegistry.ObjectHolder(ModConstants.MODID)
public class ModBlocks
{
	@GameRegistry.ObjectHolder("funnel")
	public static Block funnel = Blocks.AIR;

	@GameRegistry.ObjectHolder("tank")
	public static Block tank = Blocks.AIR;

	@GameRegistry.ObjectHolder("tap")
	public static Block tap = Blocks.AIR;

	@SubscribeEvent
	public static void registerBlocks(RegistryEvent.Register<Block> event)
	{
		event.getRegistry().registerAll(
				createBlock(new BlockFunnel(), "funnel"),
				createBlock(new BlockTank(), "tank"),
				createBlock(new BlockTap(), "tap")
		);

		GameRegistry.registerTileEntity(TileEntityFunnel.class, "fluidfunnel:funnel");
		GameRegistry.registerTileEntity(TileEntityTank.class, "fluidfunnel:tank");
		GameRegistry.registerTileEntity(TileEntityTap.class, "fluidfunnel:tap");
	}

	@SubscribeEvent
	public static void registerItemBlocks(RegistryEvent.Register<Item> event)
	{
		event.getRegistry().registerAll(
				createItemBlock(funnel),
				createItemBlock(tank),
				createItemBlock(tap)
		);
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public static void registerItemModels(ModelRegistryEvent event)
	{
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityFunnel.class, new FunnelLiquidRender());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityTank.class, new TankLiquidRender());

		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(funnel), 0, new ModelResourceLocation(funnel.getRegistryName(), "inventory"));
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(tank), 0, new ModelResourceLocation(tank.getRegistryName(), "inventory"));
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(tap), 0, new ModelResourceLocation(tap.getRegistryName(), "inventory"));

		ModelLoader.setCustomStateMapper(
				funnel, (new StateMap.Builder()).ignore(BlockFunnel.ENABLED).build()
		);
	}

	public static ItemBlock createItemBlock(Block block)
	{
		ItemBlock ib = new ItemBlock(block);
		ib.setRegistryName(block.getRegistryName());

		return ib;
	}

	public static Block createBlock(Block block, String name)
	{
		return block.setRegistryName(name).setUnlocalizedName(ModConstants.MODID + ":" + name);
	}
}
