package party.lemons.fluidfunnel.message;

import net.minecraftforge.fml.relauncher.Side;
import party.lemons.fluidfunnel.FluidFunnel;

/**
 * Created by Sam on 19/03/2018.
 */
public class Messages
{
	private static int id = 1;

	public static void init()
	{
		FluidFunnel.NETWORK.registerMessage(MessageUpdateFunnel.Handler.class, MessageUpdateFunnel.class, id++, Side.CLIENT);
		FluidFunnel.NETWORK.registerMessage(MessageUpdateTank.Handler.class, MessageUpdateTank.class, id++, Side.CLIENT);
		FluidFunnel.NETWORK.registerMessage(MessageRemoveTile.Handler.class, MessageRemoveTile.class, id++, Side.CLIENT);
	}
}
