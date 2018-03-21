package party.lemons.fluidfunnel;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import party.lemons.fluidfunnel.block.te.behaviour.BehaviorInit;
import party.lemons.fluidfunnel.config.ModConstants;
import party.lemons.fluidfunnel.message.Messages;

/**
 * Created by Sam on 19/03/2018.
 */
@Mod(modid = ModConstants.MODID, name = ModConstants.NAME, version = ModConstants.VERSION)
public class FluidFunnel
{
	@Mod.Instance(ModConstants.MODID)
	public static FluidFunnel INSTANCE;

	public static SimpleNetworkWrapper NETWORK = NetworkRegistry.INSTANCE.newSimpleChannel(ModConstants.MODID);

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		Messages.init();
		BehaviorInit.init();
	}
}
